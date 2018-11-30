# Movie Ticket Reservation
This is our final project for CS157A: Intro to Databases at San Jose State University with Professor Kim during Fall Semester 2018.
You will need to have MySQL and JDBC installed.
This was developed using Eclipse IDE.

Team Name: Yoobi

Members: 
* Vivian Leung
* Ying Wang
* Michael Li
# Setup
Clone repository
```bash
git clone https://github.com/wangy754/movieTicketReservation.git
```

## (Windows) Start MySQL
```bash
mysql -h localhost -u root -p --local-infile=1 
//Enter password

SHOW VARIABLES LIKE 'local_infile'; 
//This checks the value; make sure to set it to 1 with the command below

SET GLOBAL local_infile=1;
//This allows text files with table info to be imported to the database

mysql> source <your directory of sql file here>
//In my case: C:\Users\vivz7\Documents\GitHub\CS157A\movieTicketReservation\TicketReservation.sql
```

## (Mac) Start MySQL
```bash
<your directory of mysql here> -u root -p --local-infile=1 
//In my case: /usr/local/mysql/bin/mysql

SET GLOBAL local_infile=1; 
//This allows text files with table info to be imported to the database

mysql> source <your diretory of sql file here>
//In my case: ~/Dropbox/sjsu/cs157a/sql/library/library.sql
```

### References
https://github.com/amirradman/Ace.git
