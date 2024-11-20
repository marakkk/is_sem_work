-- 1. -- Общий скрипт для создания базы данных и таблиц

CREATE DATABASE dream_database;

\c dream_database

-- Создание таблицы пользователей
CREATE TABLE users (
    users_id SERIAL PRIMARY KEY,
    username VARCHAR(65) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(15) CHECK (role IN ('customer', 'architect', 'admin')) NOT NULL
);

-- Таблица администраторов, ссылается на user
CREATE TABLE admin (
    admin_id SERIAL PRIMARY KEY,
    users_id BIGINT NOT NULL,
    status VARCHAR(15) CHECK (status IN ('requested', 'approved', 'denied')) NOT NULL,
    FOREIGN KEY (users_id) REFERENCES users(users_id) ON DELETE CASCADE
);

-- Таблица архитекторов, ссылается на user
CREATE TABLE architect (
    architect_id SERIAL PRIMARY KEY,
    users_id BIGINT NOT NULL,
    price BIGINT NOT NULL,
    rating BIGINT,
    FOREIGN KEY (users_id) REFERENCES users(users_id) ON DELETE CASCADE
);

-- Таблица снов, ссылается на architect
CREATE TABLE dream (
    dream_id SERIAL PRIMARY KEY,
    name VARCHAR(75) NOT NULL,
    time_era VARCHAR(20) CHECK (time_era IN ('medieval', 'renaissance', 'industrial', 'victorian', 'modern')) NOT NULL,
    virtual_environment VARCHAR(31) CHECK (virtual_environment IN ('forest', 'space', 'underwater', 'fantasy')) NOT NULL,
    special_powers VARCHAR(31) CHECK (special_powers IN ('flight', 'teleportation', 'time manipulation')),
    physical_rules VARCHAR(31) CHECK (physical_rules IN ('gravity', 'speed_change')),
    role VARCHAR(15) CHECK (role IN ('hero', 'observer')) NOT NULL,
    genre VARCHAR(31) CHECK (genre IN ('adventure', 'horror', 'drama', 'fantasy')) NOT NULL,
    scenario TEXT NOT NULL,
    template BOOLEAN NOT NULL,
    architect_id BIGINT NOT NULL,
    price BIGINT,
    FOREIGN KEY (architect_id) REFERENCES architect(architect_id) ON DELETE SET NULL
);

-- Создание таблицы user_dreams
CREATE TABLE user_dreams (
    user_dreams_id SERIAL PRIMARY KEY,
    users_id BIGINT NOT NULL,
    dream_id BIGINT NOT NULL,
    FOREIGN KEY (users_id) REFERENCES users(users_id) ON DELETE CASCADE,
    FOREIGN KEY (dream_id) REFERENCES dream(dream_id) ON DELETE CASCADE
);


-- Таблица персонажей
CREATE TABLE characters (
    characters_id SERIAL PRIMARY KEY,
    name VARCHAR(75) NOT NULL,
    characteristics TEXT NOT NULL,
    appearance TEXT NOT NULL,
    relation_to_user VARCHAR(15) CHECK (relation_to_user IN ('mother', 'father', 'friend', 'enemy')),
    occupation VARCHAR(31) CHECK (occupation IN ('teacher', 'doctor', 'actor', 'programmer'))
);

-- Таблица для связи персонажей и снов
CREATE TABLE dream_characters (
    dream_characters_id BIGINT NOT NULL,
    characters_id BIGINT NOT NULL,
    dream_id BIGINT NOT NULL,
    FOREIGN KEY (characters_id) REFERENCES characters(characters_id) ON DELETE CASCADE,
    FOREIGN KEY (dream_id) REFERENCES dream(dream_id) ON DELETE CASCADE
);

-- Таблица обзоров
CREATE TABLE review (
    review_id SERIAL PRIMARY KEY,
    mark BIGINT CHECK (mark BETWEEN 1 AND 5),
    architect_id BIGINT NOT NULL,
    users_id BIGINT NOT NULL,
    dream_id BIGINT NOT NULL,
    FOREIGN KEY (architect_id) REFERENCES architect(architect_id) ON DELETE CASCADE,
    FOREIGN KEY (users_id) REFERENCES users(users_id) ON DELETE CASCADE,
    FOREIGN KEY (dream_id) REFERENCES dream(dream_id) ON DELETE CASCADE
);

-- Таблица календаря
CREATE TABLE calendar (
    calendar_id SERIAL PRIMARY KEY,
    time TIME NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(31) CHECK (status IN ('available', 'not available')) NOT NULL
);


-- Таблица бронирования
CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    dream_id BIGINT NOT NULL,
    users_id BIGINT NOT NULL,
    calendar_id BIGINT NOT NULL,
    collective_partner_id BIGINT,
    time_of_reservation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(31) CHECK (status IN ('confirmed', 'cancelled', 'denied', 'done')) NOT NULL,
    FOREIGN KEY (dream_id) REFERENCES dream(dream_id) ON DELETE CASCADE,
    FOREIGN KEY (users_id) REFERENCES users(users_id) ON DELETE CASCADE,
    FOREIGN KEY (calendar_id) REFERENCES calendar(calendar_id) ON DELETE CASCADE
);


-- 2. Скрипт для удаления таблиц и базы данных

-- Удаление таблиц
DROP TABLE IF EXISTS reservation CASCADE;
DROP TABLE IF EXISTS calendar CASCADE;
DROP TABLE IF EXISTS review CASCADE;
DROP TABLE IF EXISTS dream_characters CASCADE;
DROP TABLE IF EXISTS characters CASCADE;
DROP TABLE IF EXISTS dream CASCADE;
DROP TABLE IF EXISTS architect CASCADE;
DROP TABLE IF EXISTS admin CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Удаление базы данных
DROP DATABASE IF EXISTS dream_db;


