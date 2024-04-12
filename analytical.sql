/* First analytical query */
--We're utilizing a view to get the rankings of the number of countries that participated 
--across multiple olympiads. 

--We're using the subquery to get the DISTINCT COUNT of olympiads for each country and setting 
--that to olympiad count 

--Then we are applying RANK on the results of the subquery to get the participation_rank column 
CREATE OR REPLACE VIEW olympic_schema.country_rankings AS
SELECT country_code, country_name, olympiad_count, RANK() OVER (ORDER BY (olympiad_count) DESC) participation_rank
FROM (SELECT c.country_code, c.country_name, COUNT(DISTINCT t.olympiad) AS olympiad_count
      FROM olympic_schema.team t
               JOIN olympic_schema.country c
                    ON t.country = c.country_code
      GROUP BY c.country_code, c.country_name
) rankings;

/* Second analytical query */
--Here we are mostly joining on multiple tables - TEAM, TEAM_MEMBERS, PARTICIPANT, MEDAL, PLACEMENT
--to get the most successful participants across olympiads. we are just summing up the points 
--and fetching the results based on DESCENDING order of total points 

--We use conditional statements to filter the olympiads and get the results if an olympiad number is passed 
CREATE OR REPLACE FUNCTION olympic_schema.most_successful_participants_in_olympiad(
    olympiad_num VARCHAR(30),
    k INTEGER
)
RETURNS TABLE
(
    participant_id INTEGER,
    total_points   BIGINT
)
AS $$
DECLARE
BEGIN
    IF (olympiad_num = 'ALL') THEN
    RETURN QUERY
        SELECT pa.participant_id, SUM(points) total_points
        FROM olympic_schema.placement p
            JOIN olympic_schema.team t ON p.team = t.team_id
            JOIN olympic_schema.team_members tm ON t.team_id = tm.team
            JOIN olympic_schema.participant pa ON tm.participant = pa.participant_id
            JOIN olympic_schema.medal m ON p.medal = m.type
        GROUP BY pa.participant_id
        ORDER BY total_points DESC
        FETCH first k rows only;
    ELSE
    RETURN QUERY
        SELECT pa.participant_id, SUM(points) total_points
        FROM olympic_schema.placement p
            JOIN olympic_schema.team t ON p.team = t.team_id
            JOIN olympic_schema.team_members tm ON t.team_id = tm.team
            JOIN olympic_schema.participant pa ON tm.participant = pa.participant_id
            JOIN olympic_schema.medal m ON p.medal = m.type
        WHERE t.olympiad = olympiad_num
        GROUP BY pa.participant_id
        ORDER BY total_points desc
        FETCH first k rows only;

    END IF;
END;
$$ LANGUAGE plpgsql;

----------------------------------------------------------------------------------------------
----------------------------------------
--- We are assuming that every event and team in placement are for the
--- same sport, and so can join sport to placement, and can count the
--- entries in placement to determine the number of teams who
--- participated in an event. We are assuming that placement is up-to-date,
--- and we are both eligible and disqualified teams as having participated
--- in a sport.
----------------------------------------
DROP FUNCTION IF EXISTS top_sports(x integer, k integer);
CREATE OR REPLACE FUNCTION top_sports(x integer, k integer)
RETURNS TABLE
        (
            sport_id   INTEGER,
            sport_name VARCHAR(30),
            team_count BIGINT

        )
AS
$$
    DECLARE
        interval_text text;
    BEGIN
        interval_text := x || ' years';

        RETURN QUERY SELECT T1.sport_id, T1.sport_name, COUNT(T2.team) AS team_count
            FROM ((SELECT  S.sport_id, S.sport_name
                FROM olympic_schema.SPORT AS S) AS T1
            JOIN (SELECT P.team, E.sport
                FROM olympic_schema.EVENT AS E
                JOIN olympic_schema.PLACEMENT AS P
                ON P.event = E.event_id
                WHERE (current_timestamp- interval_text::interval) <= E.date) AS T2
            ON T1.sport_id = T2.sport)
            GROUP BY T1.sport_id, T1.sport_name
            ORDER BY team_count DESC
            LIMIT k;
        --RETURN QUERY SELECT T1.sport_id, T1.sport_name, 0 AS team_count
          --  FROM sport AS T1;



    END;

$$ LANGUAGE plpgsql;

