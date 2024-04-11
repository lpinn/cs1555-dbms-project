/* testing */
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
    CONSTRAINT LEN
        CHECK (length(website) <= 30)
);

CREATE TABLE olympic_schema.SPORT
(
    sport_id INTEGER,
    sport_name VARCHAR(30) NOT NULL,
    description VARCHAR(30),
    team_size INTEGER NOT NULL,
    date_added TIMESTAMP,
    CONSTRAINT SPORT_PK
        PRIMARY KEY (sport_id)
    CONSTRAINT VALID_SIZE
        CHECK (team_size >= 0)
);

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

CREATE TABLE olympic_schema.PARTICIPANT
(
    participant_id SERIAL,
    account INTEGER,
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
        FOREIGN KEY (account) REFERENCES olympic_schema.ACCOUNT(account_id)
    CONSTRAINT LEN_FIRST_NAME
        CHECK (length(first) <= 30)
    CONSTRAINT LEN_LAST_NAME
        CHECK (length(last) <= 30)

);

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
        FOREIGN KEY (coach) REFERENCES olympic_schema.PARTICIPANT(participant_id),
    CONSTRAINT TEAM_OLYMPIAD_FK
        FOREIGN KEY (olympiad) REFERENCES olympic_schema.OLYMPIAD(olympiad_num),
    CONSTRAINT TEAM_SPORT_FK
        FOREIGN KEY (sport) REFERENCES olympic_schema.SPORT(sport_id),
    CONSTRAINT TEAM_COUNTRY_FK
        FOREIGN KEY (country) REFERENCES olympic_schema.COUNTRY(country_code)
);

CREATE TABLE olympic_schema.TEAM_MEMBERS
(
    team INTEGER,
    participant INTEGER,
    CONSTRAINT TEAMMEM_PK
        PRIMARY KEY (team, participant),
    CONSTRAINT TM_PARTICIPANT_FK
        FOREIGN KEY (participant) REFERENCES olympic_schema.PARTICIPANT(participant_id),
    CONSTRAINT TM_TEAM_FK
        FOREIGN KEY (team) REFERENCES olympic_schema.TEAM(team_id)
);

CREATE TABLE olympic_schema.VENUE
(
    venue_name VARCHAR(30),
    capacity INTEGER NOT NULL,
    CONSTRAINT VENUE_PK
        PRIMARY KEY (venue_name)
    CONSTRAINT VALID_CAPACITY
        CHECK (capacity >= 0)
);

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
        FOREIGN KEY (venue) REFERENCES olympic_schema.VENUE(venue_name),
    CONSTRAINT EVENT_OLYMPIAD_FK
        FOREIGN KEY (olympiad) REFERENCES olympic_schema.OLYMPIAD(olympiad_num),
    CONSTRAINT EVENT_SPORT_FK
        FOREIGN KEY (sport) REFERENCES olympic_schema.SPORT(sport_id)
);

CREATE TABLE olympic_schema.MEDAL
(
    medal_id INTEGER,
    type medal_type_check,
    points INTEGER,
    CONSTRAINT MEDAL_PK
        PRIMARY KEY (medal_id)
);

CREATE TABLE olympic_schema.PLACEMENT
(
    event INTEGER,
    team INTEGER,
    medal medal_type_check,
    position INTEGER,
    CONSTRAINT PLACEMENT_PK
        PRIMARY KEY (team),
    CONSTRAINT PLACEMENT_EVENT_FK
        FOREIGN KEY (event) REFERENCES olympic_schema.EVENT(event_id),
    CONSTRAINT PLACEMENT_TEAM_FK
        FOREIGN KEY (team) REFERENCES olympic_schema.TEAM(team_id)
);
