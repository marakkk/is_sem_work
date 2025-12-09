CREATE TABLE dream_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE admin (
    id SERIAL PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    users_id INT NOT NULL,
    FOREIGN KEY (users_id) REFERENCES dream_user(id) ON DELETE CASCADE
);

CREATE TABLE architect (
    id SERIAL PRIMARY KEY,
    users_id INT NOT NULL,
    price INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    FOREIGN KEY (users_id) REFERENCES dream_user(id) ON DELETE CASCADE
);

CREATE TABLE characters (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    characteristics TEXT,
    appearance TEXT,
    relation VARCHAR(30),
    occupation VARCHAR(30)
);

CREATE TABLE dream (
    id SERIAL PRIMARY KEY,
    name VARCHAR(500) NOT NULL,
    time_era VARCHAR(50) NOT NULL,
    virtual_environment VARCHAR(50) NOT NULL,
    special_powers VARCHAR(50),
    physical_rules VARCHAR(50),
    role VARCHAR(50) NOT NULL,
    scenario TEXT,
    template BOOLEAN,
    genre VARCHAR(50) NOT NULL,
    price INT,
    architect_id INT,
    creator_id INT,
    FOREIGN KEY (architect_id) REFERENCES architect(id) ON DELETE SET NULL,
    FOREIGN KEY (creator_id) REFERENCES dream_user(id) ON DELETE SET NULL
);

CREATE TABLE dream_characters (
    dream_id INT,
    characters_id INT,
    FOREIGN KEY (dream_id) REFERENCES dream(id) ON DELETE CASCADE,
    FOREIGN KEY (characters_id) REFERENCES characters(id) ON DELETE CASCADE
);

CREATE TABLE calendar (
    id SERIAL PRIMARY KEY,
    time TIME NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) NOT NULL
);

CREATE TABLE reservation (
    id SERIAL PRIMARY KEY,
    time_of_reservation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    dream_id INT NOT NULL,
    users_id INT NOT NULL,
    calendar_id INT NOT NULL,
    architect_id INT NOT NULL,
    collective_partner_id INT,
    FOREIGN KEY (dream_id) REFERENCES dream(id) ON DELETE CASCADE,
    FOREIGN KEY (users_id) REFERENCES dream_user(id) ON DELETE CASCADE,
    FOREIGN KEY (calendar_id) REFERENCES calendar(id) ON DELETE CASCADE,
    FOREIGN KEY (architect_id) REFERENCES architect(id) ON DELETE CASCADE,
    FOREIGN KEY (collective_partner_id) REFERENCES dream_user(id) ON DELETE SET NULL
);

CREATE TABLE users_dreams (
    users_dreams_id SERIAL PRIMARY KEY,
    users_id INT NOT NULL,
    dream_id INT NOT NULL,
    UNIQUE (users_id, dream_id),
    FOREIGN KEY (users_id) REFERENCES dream_user(id) ON DELETE CASCADE,
    FOREIGN KEY (dream_id) REFERENCES dream(id) ON DELETE CASCADE
);

CREATE TABLE review (
    id SERIAL PRIMARY KEY,
    mark INT CHECK (mark BETWEEN 1 AND 5),
    users_dreams_id INT,
    architect_id INT NOT NULL,
    FOREIGN KEY (users_dreams_id) REFERENCES users_dreams(users_dreams_id) ON DELETE CASCADE,
    FOREIGN KEY (architect_id) REFERENCES architect(id) ON DELETE CASCADE
);

