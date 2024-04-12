----------------------------------------
--- We are assuming that every event and team in placement are for the
--- same sport, and so can join sport to placement, and can count the
--- entries in placement to determine the number of teams who
--- participated in an event. We are assuming that placement is up-to-date,
--- and we are both eligible and disqualified teams as having participated
--- in a sport.
----------------------------------------
CREATE OR REPLACE FUNCTION top_sports(x integer, k integer)
RETURNS TABLE
        (
            sport_id   INTEGER,
            sport_name INTEGER,
            team_count INTEGER

        )
AS
$$
    BEGIN
        RETURN QUERY SELECT T1.sport_id, T1.sport_name, COUNT(T2.team) AS team_count
            FROM (SELECT  S.sport_id, S.sport_name
                FROM olympic_schema.SPORT AS S) AS T1
            JOIN (SELECT P.team, E.sport
                FROM (olympic_schema.EVENT AS E
                JOIN olympic_schema.PLACEMENT AS P
                ON P.event = E.event_id)
                WHERE age(E.date) <= x ) AS T2
            ON T1.sport_id = T2.sport
            GROUP BY T2.team
            ORDER BY team_count DESC
            LIMIT k;


        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END;

$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION connected_coaches(c1 integer, c2 integer)
RETURNS TEXT
AS
$$
DECLARE
    connection text;
BEGIN
    IF(c1 = c2) THEN
        connection := c1 + '= ' + c2;
        RETURN connection;
    end if;

    --- all coaches and olympiads
    CREATE VIEW C_TOTAL AS
        SELECT DISTINCT T.coach, T.olympiad
        FROM olympic_schema.TEAM AS T;

    CREATE VIEW C1 AS
        SELECT DISTINCT T.coach, T.olympiad
        FROM olympic_schema.TEAM AS T
        WHERE T.coach = c1;

    CREATE VIEW C2 AS
        SELECT DISTINCT T.coach, T.olympiad
        FROM olympic_schema.TEAM AS T
        WHERE T.coach = c2;

    CREATE VIEW C1_TO_C2 AS
        SELECT *
        FROM C1 JOIN C2
            ON C1.olympiad = C2.olympiad;

     --- all coaches in years with c1 but c1
    CREATE VIEW C1_PLUS AS
        SELECT C1.olympiad AS olympiad, C_TOTAL.coach AS coach
        FROM C1 JOIN C_TOTAL
            ON C1.olympiad = C_TOTAL.olympiad
        WHERE C1.coach != C_TOTAL.coach;


     --- all coaches in years with c2 but c2
    CREATE VIEW C2_PLUS AS
        SELECT C2.olympiad AS olympiad, C_TOTAL.coach AS coach
        FROM C2 JOIN C_TOTAL
            ON C2.olympiad = C_TOTAL.olympiad
        WHERE C2.coach != C_TOTAL.coach;

    CREATE VIEW C3 AS
        SELECT C1_PLUS.olympiad, C2_PLUS.olympiad, C1_PlUS.coach
        FROM C1_PLUS JOIN C2_PLUS
            ON C1_PLUS.coach = C2_PLUS.coach;

    CREATE VIEW C4 AS
        SELECT C1_PLUS.olympiad, C1_PLUS.coach, C2_PLUS.coach
        FROM C1_PLUS JOIN C2_PLUS
            ON C1_PLUS.olympiad = C2_PLUS.olympiad;


    IF((SELECT count(*) FROM C1_TO_C2) > 0) THEN
        connection:= c1 + 'to' + c2;
        RETURN connection;
    ELSIF

    END IF;




    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';
END;
$$ LANGUAGE plpgsql;