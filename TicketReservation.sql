DROP DATABASE IF EXISTS TicketReservation;
CREATE DATABASE TicketReservation;
USE TicketReservation;

DROP TABLE IF EXISTS Customer;
CREATE TABLE Customer (
	uID INT AUTO_INCREMENT PRIMARY KEY,
	uName VARCHAR(128) NOT NULL,
    password VARCHAR(128) NOT NULL,
	age INT,
	cardNumber VARCHAR(128) 
);
ALTER Table Customer AUTO_INCREMENT = 1000;

DROP TABLE IF EXISTS Movie;
CREATE TABLE Movie (
	movieID INT AUTO_INCREMENT PRIMARY KEY,
	title VARCHAR(128) UNIQUE,
	year SMALLINT,
	director VARCHAR(128),
	stars TINYINT
);
ALTER Table Movie AUTO_INCREMENT = 2000;


DROP TABLE IF EXISTS Room;
CREATE TABLE Room (
	roomID INT AUTO_INCREMENT PRIMARY KEY,
	maxSeats TINYINT,
	location VARCHAR(128)
);
ALTER Table Room AUTO_INCREMENT = 3000;


DROP TABLE IF EXISTS Showtime;
CREATE TABLE Showtime (
	showID INT AUTO_INCREMENT PRIMARY KEY,
	movieID INT,
	roomID INT,
	seats int,
	showDate DATE NOT NULL DEFAULT '2018-12-01' ,
	startTime TIME NOT NULL DEFAULT '00:00:00',
	FOREIGN KEY (movieID) REFERENCES Movie (movieID) ON DELETE CASCADE,
	FOREIGN KEY (roomID) REFERENCES Room (roomID) ON DELETE CASCADE
);
ALTER Table Showtime AUTO_INCREMENT = 4000;


DROP TABLE IF EXISTS Reservation;
CREATE TABLE Reservation (
	rID INT AUTO_INCREMENT PRIMARY KEY,
	uID INT, 
    showID INT,
	numOfTicket TINYINT,
	resDate DATETIME DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (uID) REFERENCES Customer (uID) ON DELETE CASCADE,
	FOREIGN KEY (showID) REFERENCES Showtime (showID) ON DELETE CASCADE
);
ALTER Table Reservation AUTO_INCREMENT = 5000;

DROP TABLE IF EXISTS Cancelation;
CREATE TABLE Cancelation (
	rID INT PRIMARY KEY NOT NULL,
	uID INT, 
	showID INT,
	numOfTicket TINYINT,
	canceledDate Date NOT NULL Default '2018-12-01' 
);


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

DROP TRIGGER IF EXISTS afterDeleteRes;
delimiter //
CREATE TRIGGER afterDeleteRes
AFTER delete ON RESERVATION
for each row
BEGIN
 update showTime set seats = seats + old.numofTicket where showID= old.showID ;
 insert into cancelation (rID,uID, showID, numofTicket) values(old.rID, old.uID, old.showID, old.numofTicket);
END;//
delimiter ;

INSERT into customer(uName, password, age) values("Alice", "password", 21) ;
INSERT into customer(uName,password, age) values("Bob", "password", 22) ;
INSERT into customer(uName, password, age) values("Cloe", "password", 23) ;
INSERT into customer(uName, password, age) values("David", "password", 24) ;


insert into movie(title,year) values("Shrek",1999);
insert into movie(title,year) values("Lion King",1995);
insert into movie(title,year) values("Frozen",2007);
insert into movie(title,year) values("incredibles",2007);
insert into movie(title,year) values("incredibles 2",2018);


insert into room(maxSeats, location) values(50, 101);
insert into room(maxSeats, location) values(30, 102);
insert into room(maxSeats, location) values(50, 103);
insert into room(maxSeats, location) values(50, 104);

insert into showTime(movieID, roomID, seats, showdate, startTime) values(2001, 3001, 30, '2018-12-02', '10:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2000, 3000, 30, '2018-12-02', '12:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2001, 3001, 30, '2018-12-02', '15:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2000, 3002, 30, '2018-12-02', '17:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2002, 3002, 30, '2018-12-02', '17:00:00' );


insert into reservation(uID, showID, numofTicket) values(1000, 4000, 5);
insert into reservation(uID, showID, numofTicket) values(1000, 4001, 3);
insert into reservation(uID, showID, numofTicket) values(1000, 4002, 1);
insert into reservation(uID, showID, numofTicket) values(1000, 4002, 6);
insert into reservation(uID, showID, numofTicket) values(1001, 4002, 2);
insert into reservation(uID, showID, numofTicket) values(1002, 4003, 4);


delete from reservation where rID=5001;
delete from reservation where rID=5002;

