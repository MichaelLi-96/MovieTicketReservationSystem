
drop database if exists ticketReservation;
create database ticketReservation;
use ticketReservation;

drop table if exists customer;
create table customer
(uID INT AUTO_INCREMENT Primary Key,
 uNAME VARCHAR(30) NOT NULL,
age INT,
cardN VARCHAR(30) );
ALTER Table customer AUTO_INCREMENT = 1000;

drop table if exists Movie;
CREATE TABLE Movie 
(movieID INT AUTO_INCREMENT Primary Key,
     title VARCHAR(128) UNIQUE,
     year INT,
     director VARCHAR(128),
     stars TINYINT);
ALTER Table Movie AUTO_INCREMENT = 2000;