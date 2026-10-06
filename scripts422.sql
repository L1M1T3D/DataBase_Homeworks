CREATE TABLE car (
                     id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                     brand VARCHAR(50) NOT NULL,
                     model VARCHAR(50) NOT NULL,
                     price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE person (
                        id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        name VARCHAR(100) NOT NULL,
                        age INTEGER NOT NULL,
                        has_license BOOLEAN NOT NULL,
                        car_id BIGINT REFERENCES car(id)
);