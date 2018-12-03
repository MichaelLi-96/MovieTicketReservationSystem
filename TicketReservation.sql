
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


drop table if exists Room;
CREATE TABLE Room
(
     RoomID INT PRIMARY KEY AUTO_INCREMENT,
     maxSeats INT,
     location VARCHAR(20));
ALTER Table Room AUTO_INCREMENT = 3000;


drop table if exists showTime;
CREATE TABLE showTime (
     showID INT AUTO_INCREMENT Primary Key,
     movieID INT,
     roomID INT,
     seats int,
     showDate DATE NOT NULL Default '2018-12-01' ,
     startTime TIME NOT NULL Default '00:00:00',
     FOREIGN KEY (movieID) references movie (movieID) on delete cascade,
     FOREIGN KEY (roomID) references Room (roomID) on delete cascade);
ALTER Table showTime AUTO_INCREMENT = 4000;


drop table if exists reservation;
create table reservation
(rID INT AUTO_INCREMENT Primary Key,
 uID INT, 
 showID INT,
 numofTicket INT,
 resDate Date NOT NULL Default '2018-12-01' ,
 FOREIGN KEY (uID) references customer (uID) on delete cascade,
 FOREIGN KEY (showID) references showTime (showID) on delete cascade);
ALTER Table reservation AUTO_INCREMENT = 5000;


/* Trigger : Automatically decrement seats in showTime when a reservation is made */

DROP TRIGGER IF EXISTS afterReserve;
delimiter //
CREATE TRIGGER afterReserve
AFTER INSERT ON RESERVATION
for each row
BEGIN
 update showTime set seats = seats- new.numofTicket where showID= new.showID ;
END;//
delimiter ;


INSERT into customer(uName,age) values("Alice",21) ;
INSERT into customer(uName,age) values("Bob",22) ;
INSERT into customer(uName,age) values("Cloe",23) ;
INSERT into customer(uName,age) values("David",24) ;


insert into movie(title,year) values("Shrek",1999);
insert into movie(title,year) values("Lion King",1995);
insert into movie(title,year) values("Frozen",2007);

insert into room(maxSeats, location) values(50, 101);
insert into room(maxSeats, location) values(30, 102);

insert into showTime(movieID, roomID, seats, showdate, startTime) values(2001, 3001, 30, '2018-12-02', '10:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2000, 3000, 30, '2018-12-02', '12:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2001, 3001, 30, '2018-12-02', '15:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2000, 3000, 30, '2018-12-02', '17:00:00' );


insert into reservation(uID, showID, numofTicket) values(1000, 4000, 5);
insert into reservation(uID, showID, numofTicket) values(1000, 4001, 3);
insert into reservation(uID, showID, numofTicket) values(1000, 4002, 1);
