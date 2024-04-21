/* 1 - CREATE ACCOUNT */
CREATE OR REPLACE PROCEDURE create_account(
    IN username VARCHAR(30),
    IN passkey VARCHAR(30),
    IN role VARCHAR(12))
    LANGUAGE plpgsql
AS
$$
DECLARE
BEGIN
    --Inserting into olympic_schema.ACCOUNT--
    INSERT INTO olympic_schema.ACCOUNT(username, passkey, role, last_login)
    VALUES (username, passkey, role, current_timestamp);
    RAISE NOTICE 'Account added successfully.';

EXCEPTION
    --If someone tries to insert an ACCOUNT with an already existing name - raise EXCEPTION
    WHEN unique_violation THEN
        RAISE NOTICE 'Unique constraint failed: %', SQLERRM;
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Generic Error: %', SQLERRM;
END
$$;

/* 2 - REMOVE ACCOUNT */
/* We're assuming that deleting account doesn't delete participants and that participants can exist 
   without accounts
*/
CREATE OR REPLACE PROCEDURE olympic_schema.remove_account(
    IN aid INTEGER
)
LANGUAGE plpgsql
AS $$
    DECLARE
        row_check INTEGER;
    BEGIN
        --First update and set account to null in participant--
        UPDATE olympic_schema.participant
            SET account = NULL
        WHERE olympic_schema.account.account_id = aid;

        --Then delete the account 
        DELETE FROM olympic_schema.account
        WHERE olympic_schema.account.account_id = aid;

        --Check if a row was deleted, if it was then the account was removed properly, or did not exist-- 
        GET DIAGNOSTICS row_check = ROW_COUNT;

        if(row_check = 0) then
            RAISE NOTICE 'Account with the given account_id does not exist.';
        else
            RAISE NOTICE 'Account was deleted successfully.';
        end if;

    --Handle exceptions--
    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE EXCEPTION 'Foreign key violation.';
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$;

/* 3. - ADD PARTICIPANT */
CREATE PROCEDURE add_participant(
    IN account_id INTEGER,
    IN first VARCHAR(30),
    IN middle VARCHAR(30),
    IN last VARCHAR(30),
    IN birth_country CHAR(3),
    IN dob TIMESTAMP,
    IN gender VARCHAR(1)
)
LANGUAGE plpgsql
AS $$
    BEGIN
        --Similar to create_account--
        INSERT INTO olympic_schema.PARTICIPANT(account, first, middle, last, birth_country, dob, gender)
            VALUES(account_id, first, middle, last, birth_country, dob, gender);
        RAISE NOTICE 'Participant added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$;

/* 4. - REMOVE PARTICIPANT */
CREATE OR REPLACE PROCEDURE olympic_schema.remove_participant(
    IN pid INTEGER
)
LANGUAGE plpgsql
AS $$
    DECLARE
    --Using a CTE to remove participant--
    --ON DELETE CASCADE is included in the schema but I initially implemented this operation 
    --with a CTE. After testing, to me, it doesn't seem like DELETING the account after the 
    --initial WITH query is causing any problems. 

    --It might be redundant, however due to include different types of queries, I decided to 
    --keep this. I understand that it would be fine to not use a CTE in this case. 
    BEGIN
        WITH participant_removal AS (
            DELETE FROM olympic_schema.participant
            WHERE olympic_schema.participant.participant_id = pid
            RETURNING olympic_schema.participant.account
        )
        DELETE FROM olympic_schema.account
        WHERE olympic_schema.account.account_id in (select participant_removal.account from participant_removal);

    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Error - checking: %', SQLERRM;

    END
$$;

/* 5. - ADD TEAM MEMBER */
CREATE OR REPLACE PROCEDURE add_team_member(
    IN team INTEGER,
    IN participant INTEGER)
LANGUAGE plpgsql
AS $$
    BEGIN
        --inserting into team member 
        INSERT INTO olympic_schema.TEAM_MEMBERS(team, participant)
            VALUES(team, participant);
        RAISE NOTICE 'Team member added successfully.';

    --otherwise handle exceptions 
    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;

    END
$$;

/* 6. - REMOVE TEAM MEMBER */
CREATE OR REPLACE PROCEDURE olympic_schema.remove_team_member(
    IN pid INTEGER,
    IN tid INTEGER
)
LANGUAGE plpgsql
AS $$
    DECLARE
        row_check INTEGER;
    BEGIN
        --Deleting when the reqs match 
        DELETE FROM olympic_schema.team_members
        WHERE olympic_schema.team_members.participant = pid
            AND olympic_schema.team_members.team = tid;

        --using row check to see if the row was deleted 
        GET DIAGNOSTICS row_check = ROW_COUNT;

        IF (row_check = 0) THEN
            RAISE EXCEPTION 'Team member with the given parameters does not exist.';
        ELSE
            RAISE NOTICE 'Team member was removed successfully.';
        END IF;

    --handling exceptions 
    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$;

