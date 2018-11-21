import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class customer {
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

	public void customMain() {

		while (true) {
			// System.out.println("Welcome to movie ticket reservation system!");
			System.out.println("Please select options:");
			System.out.print("[1]Register   [2]Search movie  [3]Quit: ");
			try {x
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1')
					register();
				else if (command == '2')
					searchMovie();
				else if (command == '3') {
					System.out.println("Goodbye");
					System.exit(0);
				} else
					System.out.println("Invalid command");

			} catch (Exception e) {
				System.out.println("An error occurred.  Try again.");
			}
		}
	}

	private static void searchMovie() {
		System.out.println("Search:");
		
		System.out.println("enter movie name:");
		String name = sc.nextLine().trim();
		System.out.println()
	}

	private void register() {
		System.out.println("enter name:");
		String name = sc.nextLine().trim();
		System.out.println("enter age:");
		String age = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("INSERT into customer(uName,age) values(?,?)",
					Statement.RETURN_GENERATED_KEYS);
			if (name.isEmpty() || age.isEmpty()) {
				System.out.println("Please provide name & age!");
			} else {
				stmt.setString(1, name);
				stmt.setString(2, age);
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					System.out.println("Registered Successfully. \n Your id is: " + rs.getInt(1));
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
	
	private void 

}
