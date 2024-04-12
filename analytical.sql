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
    BEGIN

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END;
$$ LANGUAGE plpgsql;