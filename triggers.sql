/* setting medals and stuff */ 
CREATE OR REPLACE FUNCTION olympic_schema.set_placement()
RETURNS trigger AS $$
BEGIN
    if(new.position = 1) then
        new.medal = 'Gold';
    elsif(new.position = 2) then
        new.medal = 'Silver';
    elsif(new.position = 3) then
        new.medal = 'Bronze';
    else 
        new.medal = NULL;
    end if;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ASSIGN_MEDAL
    BEFORE INSERT OR UPDATE
    ON olympic_schema.placement
    FOR EACH ROW
EXECUTE FUNCTION olympic_schema.set_placement();

/* handling disqualifying */
CREATE OR REPLACE FUNCTION olympic_schema.set_dq()
RETURNS trigger AS $$
DECLARE
        event_id     INTEGER;
        position_num INTEGER;
        pl_cursor CURSOR FOR
            SELECT event, position
            FROM olympic_schema.placement
            WHERE team = new.team_id;

    BEGIN
        OPEN pl_cursor;
        LOOP
            FETCH pl_cursor INTO event_id, position_num;
            IF NOT FOUND THEN
                EXIT;
            END IF;
            RAISE notice '%',position_num;
            IF (new.eligible = false) then
                UPDATE olympic_schema.placement
                SET position = position - 1
                WHERE event = event_id
                  AND position > position_num;

                UPDATE olympic_schema.placement
                SET position = -1
                WHERE team = new.team_id
                  AND event = event_id;
            END IF;
        END LOOP;
    END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER TEAM_DISQUALIFIED
    BEFORE INSERT OR UPDATE
    ON olympic_schema.team
    FOR EACH ROW
EXECUTE FUNCTION olympic_schema.set_dq();

/* checking gender */
CREATE OR REPLACE FUNCTION olympic_schema.check_gender_f()
RETURNS TRIGGER AS $$
DECLARE
    team_gender char(1);
    participant_gender char(1);
BEGIN
    SELECT  gender INTO team_gender
    FROM    olympic_schema.team t
    WHERE   t.team_id = new.team;

    SELECT  gender INTO participant_gender
    FROM    olympic_schema.participant p
    WHERE   p.participant_id = new.participant;

    if (participant_gender <> team_gender AND team_gender <> 'X') then
        RAISE EXCEPTION 'Participant gender is not matching with team gender constraint';
    end if;

    return NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER CHECK_TEAM_GENDER
    BEFORE INSERT OR UPDATE
    ON olympic_schema.team_members
    FOR each ROW
EXECUTE FUNCTION olympic_schema.check_gender_f();

/* new triggers */ 
CREATE FUNCTION check_placement_sport()
RETURNS TRIGGER AS $$
DECLARE
    team_sport integer;
    event_sport integer;
BEGIN
    SELECT sport INTO team_sport
    FROM TEAM T
    WHERE T.team_id = new.team;

    SELECT sport INTO event_sport
    FROM EVENT E
    WHERE E.event_id = new.event;

    IF(team_sport <> event_sport) THEN
        RAISE EXCEPTION 'Team sport does not match event sport.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER CHECK_PLACEMENT_SPORT
    BEFORE INSERT OR UPDATE
    ON PLACEMENT
    FOR each ROW
EXECUTE FUNCTION check_placement_sport();

CREATE FUNCTION check_placement_gender()
RETURNS TRIGGER AS $$
DECLARE
    team_gender char(1);
    event_gender char(1);
BEGIN
    SELECT gender INTO team_gender
    FROM TEAM T
    WHERE T.team_id = new.team;

    SELECT gender INTO event_gender
    FROM EVENT E
    WHERE E.event_id = new.event;

    IF(team_gender <> event_gender AND team_gender <> 'X') THEN
        RAISE EXCEPTION 'Team gender does not match event gender.';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER CHECK_PLACEMENT_GENDER
    BEFORE INSERT OR UPDATE
    ON PLACEMENT
    FOR each ROW
EXECUTE FUNCTION check_placement_gender();
