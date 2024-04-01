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


