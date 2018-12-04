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
						"root", "12345678");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	public void adminMain() {

		while (true) {
			
			System.out.println("Please select options:");
			System.out.print("[1]add movie   [2]search movie [3]data analysis [4]quit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1')
					addMovie();
				else if (command == '2')
					deleteUser() ;
				else if (command == '3')
					dataAnalysis() ;
				else if (command == '4') {
					System.out.println("Goodbye");
					System.exit(0);
				} else
					System.out.println("Invalid command");

			} catch (Exception e) {
				System.out.println("An error occurred.  Try again.");
			}
		}
	}

	private void dataAnalysis() {
		System.out.println("Please select options:");
		System.out.print("[1]now playing   [2]customer made most resevation  [3]average age of customer ");
		char command = sc.nextLine().trim().charAt(0);
		if (command == '1')
			currentShow();
		else if (command == '2')
			mostReservation() ;
		else if (command == '3')
			averageAge() ;
		else
			System.out.println("Invalid command");
		
	}

	private void averageAge() {
		// TODO Auto-generated method stub
		
	}

	private void mostReservation() {
		// TODO Auto-generated method stub
		
	}

	private void currentShow() {
		// TODO Auto-generated method stub
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
