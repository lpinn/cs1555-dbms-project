SET SCHEMA 'olympic_schema';

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
    BEGIN
        RETURN QUERY SELECT T1.sport_id, T1.sport_name, COUNT(T2.team) AS team_count
            FROM ((SELECT  S.sport_id, S.sport_name
                FROM olympic_schema.SPORT AS S) AS T1
            JOIN (SELECT P.team, E.sport
                FROM olympic_schema.EVENT AS E
                JOIN olympic_schema.PLACEMENT AS P
                ON P.event = E.event_id
                /*WHERE /*current_timestamp - x * 365 > E.date*/ age(E.date) <= x */) AS T2
            ON T1.sport_id = T2.sport)
            GROUP BY T1.sport_id, T1.sport_name
            ORDER BY team_count DESC
            LIMIT k;
        --RETURN QUERY SELECT T1.sport_id, T1.sport_name, 0 AS team_count
          --  FROM sport AS T1;



    END;

$$ LANGUAGE plpgsql;

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

    --- all coaches and olympiads
    CREATE OR REPLACE VIEW C_TOTAL AS
        SELECT DISTINCT T.coach, T.olympiad
        FROM olympic_schema.TEAM AS T;

    CREATE OR REPLACE VIEW C1_VIEW AS
        SELECT DISTINCT T1.coach, T1.olympiad
        FROM olympic_schema.TEAM AS T1
        WHERE T1.coach = c1;

    CREATE OR REPLACE VIEW C2_VIEW AS
        SELECT DISTINCT T2.coach, T2.olympiad
        FROM olympic_schema.TEAM AS T2
        WHERE T2.coach = c2;

    CREATE OR REPLACE VIEW C1_TO_C2 AS
        SELECT
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


    RETURN connection;

    /*EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';*/
END
$$ LANGUAGE plpgsql;

SELECT * FROM olympic_schema.PLACEMENT;
SELECT * FROM top_sports(1000,7);
SELECT connected_coaches(8, 3);