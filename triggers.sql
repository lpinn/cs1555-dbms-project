--------------------------------------------------------------------------------------------
/* setting medals and stuff */
/* tables used - PLACEMENT */ 
CREATE OR REPLACE FUNCTION olympic_schema.set_placement()
RETURNS trigger AS $$
BEGIN
    --We check to see what the position is and then assign a value to medal
    --in the placement table 
    if(new.position = 1) then
        new.medal = 'Gold';
    elsif(new.position = 2) then
        new.medal = 'Silver';
    elsif(new.position = 3) then
        new.medal = 'Bronze';
    else
        --If the position isn't 1,2,3, we set the medal to NULL 
        new.medal = NULL;
    end if;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER ASSIGN_MEDAL
    BEFORE INSERT OR UPDATE
    ON olympic_schema.placement
    FOR EACH ROW
EXECUTE FUNCTION olympic_schema.set_placement();
--------------------------------------------------------------------------------------------
/* handling disqualifying */
/* Tables used - PLACEMENT, TEAM */
CREATE OR REPLACE FUNCTION olympic_schema.set_dq()
RETURNS trigger AS $$
DECLARE
        event_id     INTEGER;
        position_num INTEGER;
        -- Using a cursor to go through the placement table
        -- We select event and position where team id is equal to 
        -- the one we want to disqualify 
        pl_cursor CURSOR FOR
            SELECT event, position
            FROM olympic_schema.placement
            WHERE team = new.team_id;

    BEGIN
        OPEN pl_cursor;
        LOOP
            -- Getting the data and 
            FETCH pl_cursor INTO event_id, position_num;
            -- If there exists data that meets our reqs we don't exit 
            IF NOT FOUND THEN
                EXIT;
            END IF;
            -- We check to see if eligibility was set to false, and 
            -- if it was, we handle the position changing appropriately 
            -- and also set the position of the team that was dq'd to 
            -- -1 
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
        CLOSE pl_cursor;
        RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER TEAM_DISQUALIFIED
    BEFORE INSERT OR UPDATE
    ON olympic_schema.team
    FOR EACH ROW
EXECUTE FUNCTION olympic_schema.set_dq();
--------------------------------------------------------------------------------------------
/* checking gender */
CREATE OR REPLACE FUNCTION olympic_schema.check_gender_f()
RETURNS TRIGGER AS $$
DECLARE
    team_gender char(1);
    participant_gender char(1);
BEGIN
    -- First get the gender from team - this is what we're going to check against 
    SELECT  gender INTO team_gender
    FROM    olympic_schema.team t
    WHERE   t.team_id = new.team;

    -- Then get the gender of the participant that we're checking 
    SELECT  gender INTO participant_gender
    FROM    olympic_schema.participant p
    WHERE   p.participant_id = new.participant;

    -- Here we check to see if the values are not equal 
    if (participant_gender <> team_gender AND team_gender <> 'X') then
        RAISE EXCEPTION 'Participant gender is not matching with team gender constraint';
    end if;

    return NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER CHECK_TEAM_GENDER
    BEFORE INSERT OR UPDATE
    ON olympic_schema.team_members
    FOR each ROW
EXECUTE FUNCTION olympic_schema.check_gender_f();
--------------------------------------------------------------------------------------------
/* new triggers */
CREATE OR REPLACE FUNCTION olympic_schema.check_placement_sport()
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

CREATE OR REPLACE TRIGGER CHECK_PLACEMENT_SPORT
    BEFORE INSERT OR UPDATE
    ON olympic_schema.PLACEMENT
    FOR each ROW
EXECUTE FUNCTION olympic_schema.check_placement_sport();

CREATE OR REPLACE FUNCTION olympic_schema.check_placement_gender()
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

CREATE OR REPLACE TRIGGER CHECK_PLACEMENT_GENDER
    BEFORE INSERT OR UPDATE
    ON olympic_schema.PLACEMENT
    FOR each ROW
EXECUTE FUNCTION olympic_schema.check_placement_gender();
