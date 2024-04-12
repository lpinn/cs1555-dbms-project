CREATE FUNCTION set_placement() 
RETURNS trigger AS $$
BEGIN
    if(new.position = 1) then
        new.medal = 'Gold';
    elsif(new.position = 2) then
        new.medal = 'Silver';
    elsif(new.position = 3) then
        new.medal = 'Bronze';
    elseif(new.position is null) then  
        new.medal = NULL;
    end if;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER ASSIGN_MEDAL
    BEFORE INSERT OR UPDATE 
    ON olympic_schema.placement
    FOR EACH ROW
EXECUTE FUNCTION set_placement();

CREATE FUNCTION set_dq()
RETURNS trigger AS $$
DECLARE
    event_id integer;
    position_dq integer;
BEGIN
    SELECT e.event_id INTO event_id
    FROM olympic_schema.event e
    JOIN olympic_schema.sport s
        ON e.sport = s.sport_id
    WHERE s.sport_id = new.sport;

    SELECT position INTO position_dq
    FROM olympic_schema.placement
    WHERE team = new.team_id;

    if(new.eligible = false) then
        UPDATE placement
        SET position = position - 1
        WHERE event = event_id
        AND position > position_dq;

        UPDATE placement
        SET position = null
        WHERE team = new.team_id;
    end if;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER TEAM_DISQUALIFIED
    BEFORE INSERT OR UPDATE
    ON olympic_schema.team
    FOR EACH ROW
EXECUTE FUNCTION set_dq();

CREATE FUNCTION check_gender_f()
RETURNS TRIGGER AS $$
DECLARE
    team_gender char(1);
    participant_gender char(1);
BEGIN
    SELECT gender INTO team_gender
    FROM    team t
    WHERE   t.team_id = new.team;

    SELECT  gender INTO participant_gender
    FROM    participant p
    WHERE   p.participant_id = new.participant;

    if (participant_gender <> team_gender AND team_gender <> 'X') then
        RAISE EXCEPTION 'Participant gender is not matching with team gender constraint';
    end if;

    return NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER CHECK_TEAM_GENDER
    BEFORE INSERT OR UPDATE
    ON team_members
    FOR each ROW
EXECUTE FUNCTION check_gender_f();

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