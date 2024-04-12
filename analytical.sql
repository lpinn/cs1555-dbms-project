/* First analytical query */
CREATE OR REPLACE VIEW olympic_schema.country_rankings AS
SELECT country_code, country_name, olympiad_count, RANK() OVER (ORDER BY (olympiad_count) DESC) participation_rank
FROM (SELECT c.country_code, c.country_name, COUNT(DISTINCT t.olympiad) AS olympiad_count
      FROM olympic_schema.team t
               JOIN olympic_schema.country c
                    ON t.country = c.country_code
      GROUP BY c.country_code, c.country_name
) rankings;

/* Second analytical query */
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