/* 7. - REGISTER TEAM */
CREATE OR REPLACE PROCEDURE olympic_schema.register_team(
    IN olympiad VARCHAR(30),
    IN sport INTEGER,
    IN coach INTEGER,
    IN country CHAR(3),
    IN gender VARCHAR(1)
)
LANGUAGE plpgsql
AS $$
    BEGIN
        -- inserting values 
        INSERT INTO olympic_schema.TEAM(olympiad, sport, coach, country, gender, eligible)
            VALUES(olympiad, sport, coach, country, gender, true);
        RAISE NOTICE 'Team added successfully.';

    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE EXCEPTION 'Foreign key violation.';
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$;

/* 8. - ADD EVENT */
CREATE PROCEDURE add_event(
    IN venue_id VARCHAR(30),
    IN olympiad_id VARCHAR(30),
    IN sport_id INTEGER,
    IN gender VARCHAR(1),
    IN date TIMESTAMP
)
LANGUAGE plpgsql
AS $$
    BEGIN
        --inserting 
        INSERT INTO olympic_schema.EVENT(venue, olympiad, sport, gender, date)
            VALUES(venue_id, olympiad_id, sport_id, gender, date);
        RAISE NOTICE 'Event added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$;

/* 9. - ADD  TEAM TO EVENT */
create or replace procedure add_team_to_event(IN event integer, IN team integer)
    language plpgsql
as
$$
    BEGIN
        INSERT INTO olympic_schema.PLACEMENT(event, team, medal, position) VALUES(event,team, NULL, NULL);
        RAISE NOTICE 'Team added to Event successfully.';

        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;

    END
$$;

alter procedure  add_team_to_event(integer, integer) owner to postgres;


/* 9. - ADD EVENT OUTCOME*/
/* need to add medal considerations -- handeled by trigger?
 * need to add no-event considerations
 */
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
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;

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

        UPDATE olympic_schema.team
            SET eligible = FALSE
            WHERE team_id = disqualify_team;

        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;

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
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;        
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listEventsOfOlympiad(olympiad_id VARCHAR(30))
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

        RETURN QUERY SELECT E.event_id, E.venue, E.olympiad, E.sport, E.gender, E.date
            FROM olympic_schema.EVENT AS E
            WHERE olympiad_id = E.olympiad;

        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;
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
        RETURN QUERY SELECT P.team
            FROM olympic_schema.PLACEMENT AS P
            WHERE event_id = P.event;

        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$  language plpgsql;



CREATE OR REPLACE FUNCTION showPlacementsInEvent(event_id integer)
RETURNS TABLE(
        event INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position_id INTEGER
    )
as
$$

    BEGIN
        RETURN QUERY SELECT P.event, P.team, P.medal, P.position
            FROM olympic_schema.PLACEMENT AS P
            WHERE event_id = P.event;


        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listParticipantsOnTeam(team_this integer)
RETURNS TABLE(
        participant_id INTEGER,
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
                            WHERE P.participant_id = (SELECT T.coach
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
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listCountryPlacementsInOlympiad(olympic_id varchar(30), country_id char(3))
RETURNS TABLE(
        event_id INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position_id INTEGER
    )
AS
$$
---DECLARE
    BEGIN
        RETURN QUERY SELECT P.event, P.team, P.medal, P.position
            FROM olympic_schema.PLACEMENT AS P
            WHERE P.team IN (SELECT T.team_id
                             FROM olympic_schema.TEAM AS T
                             WHERE T.country = country_id)
                AND P.event IN(SELECT E.event_id
                               FROM olympic_schema.EVENT AS E
                               WHERE E.olympiad = olympic_id);


        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$  language plpgsql;

CREATE OR REPLACE FUNCTION listAthletePlacement(participant_id integer)
RETURNS TABLE(
        event_id INTEGER,
        team INTEGER,
        medal olympic_schema.medal_type_check,
        position_id INTEGER
    )
as
$$
---DECLARE
    BEGIN

        RETURN QUERY SELECT P.event, P.team, P.medal, P.position
            FROM olympic_schema.PLACEMENT AS P
            WHERE P.team IN (SELECT M.TEAM
                                FROM olympic_schema.TEAM_MEMBERS AS M
                                WHERE M.participant = participant_id);

        EXCEPTION
            WHEN OTHERS THEN
                RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$  language plpgsql;
