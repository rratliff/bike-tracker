-- Flyway Migration: Initial schema for Bike Tracker

-- User ID sequence
CREATE SEQUENCE IF NOT EXISTS t_user_user_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- Users table
CREATE TABLE IF NOT EXISTS t_user (
    user_id      BIGINT PRIMARY KEY DEFAULT nextval('t_user_user_id_seq'),
    oauth_subject VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_t_user_oauth_subject UNIQUE (oauth_subject)
);

-- Ride ID sequence
CREATE SEQUENCE IF NOT EXISTS t_ride_ride_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- Rides table
CREATE TABLE IF NOT EXISTS t_ride (
    ride_id       BIGINT PRIMARY KEY DEFAULT nextval('t_ride_ride_id_seq'),
    title         TEXT NOT NULL,
    miles         DOUBLE PRECISION NOT NULL,
    ride_date     TIMESTAMP,
    creation_date TIMESTAMP NOT NULL DEFAULT NOW(),
    user_id       BIGINT NOT NULL,
    CONSTRAINT fk_t_ride_user FOREIGN KEY (user_id)
        REFERENCES t_user (user_id)
);

CREATE INDEX IF NOT EXISTS idx_t_ride_user_id ON t_ride (user_id);
