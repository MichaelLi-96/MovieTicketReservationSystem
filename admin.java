import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

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
						//enter mysql password here
						//Michael's password: Mi19Li96
						//Vivian's password: currybreadchai
						"Mi19Li96");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	public void adminMain() {
		while (true) {
			System.out.println("Please select options:");
			System.out.print("[1] Add movie     [2] Current Shows     [3] Data analysis     [4] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					addMovie();
				}
				else if (command == '2') {
					currentShow() ;
				}
				else if (command == '3') {
					dataAnalysis();
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

	private void dataAnalysis() {
		System.out.println("Please select an option:");
		System.out.print("[1] Look for movie with reservations greater than \n[2] Customer that made the most resevations\n[3] Average age of customer who made reservation    [4] go back to mainmenu: ");
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
			adminMain() ;
		} 
		else {
			System.out.println();
			System.out.println("Invalid command");
		}
	}

	private void popularMovie() {
		// TODO Auto-generated method stub
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
		
		
	}

	private void averageAge() {
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

	private void mostReservation() {
		// TODO Auto-generated method stub
		
	}

	private void currentShow() {
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select distinct title from movie mv right outer JOIN showtime  st on mv.movieID = st.movieid;");
			ResultSet rs = stmt.executeQuery();
			System.out.println("***** now playing  ****");
			while (rs.next()) {
				String mtitle = rs.getString("title");
				System.out.println(mtitle);		
			}
			System.out.println("***************");
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

	private void addMovie() {
		// TODO Auto-generated method stub
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
		
	private static void deleteUser() {
		System.out.println("enter name to delete:");
		
	}
	
}
