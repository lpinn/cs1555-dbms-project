CREATE PROCEDURE add_team_member(
    IN team INTEGER,
    IN participant INTEGER)
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO olympic_schema.TEAM_MEMBERS(team, participant)
            VALUES(team, participant);
        RAISE NOTICE 'Team member added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';
    END
$$;

/* ddl */
create procedure add_participant(IN account_id integer, IN first character varying, IN middle character varying, IN last character varying, IN birth_country character, IN dob timestamp without time zone, IN gender character varying)
    language plpgsql
as
$$
    BEGIN
        INSERT INTO olympic_schema.PARTICIPANT(account, first, middle, last, birth_country, dob, gender)
            VALUES(account_id, first, middle, last, birth_country, dob, gender);
        RAISE NOTICE 'Participant added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';
    END
$$;

alter procedure add_participant(integer, varchar, varchar, varchar, char, timestamp, varchar) owner to postgres;

create procedure create_account(IN username character varying, IN passkey character varying, IN role character varying)
    language plpgsql
as
$$
    BEGIN
        INSERT INTO olympic_schema.ACCOUNT(username, passkey, role, last_login) VALUES(username, passkey, role, current_timestamp);
        RAISE NOTICE 'Account added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';
    END
$$;

alter procedure create_account(varchar, varchar, varchar) owner to postgres;


create or replace procedure add_team_to_event(IN event integer, IN team integer)
    language plpgsql
as
$$
    BEGIN
        INSERT INTO olympic_schema.PLACEMENT(event, team, medal, position) VALUES(event,team, NULL, NULL);
        RAISE NOTICE 'Team added to Event successfully.';

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';

    END
$$;

alter procedure  add_team_to_event(integer, integer) owner to postgres;

create or replace procedure add_event_outcome(IN outcome_event integer, IN outcome_team integer, IN outcome_position integer)
    language  plpgsql
as
$$
    BEGIN
        UPDATE olympic_schema.PLACEMENT
        SET position = outcome_position
        WHERE team = outcome_team AND event = outcome_event;

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';

        
    END
$$;

alter procedure add_event_outcome(integer, integer, integer) owner to postgres;

create or replace procedure disqualify_team(IN disqualify_team integer)
    language plpgsql
as
$$
    BEGIN
        UPDATE olympic_schema.PLACEMENT
        SET position = -1
        WHERE team = disqualify_team;

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';

    END
$$;

alter procedure disqualify_team(integer) owner to postgres;

create or replace function list_venues_in_olympiad(olympiad_id integer)

RETURNS TABLE(
        venue_name varchar(30),
        capacity integer
    )
as
$$
DECLARE
    BEGIN

        EXCEPTION                                
            WHEN OTHERS THEN                     
                RAISE NOTICE 'Error.';           
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listEventsOfOlympiad(olympiad_id integer)
RETURNS TABLE(
        event_id integer,
        venue varchar(30),
        olympiad_num varchar(30),
        sport integer,
        gender olympiad_schema.team_gender_check,
        date timestamp
    )
as
$$
DECLARE
    BEGIN

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listTeamsInEvent(event_id integer)
RETURNS TABLE(
        team_id integer,
        olympiad varchar(30),
        sport integer,
        coach integer,
        country char(3),
        gender olympiad_schema.team_gender_check,
        eligible boolean
    )
as
$$
DECLARE
    BEGIN

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;



CREATE OR REPLACE FUNCTION showPlacementsInEvent(event_id integer)
RETURNS TABLE(
        event_id INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position INTEGER
    )
as
$$
DECLARE
    BEGIN

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listParticipantsOnTeam(event_id integer)
RETURNS TABLE(
        participant_id SERIAL,
        account INTEGER,
        first VARCHAR(30),
        middle VARCHAR(30),
        last VARCHAR(30),
        birth_country CHAR(3),
        dob TIMESTAMP,
        gender olympic_schem.participant_gender_check
    )
as
$$
DECLARE
    BEGIN

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION showPlacementsInEvent(olympic_id integer, country char(3))
RETURNS TABLE(
        event_id INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position INTEGER
    )
as
$$
DECLARE
    BEGIN

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listAthletePlacement(participant_id integer)
RETURNS TABLE(
        event_id INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position INTEGER
    )
as
$$
DECLARE
    BEGIN

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;