-- 1. -- Общий скрипт для создания базы данных и таблиц

CREATE DATABASE dream_database;

\c dream_database

-- Создание таблицы пользователей
CREATE TABLE dream_user (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) CHECK (role IN ('customer', 'architect', 'admin')) NOT NULL
);

-- Таблица администраторов, ссылается на user
CREATE TABLE admin (
    admin_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    status VARCHAR(10) CHECK (status IN ('requested', 'approved', 'denied')) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES dream_user(user_id) ON DELETE CASCADE
);

-- Таблица архитекторов, ссылается на user
CREATE TABLE architect (
    architect_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    price INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    FOREIGN KEY (user_id) REFERENCES dream_user(user_id) ON DELETE CASCADE
);

-- Таблица снов, ссылается на architect
CREATE TABLE dream (
    dream_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    time_era VARCHAR(20) CHECK (time_era IN ('medieval', 'renaissance', 'industrial', 'victorian', 'modern')) NOT NULL,
    virtual_environment VARCHAR(20) CHECK (virtual_environment IN ('forest', 'space', 'underwater', 'fantasy')) NOT NULL,
    special_powers VARCHAR(20) CHECK (special_powers IN ('flight', 'teleportation', 'time manipulation')),
    physical_rules VARCHAR(20) CHECK (physical_rules IN ('gravity', 'speed_change')),
    role VARCHAR(10) CHECK (role IN ('hero', 'observer')) NOT NULL,
    genre VARCHAR(20) CHECK (genre IN ('adventure', 'horror', 'drama', 'fantasy')) NOT NULL,
    scenario TEXT,
    template BOOLEAN,
    architect_id INT,
    price INT,
    FOREIGN KEY (architect_id) REFERENCES architect(architect_id) ON DELETE SET NULL
);

-- Таблица персонажей
CREATE TABLE characters (
    characters_id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    characteristics TEXT,
    appearance TEXT,
    relation_to_user VARCHAR(10) CHECK (relation_to_user IN ('mother', 'father', 'friend', 'enemy')),
    occupation VARCHAR(20) CHECK (occupation IN ('teacher', 'doctor', 'actor', 'programmer'))
);

-- Таблица для связи персонажей и снов
CREATE TABLE dream_characters (
    characters_id INT,
    dream_id INT,
    FOREIGN KEY (characters_id) REFERENCES characters(characters_id) ON DELETE CASCADE,
    FOREIGN KEY (dream_id) REFERENCES dream(dream_id) ON DELETE CASCADE
);

