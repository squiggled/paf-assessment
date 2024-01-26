-- Write your Task 1 answers in this file
-- Write your Task 1 answers in this file
drop database if EXISTS bedandbreakfast;
create database bedandbreakfast;
use bedandbreakfast;

CREATE table users (
    email VARCHAR(128) NOT NULL,
    name VARCHAR(128) NOT NULL,

    PRIMARY KEY(email)
);
create table reviews(
    id INT auto_increment,
    date TIMESTAMP,
    listing_id VARCHAR(20) NOT NULL,
    reviewer_name VARCHAR(64) NOT NULL,
    comments TEXT,
    PRIMARY KEY(id)
);

CREATE table bookings(
    booking_id CHAR(8) NOT NULL,
    listing_id VARCHAR(20) NOT NULL,
    duration INT NOT NULL,
    email VARCHAR(128) NOT NULL,
    
    primary key(booking_id),
    -- FOREIGN KEY (listing_id) REFERENCES reviews(listing_id),
    FOREIGN KEY (email) REFERENCES users(email)
);

insert into users(email, name) values
("fred@gmail.com", "Fred Flintstone"),
("barney@gmail.com", "Barney Rubble"),
("fry@planetexpress.com", "Philip J Fry"),
("hlmer@gmail.com", "Homer Simpson");