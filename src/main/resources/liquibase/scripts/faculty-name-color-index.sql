-- liquibase formatted sql

-- changeset sapphirov:2
CREATE INDEX idx_faculty_name_color ON faculty (name, color);