-- Таблица обзоров
CREATE TABLE review (
    review_id SERIAL PRIMARY KEY,
    mark INT CHECK (mark BETWEEN 1 AND 5),
    architect_id INT NOT NULL,
    user_id INT NOT NULL,
    dream_id INT NOT NULL,
    FOREIGN KEY (architect_id) REFERENCES architect(architect_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES dream_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (dream_id) REFERENCES dream(dream_id) ON DELETE CASCADE
);

-- Таблица календаря
CREATE TABLE calendar (
    calendar_id SERIAL PRIMARY KEY,
    time TIME NOT NULL,
    date DATE NOT NULL,
    status VARCHAR(20) CHECK (status IN ('available', 'not available')) NOT NULL
);

-- Таблица бронирования
CREATE TABLE reservation (
    reservation_id SERIAL PRIMARY KEY,
    dream_id INT NOT NULL,
    user_id INT NOT NULL,
    calendar_id INT NOT NULL,
    collective BOOLEAN,
    collective_partner_id INT,
    time_of_reservation TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) CHECK (status IN ('confirmed', 'cancelled', 'denied', 'done')) NOT NULL,
    FOREIGN KEY (dream_id) REFERENCES dream(dream_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES dream_user(user_id) ON DELETE CASCADE,
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
DROP TABLE IF EXISTS dream_user CASCADE;

-- Удаление базы данных
DROP DATABASE IF EXISTS dream_db;



-- 3. Заполнение базы данных тестовыми данными

-- Добавление пользователей
INSERT INTO dream_user (username, password, role) VALUES 
    ('john_doe', 'password123', 'customer'),
    ('jane_architect', 'password456', 'architect'),
    ('admin_user', 'adminpass', 'admin');


-- Добавление администраторов
INSERT INTO admin (user_id, status) VALUES 
    (3, 'approved');


-- Добавление архитекторов
INSERT INTO architect (user_id, price, rating) VALUES 
    (2, 1000, 4);


-- Добавление снов
INSERT INTO dream (name, time_era, virtual_environment, special_powers, physical_rules, role, genre, scenario, template, architect_id, price) VALUES
    ('Ancient Forest Adventure', 'medieval', 'forest', 'flight', 'gravity', 'hero', 'adventure', 'Explore the enchanted forest...', TRUE, 1, 500),
    ('Space Odyssey', 'modern', 'space', 'teleportation', 'speed_change', 'observer', 'fantasy', 'Journey through the cosmos...', FALSE, 1, 750);


-- Добавление персонажей
INSERT INTO characters (name, characteristics, appearance, relation_to_user, occupation) VALUES
    ('Elven Archer', 'Skilled with a bow', 'Tall and graceful', 'friend', 'actor'), 
    ('Space Pilot', 'Brave and skilled', 'Dressed in a spacesuit', 'enemy', 'doctor');


-- Связывание персонажей и снов
INSERT INTO dream_characters (characters_id, dream_id) VALUES
    (1, 1),
    (2, 2);


-- Добавление календарных записей
INSERT INTO calendar (time, date, status) VALUES 
    ('14:00', '2023-10-15', 'available'),
    ('16:00', '2023-10-15', 'available');


-- Добавление бронирования
INSERT INTO reservation (dream_id, user_id, calendar_id, collective, time_of_reservation, status) VALUES 
    (1, 1, 1, FALSE, CURRENT_TIMESTAMP, 'confirmed');


-- Добавление отзывов
INSERT INTO review (mark, architect_id, user_id, dream_id) VALUES
    (5, 1, 1, 1);



-- Триггеры -- 

-- Функция для добавления записи в таблицу admin или architect в зависимости от роли пользователя
CREATE OR REPLACE FUNCTION add_role_record()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.role = 'admin' THEN
        INSERT INTO admin (user_id, status) VALUES (NEW.user_id, 'requested');
    ELSIF NEW.role = 'architect' THEN
        INSERT INTO architect (user_id, price, rating) VALUES (NEW.user_id, 500, 3);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



-- Триггер для вызова функции add_role_record при добавлении пользователя
CREATE TRIGGER user_role_trigger
AFTER INSERT ON "user"
FOR EACH ROW
EXECUTE FUNCTION add_role_record();



-- Триггер для установки времени бронирования
CREATE OR REPLACE FUNCTION set_reservation_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.time_of_reservation = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER reservation_timestamp_trigger
BEFORE INSERT ON reservation
FOR EACH ROW
EXECUTE FUNCTION set_reservation_timestamp();



-- Триггер для проверки статуса администратора
CREATE OR REPLACE FUNCTION check_admin_status()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.status NOT IN ('requested', 'approved', 'denied') THEN
        RAISE EXCEPTION 'Invalid status for admin';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER admin_status_trigger
BEFORE INSERT OR UPDATE ON admin
FOR EACH ROW
EXECUTE FUNCTION check_admin_status();



-- Триггер для проверки рейтинга архитектора
CREATE OR REPLACE FUNCTION check_architect_rating()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.rating < 1 OR NEW.rating > 5 THEN
        RAISE EXCEPTION 'Rating must be between 1 and 5';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER architect_rating_trigger
BEFORE INSERT OR UPDATE ON architect
FOR EACH ROW
EXECUTE FUNCTION check_architect_rating();



-- Триггер для проверки оценки в таблице обзоров
CREATE OR REPLACE FUNCTION check_review_mark()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.mark < 1 OR NEW.mark > 5 THEN
        RAISE EXCEPTION 'Mark must be between 1 and 5';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER review_mark_trigger
BEFORE INSERT OR UPDATE ON review
FOR EACH ROW
EXECUTE FUNCTION check_review_mark();




-- 4. Критически важные запросы


-- Функция для получения информации о снах архитектора
CREATE OR REPLACE FUNCTION get_architect_dreams(architect_user_id INT)
RETURNS TABLE(dream_id INT, name VARCHAR, time_era VARCHAR, virtual_environment VARCHAR) AS $$
BEGIN
    RETURN QUERY
    SELECT d.dream_id, d.name, d.time_era, d.virtual_environment
    FROM dream d
    JOIN architect a ON d.architect_id = a.architect_id
    JOIN dream_user u ON a.user_id = u.user_id
    WHERE u.user_id = architect_user_id;
END;
$$ LANGUAGE plpgsql;


-- Функция для обновления статуса бронирования
CREATE OR REPLACE FUNCTION update_reservation_status(reservation_id INT, new_status VARCHAR)
RETURNS VOID AS $$
BEGIN
    UPDATE reservation
    SET status = new_status
    WHERE reservation_id = reservation_id;
END;
$$ LANGUAGE plpgsql;


-- Процедура для создания нового пользователя
CREATE OR REPLACE PROCEDURE create_user(
    p_username VARCHAR,
    p_password VARCHAR,
    p_role VARCHAR,
    p_price INT DEFAULT 500,
    p_rating INT DEFAULT 3
) AS $$
DECLARE
    new_user_id INT;
BEGIN
    INSERT INTO dream_user (username, password, role) VALUES (p_username, p_password, p_role)
    RETURNING user_id INTO new_user_id;

    IF p_role = 'admin' THEN
        INSERT INTO admin (user_id, status) VALUES (new_user_id, 'requested');
    ELSIF p_role = 'architect' THEN
        INSERT INTO architect (user_id, price, rating) VALUES (new_user_id, p_price, p_rating);
    END IF;
END;
$$ LANGUAGE plpgsql;


-- Функция для получения отзывов об архитекторе
CREATE OR REPLACE FUNCTION get_architect_reviews(architect_id INT)
RETURNS TABLE(review_id INT, mark INT, user_id INT, dream_id INT) AS $$
BEGIN
    RETURN QUERY
    SELECT r.review_id, r.mark, r.user_id, r.dream_id
    FROM review r
    WHERE r.architect_id = architect_id;
END;
$$ LANGUAGE plpgsql;


-- Функция для получения всех бронирований пользователя
CREATE OR REPLACE FUNCTION get_user_reservations(user_id INT)
RETURNS TABLE(reservation_id INT, dream_id INT, calendar_id INT, status VARCHAR) AS $$
BEGIN
    RETURN QUERY
    SELECT r.reservation_id, r.dream_id, r.calendar_id, r.status
    FROM reservation r
    WHERE r.user_id = user_id;
END;
$$ LANGUAGE plpgsql;



-- Функция для проверки наличия свободного времени в календаре
CREATE OR REPLACE FUNCTION check_availability(calendar_id INT, reservation_time TIME, reservation_date DATE)
RETURNS BOOLEAN AS $$
DECLARE
    available_count INT;
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


-- Бронирование сна
CREATE INDEX idx_reservation_calendar ON reservation(calendar_id);
CREATE INDEX idx_reservation_status ON reservation(status);


-- Управление календарем бронирований
CREATE INDEX idx_calendar_date_time ON calendar(date, time);


-- Создание шаблонов сценариев снов
CREATE INDEX idx_dream_architect ON dream(architect_id);
CREATE INDEX idx_dream_template ON dream(template);
