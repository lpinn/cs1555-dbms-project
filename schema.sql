DROP SCHEMA IF EXISTS olympic_schema CASCADE;
CREATE SCHEMA olympic_schema;
SET SCHEMA 'olympic_schema';

/* DOMAINS */
CREATE DOMAIN olympic_schema.role_check AS VARCHAR(12)
    CHECK (VALUE IN ('Organizer', 'Participant', 'Guest'));

CREATE DOMAIN olympic_schema.team_gender_check AS VARCHAR(1)
    CHECK (VALUE IN ('M', 'F', 'X'));

CREATE DOMAIN olympic_schema.participant_gender_check AS VARCHAR(1)
    CHECK (VALUE IN ('M', 'F'));

CREATE DOMAIN olympic_schema.medal_type_check AS VARCHAR(6)
    CHECK (VALUE IN ('Gold', 'Silver', 'Bronze'));

/* For all tables - we're assuming null for empty values in event of empty values */

/* COUNTRY */
-- Structural Constraints - specifying PRIMARY KEYS, NOT NULL, UNIQUE CONSTRAINT
CREATE TABLE olympic_schema.COUNTRY
(
    country_code CHAR(3),
    country_name VARCHAR(30) NOT NULL,
    CONSTRAINT COUNTRY_PK
        PRIMARY KEY (country_code),
    CONSTRAINT COUNTRY_UN
        UNIQUE (country_name)
);

/* OLYMPIAD */
/*
We're not requiring that Website be specified, it can be null
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL
- Semantic Constraints - checking if website is a valid website in case it's NOT NULL
- Referential Triggering Constraints - specifying FOREIGN KEY
    - We're assuming that a country from the COUNTRY table will
      not be deleted
*/
CREATE TABLE olympic_schema.OLYMPIAD
(
    olympiad_num VARCHAR(30),
    city VARCHAR(30) NOT NULL,
    country VARCHAR(30) NOT NULL,
    opening_date TIMESTAMP NOT NULL,
    closing_date TIMESTAMP NOT NULL,
    website VARCHAR(30),
    CONSTRAINT OLYMPIAD_PK
        PRIMARY KEY (olympiad_num),
    CONSTRAINT OLYMPIAD_COUNTRY_FK
        FOREIGN KEY (country) REFERENCES olympic_schema.COUNTRY(country_code)
);

/* SPORT */
/*
We're not requiring that a Description be specified, it can be null
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL
- Semantic Constraints - checking if team size is greater than 0
*/
CREATE TABLE olympic_schema.SPORT
(
    sport_id INTEGER,
    sport_name VARCHAR(30) NOT NULL,
    description VARCHAR(30),
    team_size INTEGER NOT NULL,
    date_added TIMESTAMP NOT NULL,
    CONSTRAINT SPORT_PK
        PRIMARY KEY (sport_id),
    CONSTRAINT VALID_SIZE
        CHECK (team_size > 0)
);

/* ACCOUNT */
/*
We're setting last_login in the create_account operation
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL, UNIQUE CONSTRAINT
- Semantic Constraints - calling domain check on role (role_check)
*/
CREATE TABLE olympic_schema.ACCOUNT
(
    account_id SERIAL,
    username VARCHAR(30) NOT NULL,
    passkey VARCHAR(30) NOT NULL,
    role role_check NOT NULL,
    last_login TIMESTAMP,
    CONSTRAINT ACCOUNT_PK
        PRIMARY KEY (account_id),
    CONSTRAINT USERNAME_UN
        UNIQUE (username)
);

/* PARTICIPANT */
/*
We're not requiring that a Middle Initial be specified, it can be null
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL
- Semantic Constraints - calling domain check on gender (participant_gender_check)
- Referential Triggering Constraints - specifying FOREIGN KEY for birth_country and account
    - We're assuming that a country from the COUNTRY table will not be deleted,
    - so that's why we're only handling when an account from ACCOUNT is deleted using SET NULL
*/
CREATE TABLE olympic_schema.PARTICIPANT
(
    participant_id SERIAL,
    account INTEGER NOT NULL,
    first VARCHAR(30) NOT NULL,
    middle VARCHAR(30),
    last VARCHAR(30) NOT NULL,
    birth_country CHAR(3) NOT NULL,
    dob TIMESTAMP NOT NULL,
    gender participant_gender_check,
    CONSTRAINT PARTICIPANT_PK
        PRIMARY KEY (participant_id),
    CONSTRAINT PARTICIPANT_COUNTRY_FK
        FOREIGN KEY (birth_country) REFERENCES olympic_schema.COUNTRY(country_code),
    CONSTRAINT PARTICIPANT_ACCOUNT_FK
        FOREIGN KEY (account) REFERENCES olympic_schema.ACCOUNT(account_id) ON DELETE SET NULL,
);

