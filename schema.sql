/* testing */

DROP SCHEMA IF EXISTS olympic_schema CASCADE;
CREATE SCHEMA olympic_schema;
SET SCHEMA 'olympic_schema';

--DROP DOMAIN IF EXISTS role_check CASCADE;
DROP TABLE IF EXISTS ACCOUNT CASCADE;
DROP TABLE IF EXISTS COUNTRY CASCADE;
DROP TABLE IF EXISTS EVENT CASCADE;
DROP TABLE IF EXISTS MEDAL CASCADE;
DROP TABLE IF EXISTS OLYMPIAD CASCADE;
DROP TABLE IF EXISTS PARTICIPANT CASCADE;
DROP TABLE IF EXISTS PLACEMENT CASCADE;
DROP TABLE IF EXISTS SPORT CASCADE;
DROP TABLE IF EXISTS TEAM CASCADE;
DROP TABLE IF EXISTS TEAM_MEMBERS CASCADE;
DROP TABLE IF EXISTS VENUE CASCADE;

CREATE DOMAIN role_check AS VARCHAR(12)
    CHECK (VALUE IN ('Organizer', 'Participant', 'Guest'));

CREATE DOMAIN team_gender_check AS VARCHAR(1)
    CHECK (VALUE IN ('M', 'F', 'X'));

CREATE DOMAIN participant_gender_check AS VARCHAR(1)
    CHECK (VALUE IN ('M', 'F'));

CREATE DOMAIN medal_type_check AS VARCHAR(6)
    CHECK (VALUE IN ('Gold', 'Silver', 'Bronze'));

CREATE TABLE olympic_schema.COUNTRY
(
    country_code CHAR(3),
    country_name VARCHAR(30) NOT NULL,
    CONSTRAINT COUNTRY_PK
        PRIMARY KEY (country_code),
    CONSTRAINT COUNTRY_UN
        UNIQUE (country_name)
);

CREATE TABLE OLYMPIAD
(
    olympiad_num VARCHAR(30),
    city VARCHAR(30),
    country VARCHAR(30),
    opening_date TIMESTAMP,
    closing_date TIMESTAMP,
    website VARCHAR(30),
    CONSTRAINT OLYMPIAD_PK
        PRIMARY KEY (olympiad_num),
    CONSTRAINT OLYMPIAD_COUNTRY_FK
        FOREIGN KEY (country) REFERENCES olympic_schema.COUNTRY(country_code),
    CONSTRAINT LEN
        CHECK (length(website) <= 30)

);

CREATE TABLE SPORT
(
    sport_id INTEGER,
    sport_name VARCHAR(30),
    description VARCHAR(30),
    team_size INTEGER,
    date_added TIMESTAMP,
    CONSTRAINT SPORT_PK
        PRIMARY KEY (sport_id)
);

CREATE TABLE ACCOUNT
(
    account_id SERIAL,
    username VARCHAR(30),
    passkey VARCHAR(30),
    role role_check,
    last_login TIMESTAMP,
    CONSTRAINT ACCOUNT_PK
        PRIMARY KEY (account_id)
);

CREATE TABLE TEAM
(
    team_id SERIAL,
    olympiad VARCHAR(30),
    sport INTEGER,
    coach INTEGER,
    country CHAR(3),
    gender team_gender_check,
    eligible BOOLEAN,
    CONSTRAINT TEAM_PK
        PRIMARY KEY (team_id),
    CONSTRAINT TEAM_OLYMPIAD_FK
        FOREIGN KEY (olympiad) REFERENCES OLYMPIAD(olympiad_num),
    CONSTRAINT TEAM_SPORT_FK
        FOREIGN KEY (sport) REFERENCES SPORT(sport_id),
    CONSTRAINT TEAM_COUNTRY_FK
        FOREIGN KEY (country) REFERENCES olympic_schema.COUNTRY(country_code)
);

CREATE TABLE PARTICIPANT
(
    participant_id SERIAL,
    account INTEGER,
    first VARCHAR(30),
    middle VARCHAR(30),
    last VARCHAR(30),
    birth_country CHAR(3),
    dob TIMESTAMP,
    gender participant_gender_check,
    CONSTRAINT PARTICIPANT_PK
        PRIMARY KEY (participant_id),
    CONSTRAINT PARTICIPANT_COUNTRY_FK
        FOREIGN KEY (birth_country) REFERENCES COUNTRY(country_code),
    CONSTRAINT PARTICIPANT_ACCOUNT_FK
        FOREIGN KEY (account) REFERENCES ACCOUNT(account_id)
);

CREATE TABLE TEAM_MEMBERS
(
    team INTEGER,
    participant INTEGER,
    CONSTRAINT TM_PARTICIPANT_FK
        FOREIGN KEY (participant) REFERENCES PARTICIPANT(participant_id),
    CONSTRAINT TM_TEAM_FK
        FOREIGN KEY (team) REFERENCES TEAM(team_id)
);

CREATE TABLE VENUE
(
    venue_name VARCHAR(30),
    capacity INTEGER,
    CONSTRAINT VENUE_PK
        PRIMARY KEY (venue_name)
);

CREATE TABLE EVENT
(
    event_id INTEGER,
    venue VARCHAR(30),
    olympiad VARCHAR(30),
    sport INTEGER,
    gender team_gender_check,
    date TIMESTAMP,
    CONSTRAINT EVENT_PK
        PRIMARY KEY (event_id),
    CONSTRAINT EVENT_VENUE_FK
        FOREIGN KEY (venue) REFERENCES VENUE(venue_name),
    CONSTRAINT EVENT_OLYMPIAD_FK
        FOREIGN KEY (olympiad) REFERENCES OLYMPIAD(olympiad_num),
    CONSTRAINT EVENT_SPORT_FK
        FOREIGN KEY (sport) REFERENCES SPORT(sport_id)
);

CREATE TABLE MEDAL
(
    medal_id SERIAL,
    type medal_type_check,
    points INTEGER,
    CONSTRAINT MEDAL_PK
        PRIMARY KEY (medal_id)
);

CREATE TABLE PLACEMENT
(
    event INTEGER,
    team INTEGER,
    medal medal_type_check,
    position INTEGER,
    CONSTRAINT PLACEMENT_PK
        PRIMARY KEY (event, team, position),
    CONSTRAINT PLACEMENT_EVENT_FK
        FOREIGN KEY (event) REFERENCES EVENT(event_id),
    CONSTRAINT PLACEMENT_TEAM_FK
        FOREIGN KEY (team) REFERENCES TEAM(team_id)
);