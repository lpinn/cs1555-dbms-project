/* 1 */
CREATE OR REPLACE PROCEDURE create_account(
    IN username VARCHAR(30),
    IN passkey VARCHAR(30),
    IN role VARCHAR(12))
    LANGUAGE plpgsql
AS
$$
DECLARE
BEGIN
    /* START TRANSACTION; */

    INSERT INTO olympic_schema.ACCOUNT(username, passkey, role, last_login)
    VALUES (username, passkey, role, current_timestamp);
    RAISE NOTICE 'Account added successfully.';

    /* COMMIT; */

EXCEPTION
    WHEN unique_violation THEN
        RAISE NOTICE 'Unique constraint failed: %', SQLERRM;
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Generic Error: %', SQLERRM;
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
        UPDATE olympic_schema.participant
            SET account = NULL
        WHERE olympic_schema.account.account_id = aid;

        DELETE FROM olympic_schema.account
        WHERE olympic_schema.account.account_id = aid;

        GET DIAGNOSTICS row_check = ROW_COUNT;

        if(row_check = 0) then
            RAISE NOTICE 'Account with the given account_id does not exist.';
        else
            RAISE NOTICE 'Account was deleted successfully.';
        end if;

    EXCEPTION
        WHEN foreign_key_violation THEN
            RAISE EXCEPTION 'Foreign key violation.';
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;
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
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$;

/* 4. */
CREATE OR REPLACE PROCEDURE olympic_schema.remove_participant(
    IN pid INTEGER
)
LANGUAGE plpgsql
AS $$
    DECLARE
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

/* 5. */
CREATE OR REPLACE PROCEDURE add_team_member(
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
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;

    END
$$;

/* 6. */
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

        IF (row_check = 0) THEN
            RAISE EXCEPTION 'Team member with the given parameters does not exist.';
        ELSE
            RAISE NOTICE 'Team member was removed successfully.';
        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            RAISE EXCEPTION 'Generic Error: %', SQLERRM;
    END
$$;

/* 7. */
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


call olympic_schema.create_account('user17', 'passkey', 'Participant');
call olympic_schema.add_participant(10, 'vara', 's', 'pinn', 'INA', '01-01-2000', 'M');
call olympic_schema.remove_participant(11);
call olympic_schema.add_team_member(1, 2);
call olympic_schema.register_team('XXIV',3, 7, 'INA', 'g');