-- 3. Заполнение базы данных тестовыми данными

-- Добавление пользователей
INSERT INTO users (username, password, role) VALUES 
    ('john_doe', 'password123', 'customer'),
    ('jane_architect', 'password456', 'architect'),
    ('admin_user', 'adminpass', 'admin');


-- Добавление администраторов
INSERT INTO admin (users_id, status) VALUES 
    (3, 'approved');


-- Добавление архитекторов
INSERT INTO architect (users_id, price, rating) VALUES 
    (2, 1000, 4);


-- Добавление снов
INSERT INTO dream (name, time_era, virtual_environment, special_powers, physical_rules, role, genre, scenario, template, architect_id, price) VALUES
    ('Ancient Forest Adventure', 'medieval', 'forest', 'flight', 'gravity', 'hero', 'adventure', 'Explore the enchanted forest...', TRUE, 1, 500),
    ('Space Odyssey', 'modern', 'space', 'teleportation', 'speed_change', 'observer', 'fantasy', 'Journey through the cosmos...', FALSE, 1, 750);


-- Вставка тестовых данных в таблицу user_dreams
INSERT INTO user_dreams (users_id, dream_id) VALUES
    (1, 1),
    (1, 2),
    (2, 1);


-- Добавление персонажей
INSERT INTO characters (name, characteristics, appearance, relation_to_user, occupation) VALUES
    ('Elven Archer', 'Skilled with a bow', 'Tall and graceful', 'friend', 'actor'), 
    ('Space Pilot', 'Brave and skilled', 'Dressed in a spacesuit', 'enemy', 'doctor');



-- Связывание персонажей и снов
INSERT INTO dream_characters (dream_characters_id, characters_id, dream_id) VALUES
    (1, 1, 1),
    (2, 2, 2);


-- Добавление календарных записей
INSERT INTO calendar (time, date, status) VALUES 
    ('14:00', '2023-10-15', 'available'),
    ('16:00', '2023-10-15', 'available');


-- Добавление бронирования
INSERT INTO reservation (dream_id, users_id, calendar_id, time_of_reservation, status) VALUES 
    (1, 1, 1, CURRENT_TIMESTAMP, 'confirmed');


-- Добавление отзывов
INSERT INTO review (mark, architect_id, users_id, dream_id) VALUES
    (5, 1, 1, 1);


-- Триггеры -- 

-- Функция для добавления записи в таблицу admin или architect в зависимости от роли пользователя
CREATE OR REPLACE FUNCTION add_role_record()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.role = 'admin' THEN
        INSERT INTO admin (users_id, status) VALUES (NEW.users_id, 'requested');
    ELSIF NEW.role = 'architect' THEN
        INSERT INTO architect (users_id, price, rating) VALUES (NEW.users_id, 500, 1);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


-- 4. Критически важные запросы

-- Функция для получения информации о снах архитектора
CREATE OR REPLACE FUNCTION get_architect_dreams(architect_users_id BIGINT)
RETURNS TABLE(dream_id BIGINT, name VARCHAR, time_era VARCHAR, virtual_environment VARCHAR) AS $$
BEGIN
    RETURN QUERY
    SELECT d.dream_id, d.name, d.time_era, d.virtual_environment
    FROM dream d
    JOIN architect a ON d.architect_id = a.architect_id
    JOIN users u ON a.users_id = u.users_id
    WHERE u.users_id = architect_users_id;
END;
$$ LANGUAGE plpgsql;


-- Функция для обновления статуса бронирования
CREATE OR REPLACE FUNCTION update_reservation_status(reservation_id BIGINT, new_status VARCHAR)
RETURNS VOID AS $$
BEGIN
    UPDATE reservation
    SET status = new_status
    WHERE reservation_id = reservation_id;
END;
$$ LANGUAGE plpgsql;


-- Функция для получения отзывов об архитекторе
CREATE OR REPLACE FUNCTION get_architect_reviews(architect_id BIGINT)
RETURNS TABLE(review_id BIGINT, mark BIGINT, users_id BIGINT, dream_id BIGINT) AS $$
BEGIN
    RETURN QUERY
    SELECT r.review_id, r.mark, r.users_id, r.dream_id
    FROM review r
    WHERE r.architect_id = architect_id;
END;
$$ LANGUAGE plpgsql;


-- Функция для получения всех бронирований пользователя
CREATE OR REPLACE FUNCTION get_user_reservations(users_id BIGINT)
RETURNS TABLE(reservation_id BIGINT, dream_id BIGINT, calendar_id BIGINT, status VARCHAR) AS $$
BEGIN
    RETURN QUERY
    SELECT r.reservation_id, r.dream_id, r.calendar_id, r.status
    FROM reservation r
    WHERE r.users_id = users_id;
END;
$$ LANGUAGE plpgsql;


-- Функция для проверки наличия свободного времени в календаре
CREATE OR REPLACE FUNCTION check_availability(calendar_id BIGINT, reservation_time TIME, reservation_date DATE)
RETURNS BOOLEAN AS $$
DECLARE
    available_count BIGINT;
BEGIN
    SELECT COUNT(*) INTO available_count
    FROM reservation
    WHERE calendar_id = calendar_id
    AND date = reservation_date
    AND time = reservation_time
    AND status = 'confirmed';  -- Можно изменить в зависимости от логики

    RETURN available_count = 0;
END;
$$ LANGUAGE plpgsql;


-- 5. Индексы

-- Управление календарем бронирований
CREATE INDEX idx_calendar_date_time ON calendar(date, time);