--This one is incomplete - could not test 
DROP FUNCTION IF EXISTS connected_coaches(c1 integer, c2 integer);
CREATE OR REPLACE FUNCTION connected_coaches(c1 integer, c2 integer)
RETURNS TEXT
AS
$$
DECLARE
    connection TEXT;
    --c1 integer = 8;
    --c2 integer = 3;
    c3 integer;
    c4 integer;
BEGIN
    IF(c1 = c2) THEN
        connection := c1 || ' = ' || c2;
        RETURN connection;
    END IF;

    /*CREATE OR REPLACE TABLE C_INPUT(
        c1 integer,
        c2 integer;
     );   */

    --- all coaches and olympiads
    CREATE OR REPLACE VIEW C_TOTAL AS
        SELECT DISTINCT T.coach, T.olympiad
        FROM olympic_schema.TEAM AS T;

    CREATE OR REPLACE VIEW C1_VIEW AS
        SELECT DISTINCT T1.coach, T1.olympiad
        FROM olympic_schema.TEAM AS T1
        WHERE T1.coach = c1::INTEGER;

    CREATE OR REPLACE VIEW C2_VIEW AS
        SELECT DISTINCT T2.coach, T2.olympiad
        FROM olympic_schema.TEAM AS T2
        WHERE T2.coach = c2::INTEGER;

    CREATE OR REPLACE VIEW C1_TO_C2 AS
        SELECT *
        FROM C1_VIEW JOIN C2_VIEW
            ON C1_VIEW.olympiad = C2_VIEW.olympiad;

     --- all coaches in years with c1 but c1
    CREATE OR REPLACE VIEW C1_PLUS AS
        SELECT C1_VIEW.olympiad AS c1_olympiad, C_TOTAL.coach AS c1_coach
        FROM C1_VIEW JOIN C_TOTAL
            ON C1_VIEW.olympiad = C_TOTAL.olympiad
        WHERE C1_VIEW.coach <> C_TOTAL.coach;


     --- all coaches in years with c2 but c2
    CREATE OR REPLACE VIEW C2_PLUS AS
        SELECT C2_VIEW.olympiad AS c2_olympiad, C_TOTAL.coach AS c2_coach
        FROM C2_VIEW JOIN C_TOTAL
            ON C2_VIEW.olympiad = C_TOTAL.olympiad
        WHERE C2_VIEW.coach <> C_TOTAL.coach;

    CREATE OR REPLACE VIEW C3_VIEW AS
        SELECT C1_PLUS.c1_olympiad AS c1_olympiad, C2_PLUS.c2_olympiad AS c2_olympiad, C1_PlUS.c1_coach AS c3_coach
        FROM C1_PLUS JOIN C2_PLUS
            ON C1_PLUS.c1_coach = C2_PLUS.c2_coach;

    CREATE OR REPLACE VIEW C4_VIEW AS
        SELECT C1_PLUS.c1_olympiad AS olympiad, C1_PLUS.c1_coach AS c3_coach, C2_PLUS.c2_coach AS c4_coach
        FROM C1_PLUS JOIN C2_PLUS
            ON C1_PLUS.c1_olympiad = C2_PLUS.c2_olympiad;


    IF((SELECT count(*) FROM C1_TO_C2) > 0) THEN
        connection:= c1 ||' to ' || c2;
        RETURN connection;
    ELSIF((SELECT count(*) FROM C3_VIEW ) > 0) THEN
        SELECT c3_coach AS c3
            FROM C3_VIEW
            LIMIT 1;
        connection := c1 || ' to ' || c3 || ' to ' || c2;
        RETURN connection;
    ELSIF((SELECT count(*) FROM C4_VIEW ) > 0) THEN
        SELECT c3_coach AS c3
            FROM C4_VIEW
            LIMIT 1;
        SELECT c4_coach AS c4
            FROM C4_VIEW
            LIMIT 1;
        connection := c1 ||' to ' || c3 || ' to ' || c4 || ' to ' || c2;
        RETURN connection;
    ELSE
        connection := 'no path found';
        RETURN connection;

    END IF;


    --RETURN connection;

    /*EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';*/
END
$$ LANGUAGE plpgsql;

SELECT * FROM olympic_schema.PLACEMENT;
SELECT * FROM top_sports(0,9);
SELECT  connected_coaches(8, 3);
