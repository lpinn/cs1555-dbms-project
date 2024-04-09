/* 1. */
CREATE PROCEDURE create_account(
    IN username VARCHAR(30),
    IN passkey VARCHAR(30),
    IN role VARCHAR(12))
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO olympic_schema.ACCOUNT(account_id, username, passkey, role, last_login)
            VALUES(account_id, username, passkey, role, current_timestamp);
        RAISE NOTICE 'Account added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';
    END
$$;

/* 2 */
CREATE OR REPLACE PROCEDURE olympic_schema.remove_account(
    IN aid INTEGER
)
LANGUAGE plpgsql
AS $$
    DECLARE
        row_check INTEGER;
    BEGIN
        DELETE FROM olympic_schema.account
        WHERE olympic_schema.account.account_id = aid;

       GET DIAGNOSTICS row_check = ROW_COUNT;

        COMMIT;

        if(row_check = 0) then
            RAISE NOTICE 'Account with the given account_id does not exist.';
        else
            RAISE NOTICE 'Account was deleted successfully.';
        end if;

    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE EXCEPTION 'Foreign key violation.';
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Error - checking: %', SQLERRM;
    END
$$;

/* 3. */
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
        INSERT INTO olympic_schema.PARTICIPANT(account, first, middle, last, birth_country, dob, gender)
            VALUES(account_id, first, middle, last, birth_country, dob, gender);
        RAISE NOTICE 'Participant added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';
    END
$$;

/* 4. */
CREATE PROCEDURE olympic_schema.remove_participant(
    IN pid INTEGER
)
LANGUAGE plpgsql
AS $$
    DECLARE
        row_check_participant INTEGER;
        row_check_account INTEGER;
    BEGIN
        WITH participant_removal AS (
            DELETE FROM olympic_schema.participant
            WHERE olympic_schema.participant.participant_id = pid
            RETURNING olympic_schema.participant.account
        )
        DELETE FROM olympic_schema.account
        WHERE olympic_schema.account.account_id = participant_removal.account;

        GET DIAGNOSTICS row_check_account = ROW_COUNT;

        COMMIT;

        if(row_check_participant = 0) then
            RAISE NOTICE 'Account with the given account_id does not exist.';
        else
            RAISE NOTICE 'Account was deleted successfully.';
        end if;

    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE EXCEPTION 'Foreign key violation.';
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Error - checking: %', SQLERRM;
    END
$$;

/* 5. */
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

/* 6. */
/* 2 */
CREATE OR REPLACE PROCEDURE olympic_schema.remove_team_member(
    IN pid INTEGER,
    IN tid INTEGER
)
LANGUAGE plpgsql
AS $$
    DECLARE
        row_check INTEGER;
    BEGIN
        DELETE FROM olympic_schema.team_members
        WHERE olympic_schema.team_members.participant = pid
            AND olympic_schema.team_members.team = tid;

       GET DIAGNOSTICS row_check = ROW_COUNT;

        COMMIT;

        if(row_check = 0) then
            RAISE NOTICE 'Team member with the given parameters does not exist.';
        else
            RAISE NOTICE 'Team member was removed successfully.';
        end if;

    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE EXCEPTION 'Foreign key violation.';
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Error - checking: %', SQLERRM;
    END
$$;

/* 7. */
CREATE PROCEDURE register_team(
    IN olympiad VARCHAR(30),
    IN sport INTEGER,
    IN coach CHAR(3),
    IN country CHAR(3),
    IN gender VARCHAR(1)
)
LANGUAGE plpgsql
AS $$
    BEGIN
        INSERT INTO olympic_schema.TEAM(olympiad, sport, coach, country, gender)
            VALUES(olympiad, sport, coach, country, gender);
        RAISE NOTICE 'Team added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';
    END
$$;

/* 8. */
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
        INSERT INTO olympic_schema.EVENT(venue, olympiad, sport, gender, date)
            VALUES(venue_id, olympiad_id, sport_id, gender, date);
        RAISE NOTICE 'Event added successfully.';

    EXCEPTION
        WHEN OTHERS THEN
            RAISE NOTICE 'Error.';
    END
$$;
