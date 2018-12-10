import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.*;

import com.mysql.cj.util.StringUtils;


public class admin {
	
	static Scanner sc = new Scanner(System.in);
	dbconnection mt = new dbconnection();
	Connection myConn = mt.myConn;

	public class dbconnection {
		public Connection myConn;

		public dbconnection() {

			try {
				myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicketReservation?useSSL=true",
						"root", 
						//enter Mi19Li96 password here
						//Michael's password: Mi19Li96
						//Vivian's password: currybreadchai
						"Mi19Li96");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	public void adminSignIn() {
		System.out.print("\nPlease enter your admin ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() >= 1 && id.length() < 4)) {
			System.out.println("\nInputted admin ID was not accepted. Please try again.");
		}
		else {
			System.out.print("\nEnter your password: ");
			String password = sc.nextLine().trim();
			PreparedStatement stmt = null;
			try {
				stmt = myConn.prepareStatement("select adminName from Admin where adminID =" + id +" and password ='" + password + "';",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.executeQuery(); 
				if (!rs.next()) {
					System.out.println("\nAdmin account could not be found. Please try again.\n");
				} else {
					System.out.println("\nWelcome Administrator, " + rs.getString("adminName") + "!");
					adminMain();
					return;
				}
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			} finally {
				try {
					stmt.close();
				} catch (SQLException exc) {
					System.out.println("An error occured. Error: => " + exc.getMessage());
				}
			}
		}
	
	}
	
	private void adminMain() {
		while (true) {
			System.out.println("\nPlease select an administrator option:");
			System.out.print("\n[1] Accounts    \n[2] Movies     \n[3] Data analysis     \n[4] Logout\n\n");
			try {
				String command = sc.nextLine().trim();
				if (command.equals("1")) {
					accountsMain();
				}
				else if (command.equals("2")) {
					moviesMain() ;
				}
				else if (command.equals("3")) {
					dataAnalysisMain() ;
				}
				else if (command.equals("4")) {
					System.out.println("Logged out of admin.\n");
					return;
				} 
				else {
					System.out.println("\nInvalid command");
				}
			} catch (Exception e) {
				System.out.println("\nAn error occurred.  Try again.");
			}
		}
	}
	
	// Accounts menu
	//
	private void accountsMain() {
		while (true) {
			System.out.println("\nPlease select an accounts option:");
			System.out.print("\n[1] Add An Administrator     \n[2] Edit Customer Info     \n[3] Delete A Customer     \n[4] Back To Admin Options\n\n");
			try {
				String command = sc.nextLine().trim();
				if (command.equals("1")) {
					addAdmin();
				}
				else if (command.equals("2")) {
					editCustomerInfo();
				}
				else if (command.equals("3")) {
					deleteCustomer();
				}
				else if (command.equals("4")) {
					break;
				}
				else {
					System.out.println();
					System.out.println("Invalid command.");
				}

			} catch (Exception e) {
				System.out.println();
				System.out.println("An error occurred. Please try again.");
			}
		}
		adminMain();
	}
	
	// Movie menu
	//
	private void moviesMain() {
		while (true) {
			System.out.println("\nPlease select a movies option:");
			System.out.println("\n[1] View Movie Inventory  \n[2] Add Showtime \n[3] Show Now Playing Movies 	\n[4] Archive Expired Showtimes \n[5] Back to Admin Options");
			try {
				String command = sc.nextLine().trim();
				if (command.equals("1")) {
					showAllMoviesAlphabetically();
				}
				else if (command.equals("2")) {
					addShowtime();
					
				}
				else if (command.equals("3")) {
					currentShow();
				}
				else if (command.equals("4")) {
					archiveShowtimes();
				}
				else if (command.equals("5")) {
					break;
				}
				else {
					System.out.println("\nInvalid command.");
				}
			} catch (Exception e) {
				System.out.println("\nAn error occurred. Please try again.");
			}
		}
		adminMain();
	}

	private void dataAnalysisMain() {
		while (true) {
			System.out.println("\nPlease select a data analysis option:");
			System.out.println("\n[1] Movies With Reservations Greater Than 'Input'  \n[2] Average Age Of Customers Who Made A Reservation \n[3] Customers make no transaction \n[4] Go Back To Admin Options\n");
			String command = sc.nextLine().trim();
			if (command.equals("1")) {
				popularMovie();
			}
			else if (command.equals("2")) {
				averageAge();
			}
			else if (command.equals("3")) {
				noTransaction();
			} 
			else if (command.equals("4")) {
				break;
			} 
			else {
				System.out.println("\nInvalid command");
			}
		}
		adminMain();
	}

	
	private void addAdmin() {
		//Input Customer.name
		System.out.print("\nEnter new administrator name: ");
		String name = sc.nextLine().trim();
		//Input Customer.password
		System.out.print("Enter password of new administrator: ");
		String password = sc.nextLine().trim();
		PreparedStatement stmt = null;
		System.out.println();
		//Insert user into Admin table
		try {
			stmt = myConn.prepareStatement("INSERT into admin(adminName, password) values(?,?);",
					Statement.RETURN_GENERATED_KEYS);
			if (name.isEmpty() || password.isEmpty()) {
				System.out.println("Please provide a name and password");
			} else {
				stmt.setString(1, name);
				stmt.setString(2, password);
				stmt.executeUpdate();
				
				//Store results
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					System.out.println("New administrator was added successfully. New administrator ID is: " + rs.getInt(1));
				}
				else {
					System.out.println("New administrator was not added successfully. Please try again.");
				}
			}
		} catch (SQLException exc) {
			System.out.println();
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt.close();
			} catch (SQLException exc) {
				System.out.println();
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}

	private void editCustomerInfo() {
		System.out.println();
		System.out.print("Enter a customer ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
			System.out.println();
			System.out.println("Inputted customer ID was not accepted. Please try again.");
		}
		else {
			PreparedStatement stmt1 = null;
			PreparedStatement stmt2 = null;
			try {
				stmt1 = myConn.prepareStatement("select * from Customer where uID = ?;",
						Statement.RETURN_GENERATED_KEYS);
				stmt1.setString(1, id);
				ResultSet rs1 = stmt1.executeQuery(); 
				if (!rs1.next()) {
					System.out.println("Account could not be found. Please try again.");
				} 
				else {
					boolean didEdit = false;
					String name = rs1.getString("uName");
					String password = rs1.getString("password");
					String age = rs1.getString("age");
					String cardNumber = rs1.getString("cardNumber");
					
					System.out.println(rs1.getString("uName") + "'s Account");
					System.out.println();
					System.out.println("Edit customer name?:");
					System.out.print("[1] Yes     [2] No: ");
					String command = sc.nextLine().trim();
					if(command.equals("1")) {
						System.out.println();
						System.out.print("New name of customer: ");
						name = sc.nextLine().trim();
						didEdit = true;
					}
					System.out.println();
					System.out.println("Edit customer password?");
					System.out.print("[1] Yes     [2] No: ");
					command = sc.nextLine().trim();
					if(command.equals("1")) {
						System.out.println();
						System.out.print("New password of customer: ");
						password = sc.nextLine().trim();
						didEdit = true;
					}
					System.out.println();
					System.out.println("Edit customer age?");
					System.out.print("[1] Yes     [2] No: ");
					command = sc.nextLine().trim();
					if(command.equals("1")) {
						System.out.println();
						System.out.print("New age of customer: ");
						age = sc.nextLine().trim();		
						didEdit = true;
					}
					System.out.println();
					System.out.println("Edit customer card?");
					System.out.print("[1] Yes     [2] No: ");
					command = sc.nextLine().trim();
					if(command.equals("1")) {
						System.out.println();
						System.out.print("New card number of customer: ");
						cardNumber = sc.nextLine().trim();
						didEdit = true;
					}
					System.out.println();
					if(didEdit) {
						stmt2 = myConn.prepareStatement("update Customer set uName = ?, password = ?, age = ?, cardNumber = ? where uID =" + id + ";",
								Statement.RETURN_GENERATED_KEYS);
						stmt2.setString(1, name);
						stmt2.setString(2, password);
						stmt2.setString(3, age);
						stmt2.setString(4, cardNumber);
						int rowCount = stmt2.executeUpdate(); 
						if (rowCount > 0) {
							System.out.println("The changes were successful.");
						} 
						else {
							System.out.println("The changes were not successful. Please try again.");
						}
					}
					else {
						System.out.println("No changes were made to " + name + "'s account.");
					}
				}
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			} finally {
				try {
					stmt1.close();
				} catch (SQLException exc) {
					System.out.println("An error occured. Error: => " + exc.getMessage());
				}
			}
		}
		accountsMain();
	}

	private void deleteCustomer() {
		System.out.println();
		System.out.print("Enter customer ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
			System.out.println();
			System.out.println("Inputted customer ID was not accepted. Please try again.");
		}
		else {
			PreparedStatement stmt1 = null;
			PreparedStatement stmt2 = null;
			try {
				stmt1 = myConn.prepareStatement("select uName from Customer where uID = ?;",
						Statement.RETURN_GENERATED_KEYS);
				stmt1.setString(1, id);
				ResultSet rs = stmt1.executeQuery(); 
				System.out.println();
				if (!rs.next()) {
					System.out.println("Account could not be found. Please try again.");
				} 
				else {
					System.out.println("Are you sure you want to delete " + rs.getString("uName") + "'s Account?");
					while (true) {
						System.out.println("\nPlease select an option:");
						System.out.print("\n[1] Yes     \n[2] Cancel");
						try {
							String command = sc.nextLine().trim();
							if (command.equals("1")) {
								try {
									stmt2 = myConn.prepareStatement("delete from customer where uID = ?;",
											Statement.RETURN_GENERATED_KEYS);
									stmt2.setString(1, id);
									int rowCount = stmt2.executeUpdate();
									System.out.println();
									if (rowCount > 0) {
										System.out.println("Account deleted successfully.");
									}
									else {
										System.out.println("Account was not deleted successfully.");
									}
								} catch (SQLException exc) {
									System.out.println();
									System.out.println("An error occured. Error: => " + exc.getMessage());
								} finally {
									try {
										stmt2.close();
										break;
									} catch (SQLException exc) {
										System.out.println();
										System.out.println("An error occured. Error: => " + exc.getMessage());
									}
								}
							}
							else if (command.equals("2")) {
								break;
							}
							else {
								System.out.println();
								System.out.println("Invalid command.");
							}
						} catch (Exception e) {
							System.out.println();
							System.out.println("An error occurred. Please try again.");
						}
					}
				}
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			} finally {
				try {
					stmt1.close();
				} catch (SQLException exc) {
					System.out.println("An error occured. Error: => " + exc.getMessage());
				}
			}
		}
		accountsMain();
	}

	private void addMovie() {
		System.out.print("Enter movie title: ");
		String title = sc.nextLine().trim();
		System.out.print("Enter movie Year: ");
		String yr = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("insert into movie(title,year) values(?,?)", Statement.RETURN_GENERATED_KEYS);
			if (title.isEmpty() || yr.isEmpty()) {
				System.out.println("Please provide title & year !");
			} else {
				stmt.setString(1, title);
				stmt.setString(2, yr);
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					System.out.println();
					System.out.println("Movie added Successfully. Movie id is: " + rs.getInt(1));
				}
			}
		} catch (SQLException exc) {
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt.close();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}
	
	
	private void archiveShowtimes() {

		CallableStatement cstmt = null;
		try { 
			String sql = "{call archiveShowtimes()}";
			cstmt = myConn.prepareCall(sql);
			cstmt.execute();
			System.out.println();
			System.out.println("Archiving expired showtimes was successful.");
		}
		catch(SQLException exc)
		{
			System.out.println("An error occured. Error: => " + exc.getMessage());
		}
		finally{
			try {
				cstmt.close();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}

	// Show all the movies in our database alphabetically
	//
	private void showAllMoviesAlphabetically() {
		System.out.println();
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			stmt1 = myConn.prepareStatement("select * from movie order by title;");
			stmt2 = myConn.prepareStatement("select count(*) as totalMovies from movie");
			ResultSet rs = stmt1.executeQuery();
			ResultSet rs2 = stmt2.executeQuery();
			System.out.println("ID\tYear\tStars\tTitle");
			while (rs.next()) {
				String mtitle = rs.getString("title");
				String mid = rs.getString("movieID");
				String year = rs.getString("year");
				double rating = rs.getDouble("rating");
					System.out.println( "" + mid + "\t" + year + "\t" + rating + "\t" + mtitle +  "");
				}

				while(rs2.next()){
				int totalMovies = rs2.getInt("totalMovies");
				System.out.println("\nTotal Movies: " + totalMovies + "");
			}
		} catch (SQLException exc) {
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt1.close();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}

	// Add a showtime
	//
	private void addShowtime() throws ParseException {
		PreparedStatement s = null;
		String seats = "";
		//Input Showtime.movieID
		System.out.print("\nEnter Movie ID: ");
		String movieID = sc.nextLine().trim();
		//Input Showtime.roomID
		System.out.print("Enter desired show Room ID: ");
		String roomID = sc.nextLine().trim();

		try{
			s = myConn.prepareStatement("select maxSeats from Room where roomID=" + roomID + ";", Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = s.executeQuery();
			while(rs.next()){
				seats = rs.getString("maxSeats");
				System.out.println("Available seats: " + seats);
			}
		}

		catch (SQLException exc) {
			System.out.println();
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				s.close();
			} catch (SQLException exc) {
				System.out.println();
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
		//Remove later
		// System.out.print("Enter desired seat amount: ");
		// String seats = sc.nextLine().trim();
		//Input Showtime.date
		System.out.print("Enter desired show DATE in the format [YYYY-MM-DD] : ");
		String showDate = sc.nextLine().trim();
		//Input Showtime.time
		System.out.print("Enter desired show TIME in the format [HH:MM:SS] : ");
		String showTime = sc.nextLine().trim();
		
		PreparedStatement stmt = null;
		System.out.println();
		//Insert user into Customer table
		try {
			stmt = myConn.prepareStatement("INSERT into Showtime(movieID, roomID, seats, showDate, startTime) values(?,?,?,?,?);",
					Statement.RETURN_GENERATED_KEYS);
			if (movieID.isEmpty() || roomID.isEmpty() || showDate.isEmpty() || showTime.isEmpty()) {
				System.out.println("Please provide valid entries for each field");
			} 
			else {
				stmt.setString(1, movieID);
				stmt.setString(2, roomID);
				stmt.setString(3, seats);
				stmt.setString(4, showDate);
				stmt.setString(5, showTime);
				stmt.executeUpdate();
				
				//Store results
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					System.out.println("Added successfully. Your Showtime ID is: " + rs.getInt(1));
				}
				else {
					System.out.println("Add was not successful.");
				}
			}
		} catch (SQLException exc) {
			System.out.println();
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt.close();
			} catch (SQLException exc) {
				System.out.println();
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}
	
	private void currentShow() {
	System.out.println();
	PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement(
					"select distinct title, year, rating from movie mv right outer JOIN showtime  st on mv.movieID = st.movieid;");
			ResultSet rs = stmt.executeQuery();
			System.out.println("***** Now Playing *****\n");
			System.out.println("Rating \t Year \t Title");
			while (rs.next()) {
				String year = rs.getString("year");
				String rating = rs.getString("rating");
				String mtitle = rs.getString("title");
				System.out.println(rating + "\t " + year + "\t" + mtitle);
			}
			System.out.println("\n------------------------------------------");
		} catch (SQLException exc) {
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt.close();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}
	
	private void popularMovie() {
		System.out.println();
		System.out.print("Enter a number: ");
		String num = sc.nextLine().trim();
		System.out.println();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select title from movie where movieID in( select movieID from showtime where showID in "
					+ " (select distinct showID as 'popmv' from reservation group by showID having(count(*)>=?)));");
			stmt.setString(1, num);
			ResultSet rs = stmt.executeQuery();
			System.out.printf("Movie(s) with reservation >= %s: \n", num);
			if(!rs.next()) System.out.println("No movie found.");
			else {
				while (rs.next()) {	
					String mv = rs.getString("title");	
					System.out.println(mv);		
				}
			}
		} catch (SQLException exc) {
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt.close();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}
	
	private void noTransaction() {
		System.out.println();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select uname from customer where uID not in (select uid from reservation union select uid from cancellation);");
			ResultSet rs = stmt.executeQuery();
			System.out.println("***** Below customers have not made any transaction. *****");
			while (rs.next()) {
				String cname = rs.getString("uName");
				System.out.println(cname);		
			}
			System.out.println("***********************");
		} catch (SQLException exc) {
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt.close();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
		
	}

	private void averageAge() {
		System.out.println();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("Select avg(age) as avAge from customer c NATURAL JOIN(select distinct uid from reservation group by uid) r;");
			ResultSet rs = stmt.executeQuery();
			System.out.print("Average age of customer who made reservation: ");
			if(rs==null) {
					System.out.println("Currently no user made reservation.");	
			}
			
			while (rs.next()) {
				Double age = rs.getDouble("avAge");	
				System.out.printf("%.1f",age);		
			}
			System.out.println();
		} catch (SQLException exc) {
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt.close();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}

}
