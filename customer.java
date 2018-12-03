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
			System.out.print("[1]Register   [2]showtime [3]make reservation [4]cancel reservation [5]quit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1')
					register();
				else if (command == '2')
					showMovie();
				else if (command == '3')
					resevation();
				else if (command == '4')
					cancelReservation();
				else if (command == '5') {
					System.out.println("Goodbye");
					System.exit(0);
				} else
					System.out.println("Invalid command");

			} catch (Exception e) {
				System.out.println("An error occurred.  Try again.");
			}
		}
	}

	private void showMovie() {
		// TODO Auto-generated method stub
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = myConn.prepareStatement("select * from movie;");
			ResultSet rs = stmt.executeQuery();
			System.out.println("***** movie showtime ****");
			while (rs.next()) {
				String mtitle = rs.getString("title");
				String mid = rs.getString("movieID");
				System.out.println(mtitle);
				// System.out.println("Showtime: ") ;
				stmt2 = myConn.prepareStatement("select * from showtime where movieID=?;");
				stmt2.setString(1, mid);
				ResultSet rs2 = stmt2.executeQuery();
				if (!rs2.isBeforeFirst())
					System.out.println("No current show time for this movie.");
				while (rs2.next()) {
					String st = rs2.getString("startTime");
					String sid = rs2.getString("showID");
					System.out.println(st + " showID:" + sid);
				}
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

	private static void searchShowtime() {
		System.out.println("Search Showtime");
		System.out.println("Enter movie title:");
		String title = sc.nextLine().trim();

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
					System.out.println("Registered Successfully. Your id is: " + rs.getInt(1));
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

	private void resevation() {
		System.out.println("enter customer ID:");
		String uid = sc.nextLine().trim();
		System.out.println("enter showID:");
		String sid = sc.nextLine().trim();
		System.out.println("enter number of tickets:");
		String tickets = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("insert into reservation(uID, showID, numofTicket) values(?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			if (sid.isEmpty() || tickets.isEmpty()) {
				System.out.println("Please provide showID & number of tickets!");
			} else {
				stmt.setString(1, uid);
				stmt.setString(2, sid);
				stmt.setString(3, tickets);
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					System.out.println("Reservation made Successfully.Your reservation ID is: " + rs.getInt(1));
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

	private void cancelReservation() {
		System.out.println("enter reservation ID:");
		String rid = sc.nextLine().trim();

		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("delete from Reservation where rid= ? ");
			if (rid.isEmpty()) {
				System.out.println("Please enter reservation ID:");
			} else {
				stmt.setString(1, rid);
				int bool = stmt.executeUpdate();
				if (bool == 0)
					System.out.println("unable to delete\n");
				else
					System.out.printf("Reservation %s cancelled successfully!\n", rid);
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