/* TEAM */
/*
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL, setting DEFAULT
- Semantic Constraints - calling domain check on gender (team_gender_check)
- Referential Triggering Constraints - specifying FOREIGN KEY for coach, olympiad, sport, and country
    - We're assuming that a country from the COUNTRY table will not be deleted
    - When a coach (participant) is deleted from PARTICIPANT then we delete cascade
    - We're assuming that a sport will not be deleted
    - We're assuming that an olympiad will not be deleted
*/
CREATE TABLE olympic_schema.TEAM
(
    team_id SERIAL,
    olympiad VARCHAR(30) NOT NULL,
    sport INTEGER NOT NULL,
    coach INTEGER NOT NULL,
    country CHAR(3) NOT NULL,
    gender team_gender_check,
    eligible BOOLEAN DEFAULT true,
    CONSTRAINT TEAM_PK
        PRIMARY KEY (team_id),
    CONSTRAINT TEAM_PART_FK
        FOREIGN KEY (coach) REFERENCES olympic_schema.PARTICIPANT(participant_id) ON DELETE CASCADE,
    CONSTRAINT TEAM_OLYMPIAD_FK
        FOREIGN KEY (olympiad) REFERENCES olympic_schema.OLYMPIAD(olympiad_num),
    CONSTRAINT TEAM_SPORT_FK
        FOREIGN KEY (sport) REFERENCES olympic_schema.SPORT(sport_id),
    CONSTRAINT TEAM_COUNTRY_FK
        FOREIGN KEY (country) REFERENCES olympic_schema.COUNTRY(country_code)
);

/* TEAM MEMBER */
/*
- Structural Constraints - specifying PRIMARY KEYS
- Referential Triggering Constraints - specifying FOREIGN KEY for team, participant
    - When a team is deleted from TEAM we delete cascade
    - When a participant is deleted from PARTICIPANT then we delete cascade
*/
CREATE TABLE olympic_schema.TEAM_MEMBERS
(
    team INTEGER,
    participant INTEGER,
    CONSTRAINT TEAMMEM_PK
        PRIMARY KEY (team, participant),
    CONSTRAINT TM_PARTICIPANT_FK
        FOREIGN KEY (participant) REFERENCES olympic_schema.PARTICIPANT(participant_id) ON DELETE CASCADE,
    CONSTRAINT TM_TEAM_FK
        FOREIGN KEY (team) REFERENCES olympic_schema.TEAM(team_id) ON DELETE CASCADE
);

/* VENUE */
/*
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL
- Semantic Constraints - checking if team size is greater than 0
*/
CREATE TABLE olympic_schema.VENUE
(
    venue_name VARCHAR(30),
    capacity INTEGER NOT NULL,
    CONSTRAINT VENUE_PK
        PRIMARY KEY (venue_name),
    CONSTRAINT VALID_CAPACITY
        CHECK (capacity > 0)
);

/* EVENT */
/*
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL
- Semantic Constraints - calling domain check on gender (team_gender_check)
- Referential Triggering Constraints - specifying FOREIGN KEY for venu, olympiad, sport
    - We're assuming that a country from the COUNTRY table will not be deleted
    - When an olympiad is deleted from OLYMPIAD, we don't do anything
    - When a venue is deleted from VENUE, we delete the venue and also CASCADE the delete 
    - When a sport is deleted from SPORT, we don't do anything
*/
CREATE TABLE olympic_schema.EVENT
(
    event_id INTEGER,
    venue VARCHAR(30) NOT NULL,
    olympiad VARCHAR(30) NOT NULL,
    sport INTEGER NOT NULL,
    gender team_gender_check,
    date TIMESTAMP NOT NULL,
    CONSTRAINT EVENT_PK
        PRIMARY KEY (event_id),
    CONSTRAINT EVENT_VENUE_FK
        FOREIGN KEY (venue) REFERENCES olympic_schema.VENUE(venue_name) ON DELETE CASCADE,
    CONSTRAINT EVENT_OLYMPIAD_FK
        FOREIGN KEY (olympiad) REFERENCES olympic_schema.OLYMPIAD(olympiad_num),
    CONSTRAINT EVENT_SPORT_FK
        FOREIGN KEY (sport) REFERENCES olympic_schema.SPORT(sport_id)
);

/* MEDAL */
/*
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL
*/
CREATE TABLE olympic_schema.MEDAL
(
    medal_id SERIAL NOT NULL,
    type medal_type_check,
    points INTEGER NOT NULL,
    CONSTRAINT MEDAL_PK
        PRIMARY KEY (type)
);

/* PLACEMENT */
/*
- Structural Constraints - specifying PRIMARY KEYS, NOT NULL
*/
CREATE TABLE olympic_schema.PLACEMENT
(
    event INTEGER,
    team INTEGER,
    medal medal_type_check,
    position INTEGER,
    CONSTRAINT PLACEMENT_PK
        PRIMARY KEY (event, team),
    CONSTRAINT PLACEMENT_EVENT_FK
        FOREIGN KEY (event) REFERENCES olympic_schema.EVENT(event_id),
    CONSTRAINT PLACEMENT_TEAM_FK
        FOREIGN KEY (team) REFERENCES olympic_schema.TEAM(team_id) ON DELETE CASCADE,
    CONSTRAINT PLACEMENT_MEDAL_FK
        FOREIGN KEY (medal) REFERENCES olympic_schema.MEDAL(type)
);
