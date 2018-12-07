import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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
						"password");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	public void adminSignIn() {
		System.out.println();
		System.out.print("Please enter your Admin ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() >= 1 && id.length() < 4)) {
			System.out.println();
			System.out.println("Inputted Admin ID was not accepted. Please try again.");
		}
		else {
			System.out.print("Enter your password: ");
			String password = sc.nextLine().trim();
			PreparedStatement stmt = null;
			try {
				stmt = myConn.prepareStatement("select adminName from Admin where adminID =" + id +" and password ='" + password + "';",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmt.executeQuery(); 
				System.out.println();
				if (!rs.next()) {
					System.out.println("Admin account could not be found. Please try again.");
					System.out.println();
				} else {
					System.out.println("Welcome Administrator: " + rs.getString("adminName"));
					while (true) {
						adminMain();
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
	}
	
	private void adminMain() {
		System.out.println();
		while (true) {
			System.out.println("Please select an administrator option:");
			System.out.print("[1] Accounts     [2] Movies     [3] Data analysis     [4] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					accountsMain();
				}
				else if (command == '2') {
					moviesMain() ;
				}
				else if (command == '3') {
					dataAnalysisMain() ;
				}
				else if (command == '4') {
					System.out.println();
					System.out.println("Goodbye");
					System.exit(0);
				} 
				else {
					System.out.println();
					System.out.println("Invalid command");
				}
			} catch (Exception e) {
				System.out.println();
				System.out.println("An error occurred.  Try again.");
			}
		}
	}
	
	// Accounts menu
	//
	private void accountsMain() {
		while (true) {
			System.out.println();
			System.out.println("Please select an accounts option:");
			System.out.print("[1] Add An Administrator     [2] Edit Customer Info     [3] Delete A Customer     [4] Back To Admin Options     [5] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					addAdmin();
				}
				else if (command == '2') {
					editCustomerInfo();
				}
				else if (command == '3') {
					deleteCustomer();
				}
				else if (command == '4') {
					break;
				}
				else if (command == '5') {
					System.out.println();
					System.out.println("Goodbye.");
					System.exit(0);
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
			System.out.println();
			System.out.println("Please select a movies option:");
			System.out.println("[1] Add Movie     [2] Add Movie Showtime     [3] Archive Expired Showtimes     [4] Show Now Playing Movies     ");
			System.out.print("[5] Back to Admin Options     [6] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					addMovie();
				}
				else if (command == '2') {
					addShowtime();
				}
				else if (command == '3') {
					archiveShowtimes();
				}
				else if (command == '4') {
					currentShow();
				}
				else if (command == '5') {
					break;
				}
				else if (command == '6') {
					System.out.println();
					System.out.println("Goodbye.");
					System.exit(0);
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

	private void dataAnalysisMain() {
		System.out.println();
		while (true) {
			System.out.println("Please select a data analysis option:");
			System.out.println("[1] Movies With Reservations Greater Than 'Input'     [2] Customer That Made The Most Reservations ");
			System.out.print("[3] Average Age Of Customers Who Made A Reservation     [4] Go Back To Admin Options     [5] Exit: ");
			char command = sc.nextLine().trim().charAt(0);
			if (command == '1') {
				popularMovie();
			}
			else if (command == '2') {
				mostReservation();
			}
			else if (command == '3') {
				averageAge();
			}
			else if (command == '4') {
				break;
			} 
			else if (command == '5') {
				System.out.println();
				System.out.println("Goodbye.");
				System.exit(0);
			}
			else {
				System.out.println();
				System.out.println("Invalid command");
			}
		}
		adminMain();
	}


	private void currentShow() {
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select distinct title from movie mv right outer JOIN showtime  st on mv.movieID = st.movieid;");
			ResultSet rs = stmt.executeQuery();
			System.out.println("***** Now Playing *****");
			while (rs.next()) {
				String mtitle = rs.getString("title");
				System.out.println(mtitle);		
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
	
	private void addAdmin() {
		// TODO Auto-generated method stub
	}

	private void editCustomerInfo() {
		// TODO Auto-generated method stub
	}

	private void deleteCustomer() {
		// TODO Auto-generated method stub
	}

	private void addMovie() {
		System.out.println("enter movie title:");
		String title = sc.nextLine().trim();
		System.out.println("enter movie Year:");
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
	
	private void addShowtime() {
		// TODO Auto-generated method stub
	}
	
	private void archiveShowtimes() {

		CallableStatement cstmt = null;
		try { 
			String sql = "{call archiveShowtimes()}";
			cstmt = myConn.prepareCall(sql);
			cstmt.execute();
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
	
	
	private void popularMovie() {
		System.out.println();
		System.out.print("Enter a number: ");
		String num = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select title from movie where movieID in( select movieID from showtime where showID in "
					+ " (select distinct showID as 'popmv' from reservation group by showID having(count(*)>=?)));");
			stmt.setString(1, num);
			ResultSet rs = stmt.executeQuery();
			System.out.printf("Movie(s) with reservation >= %s: \n", num);
	//1		if(!rs.next()) System.out.println("No movie found.");
			System.out.println();
			while (rs.next()) {	
				String mv = rs.getString("title");	
				System.out.println(mv);		
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
		System.out.println();
	}
	
	private void mostReservation() {
		// TODO Auto-generated method stub
		
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
		System.out.println();
	}

}
