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

create or replace function list_venues_in_olympiad(olympiad_id varchar(30))

RETURNS TABLE(
        venue_name varchar(30),
        capacity integer
    )
as
$$
    BEGIN

        RETURN QUERY SELECT V.venue_name, V.capacity
            FROM olympic_schema.VENUE AS V
            WHERE V.venue_name IN (SELECT E.venue
                                   FROM olympic_schema.EVENT as E
                                   WHERE olympiad_id = E.olympiad
                     );

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
        gender olympic_schema.team_gender_check,
        date timestamp
    )
as
$$
    BEGIN

        RETURN QUERY SELECT E.event_id, E.venue, E.olympiad_num, E.sport, E.gender, E.date
            FROM olympic_schema.EVENT AS E
            WHERE olympiad_id = E.olympiad;

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listTeamsInEvent(event_id integer)
RETURNS TABLE(
        team_id integer
        ---olympiad varchar(30),
        ---sport integer,
        --coach integer,
       --- country char(3),
        ---gender olympiad_schema.team_gender_check,
        ---eligible boolean
    )
as
$$

    BEGIN
        RETURN QUERY SELECT P.team_id
            FROM olympic_schema.PLACEMENTS AS P
            WHERE event_id = P.event_id;

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;



CREATE OR REPLACE FUNCTION showPlacementsInEvent(event integer)
RETURNS TABLE(
        event_id INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position INTEGER
    )
as
$$

    BEGIN

    BEGIN
        RETURN QUERY SELECT P.event_id, P.team, P.medal, P.position
            FROM olympic_schema.PLACEMENT AS P
            WHERE event = P.event_id;


        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listParticipantsOnTeam(team_this integer)
RETURNS TABLE(
        participant_id SERIAL,
        account INTEGER,
        first VARCHAR(30),
        middle VARCHAR(30),
        last VARCHAR(30),
        birth_country CHAR(3),
        dob TIMESTAMP,
        gender olympic_schema.participant_gender_check
    )
as
$$
---DECLARE
    BEGIN

        RETURN QUERY ((SELECT P.participant_id, P.account, P.first, P.middle, P.last, P.birth_country, P.dob, P.gender
                            FROM olympic_schema.PARTICIPANT AS P
                            WHERE P.particpant_id = (SELECT T.coach
                                                        FROM olympic_schema.TEAM AS T
                                                        WHERE T.team_id = team_this))
                        UNION
                        (SELECT P.participant_id, P.account, P.first, P.middle, P.last, P.birth_country, P.dob, P.gender
                            FROM olympic_schema.PARTICIPANT AS P
                            WHERE P.participant_id IN (SELECT M.participant
                                                       FROM olympic_schema.TEAM_MEMBERS AS M
                                                       WHERE M.team = team_this)
                        ));

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION showPlacementsInEvent(olympic_id varchar(30), country_id char(3))
RETURNS TABLE(
        event_id INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position INTEGER
    )
as
$$
---DECLARE
    BEGIN
        RETURN QUERY SELECT P.event_id, P.team, P.medal, P.position
            FROM olympic_schema.PLACEMENT AS P
            WHERE P.team IN (SELECT T.team_id
                             FROM olympic_schema.TEAM AS T
                             WHERE T.country = country_id)
                AND P.event_id IN(SELECT E.event_id
                               FROM olympic_schema.EVENT AS E
                               WHERE E.olympiad = olympic_id);


        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listAthletePlacement(participant integer)
RETURNS TABLE(
        event_id INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position INTEGER
    )
as
$$
---DECLARE
    BEGIN

        RETURN QUERY SELECT P.event_id, P.team, P.medal, P.position
            FROM olympic_schema.PLACEMENT
            WHERE P.team IN (SELECT M.TEAM
                                FROM olympic_schema.TEAM_MEMBERS AS M
                                WHERE M.participant_id = participant);

        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Error.';
    END
$$  language plpgsql;

--- pk code
--- declare
---     pk
--- select 1 + count(distinct table.pk) into pk
    --- from table;
--- insert ...