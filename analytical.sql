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