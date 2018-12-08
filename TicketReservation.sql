DROP DATABASE IF EXISTS TicketReservation;
CREATE DATABASE TicketReservation;
USE TicketReservation;

DROP TABLE IF EXISTS Admin;
CREATE TABLE Admin (
	adminID INT AUTO_INCREMENT PRIMARY KEY,
	adminName VARCHAR(128) NOT NULL,
    password VARCHAR(128) NOT NULL
);
ALTER Table Admin AUTO_INCREMENT = 0;

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
    director VARCHAR(128),
	year SMALLINT,
	rating DECIMAL(4,2) DEFAULT 0
);
ALTER Table Movie AUTO_INCREMENT = 2000;

DROP TABLE IF EXISTS Rating;
CREATE TABLE Rating (
	ratingID INT AUTO_INCREMENT PRIMARY KEY,
	movieID INT,
    uID INT,
	rating DECIMAL(4,2) DEFAULT 0,
    FOREIGN KEY (movieID) REFERENCES Movie (movieID) ON DELETE CASCADE,
    FOREIGN KEY (uID) REFERENCES Customer (uID) ON DELETE CASCADE
);
ALTER Table Rating AUTO_INCREMENT = 3000;

DROP TABLE IF EXISTS Ticket;
CREATE TABLE Ticket (
	ticketType VARCHAR(32) PRIMARY KEY,
	price DECIMAL(5,2)
);

DROP TABLE IF EXISTS Room;
CREATE TABLE Room (
	roomID INT AUTO_INCREMENT PRIMARY KEY,
	maxSeats INT,
	location VARCHAR(128)
);
ALTER Table Room AUTO_INCREMENT = 4000;

DROP TABLE IF EXISTS Showtime;
CREATE TABLE Showtime (
	showID INT AUTO_INCREMENT PRIMARY KEY,
	movieID INT,
	roomID INT,
	seats int,
	showDate DATE NOT NULL DEFAULT '2018-12-01',
	startTime TIME NOT NULL DEFAULT '00:00:00',
	FOREIGN KEY (movieID) REFERENCES Movie (movieID) ON DELETE CASCADE,
	FOREIGN KEY (roomID) REFERENCES Room (roomID) ON DELETE CASCADE
);
ALTER Table Showtime AUTO_INCREMENT = 5000;

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
ALTER Table Reservation AUTO_INCREMENT = 6000;

DROP TABLE IF EXISTS Cancelation;
CREATE TABLE Cancelation (
	rID INT PRIMARY KEY,
	uID INT, 
	showID INT,
	numOfTicket TINYINT,
	cancelledDate Date NOT NULL Default '2018-12-01',
	FOREIGN KEY (uID) REFERENCES Customer (uID) ON DELETE CASCADE,
	FOREIGN KEY (showID) REFERENCES Showtime (showID) ON DELETE CASCADE 
);

DROP TABLE IF EXISTS ExpiredShowtime;
CREATE TABLE ExpiredShowtime (
	showID INT PRIMARY KEY,
	movieID INT,
	roomID INT,
	seats int,
	showDate DATE,
	startTime TIME
);

/* Trigger : Automatically decrement seats in a showtime after a reservation is made */
DROP TRIGGER IF EXISTS afterReserve;
delimiter //
CREATE TRIGGER afterReserve
AFTER INSERT ON Reservation
for each row
BEGIN
	update ShowTime set seats = seats - new.numofTicket where showID = new.showID;
END;//
delimiter ;

/* Trigger : Automatically opens up seats in a showtime after a reservation is deleted*/
DROP TRIGGER IF EXISTS afterDeleteRes;
delimiter //
CREATE TRIGGER afterDeleteRes
AFTER DELETE ON Reservation
for each row
BEGIN
	update Showtime set seats = seats + old.numofTicket where showID = old.showID ;
	insert into Cancelation (rID,uID, showID, numofTicket) values (old.rID, old.uID, old.showID, old.numofTicket);
END;//
delimiter ;

/* Trigger : Automatically updates and averages a rating for a movie*/
DROP TRIGGER IF EXISTS updateMovieRating;
delimiter //
CREATE TRIGGER updateMovieRating
AFTER INSERT ON Rating
for each row
BEGIN
	update Movie set rating = (select AVG(rating) from Rating where movieID = new.movieID) where movieID = new.movieID;
END;//
delimiter ;

/* Procedure : Archives expired showtimes and removes expired showtimes from the showtime table*/
drop PROCEDURE if exists archiveShowtimes;
delimiter //
create PROCEDURE archiveShowtimes()
BEGIN
	insert into ExpiredShowtime select showID, movieID, roomID, seats, showDate, startTime from Showtime where showDate < CURRENT_DATE();
	delete from Showtime where showDate < CURRENT_DATE();
END; //
delimiter ;

INSERT into Admin(adminName, password) values("Admin", "password");

/*LOAD DATA LOCAL INFILE '/Users/eliassun/workshop/MovieTicket/src/showtime.txt' INTO TABLE Showtime fields terminated by '\t' lines terminated by '\n';

MAKE SURE TO USE YOUR OWN DIRECTORY LOCATION WHEN IMPORTING DATA FROM .txt FILES 

-- Vivian's directory: C:/Users/vivz7/Documents/eclipse-workspace/movieTicketReservation/src/ticket.txt
-- Ying's directory: /Users/eliassun/workshop/MovieTicket/src/
-- Michael's directory: D:/SJSU/CS 157A/src/ticket.txt
-- LOAD DATA LOCAL INFILE '/Users/eliassun/workshop/MovieTicket/src/reservation.txt' INTO TABLE Reservation;

*/


LOAD DATA LOCAL INFILE '/Users/eliassun/workshop/MovieTicket/src/ticket.txt' INTO TABLE Ticket; 
LOAD DATA LOCAL INFILE '/Users/eliassun/workshop/MovieTicket/src/customer.txt' INTO TABLE Customer;
LOAD DATA LOCAL INFILE '/Users/eliassun/workshop/MovieTicket/src/movie.txt' INTO TABLE Movie;
LOAD DATA LOCAL INFILE '/Users/eliassun/workshop/MovieTicket/src/room.txt' INTO TABLE Room;


insert into showTime(movieID, roomID, seats, showdate, startTime) values(2001, 4001, 30, '2018-12-02', '10:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2000, 4000, 30, '2018-12-05', '12:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2001, 4001, 30, '2018-12-03', '11:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2000, 4002, 30, '2018-12-07', '13:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2000, 4002, 30, '2018-12-01', '19:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2002, 4002, 30, '2018-12-01', '9:00:00' );
insert into showTime(movieID, roomID, seats, showdate, startTime) values(2001, 4001, 30, '2018-12-02', '15:00:00' );


insert into reservation(uID, showID, numofTicket) values(1000, 5000, 5);
insert into reservation(uID, showID, numofTicket) values(1000, 5001, 2);
insert into reservation(uID, showID, numofTicket) values(1000, 5002, 1);
insert into reservation(uID, showID, numofTicket) values(1000, 5002, 6);
insert into reservation(uID, showID, numofTicket) values(1001, 5002, 2);
insert into reservation(uID, showID, numofTicket) values(1002, 5003, 4);
delete from reservation where rID = 6001;
delete from reservation where rID = 6002;

