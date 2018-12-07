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
	maxSeats TINYINT,
	location VARCHAR(128)
);
ALTER Table Room AUTO_INCREMENT = 4000;

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
	update showTime set seats = seats - new.numofTicket where showID = new.showID;
END;//
delimiter ;

DROP TRIGGER IF EXISTS afterDeleteRes;
delimiter //
CREATE TRIGGER afterDeleteRes
AFTER DELETE ON RESERVATION
for each row
BEGIN
	update showTime set seats = seats + old.numofTicket where showID = old.showID ;
	insert into cancelation (rID,uID, showID, numofTicket) values (old.rID, old.uID, old.showID, old.numofTicket);
END;//
delimiter ;

DROP TRIGGER IF EXISTS updateMovieRating;
delimiter //
CREATE TRIGGER updateMovieRating
AFTER INSERT ON RATING
for each row
BEGIN
	update movie set rating = (select AVG(rating) from Rating where movieID = new.movieID) where movieID = new.movieID;
END;//
delimiter ;

INSERT into customer(uName, password, age) values("Alice", "password", 21) ;

insert into movie(title,year) values("Shrek",1999);


insert into room(maxSeats, location) values(50, 101);



insert into showTime(movieID, roomID, seats, showdate, startTime) values(2001, 4001, 30, '2018-12-02', '10:00:00' );



insert into reservation(uID, showID, numofTicket) values(1000, 5000, 5);



delete from reservation where rID = 6001;
delete from reservation where rID = 6002;


-- * MAKE SURE TO USE YOUR OWN DIRECTORY LOCATION WHEN IMPORTING DATA FROM .txt FILES * --
-- Vivian's directory: C:/Users/vivz7/Documents/eclipse-workspace/movieTicketReservation/src/ticket.txt
-- Ying's directory: /Users/eliassun/Downloads/cs157A/movie/ticket.txt
-- Michael's directory: D:/SJSU/CS 157A/src/ticket.txt
LOAD DATA LOCAL INFILE 'C:/Users/vivz7/Documents/eclipse-workspace/movieTicketReservation/src/ticket.txt' INTO TABLE Ticket; 
LOAD DATA LOCAL INFILE 'C:/Users/vivz7/Documents/eclipse-workspace/movieTicketReservation/src/customer.txt' INTO TABLE Customer;
LOAD DATA LOCAL INFILE 'C:/Users/vivz7/Documents/eclipse-workspace/movieTicketReservation/src/movie.txt' INTO TABLE Movie;
LOAD DATA LOCAL INFILE 'C:/Users/vivz7/Documents/eclipse-workspace/movieTicketReservation/src/showtime.txt' INTO TABLE Showtime;
LOAD DATA LOCAL INFILE 'C:/Users/vivz7/Documents/eclipse-workspace/movieTicketReservation/src/room.txt' INTO TABLE Room;
LOAD DATA LOCAL INFILE 'C:/Users/vivz7/Documents/eclipse-workspace/movieTicketReservation/src/reservation.txt' INTO TABLE Reservation;