-- liquibase formatted sql

-- changeset sapphirov:1
CREATE INDEX idx_student_name ON student (name);