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
						"root", "password");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}

	public void customerMain() {
		while (true) {
			System.out.println();
			System.out.println("Please select a customer option: ");
			System.out.print("[1] Account     [2] Showtimes     [3] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					accountMain();
				}
				else if (command == '2') {
					showtimeMain();
				}
				else if (command == '3') {
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
	}
	
	private void accountMain() {
		while (true) {
			System.out.println();
			System.out.println("Please select an account option:");
			System.out.print("[1] Create an Account     [2] Edit Your Account     [3] Delete Your Account     [4] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					register();
				}
				else if (command == '2') {
					editAccount();
				}
				else if (command == '3') {
					deleteAccount();
				}
				else if (command == '4') {
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
	}

	private void showtimeMain() {
		while (true) {
			System.out.println();
			System.out.println("Please select a showtime option:");
			System.out.print("[1] Show Movies     [2]Make Reservation     [3] Cancel Reservation     [4] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					showMovie();
				}
				else if (command == '2') {
					resevation();
				}
				else if (command == '3') {
					cancelReservation();
				}
				else if (command == '4') {
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
	}

	private void register() {
		System.out.println();
		System.out.print("Enter your name: ");
		String name = sc.nextLine().trim();
		System.out.print("Enter a password: ");
		String password = sc.nextLine().trim();
		System.out.print("Enter your age: ");
		String age = sc.nextLine().trim();
		PreparedStatement stmt = null;
		System.out.println();
		try {
			stmt = myConn.prepareStatement("INSERT into customer(uName, password, age) values(?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			if (name.isEmpty() || password.isEmpty() || age.isEmpty()) {
				System.out.println("Please provide name, password, & age!");
			} else {
				stmt.setString(1, name);
				stmt.setString(2, password);
				stmt.setString(3, age);
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					System.out.println("Registered Successfully. Your ID is: " + rs.getInt(1));
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
	
	private void editAccount() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		System.out.print("Enter your password: ");
		String password = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select uName from Customer where uID =" + id +" and password ='" + password + "'",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.executeQuery(); 
			System.out.println();
			if (!rs.next()) {
				System.out.println("Account could not be found. Please try again.");
			} else {
				System.out.println(rs.getString("uName") + "'s Account");
				while (true) {
					System.out.println();
					System.out.println("Please select an option:");
					System.out.print("[1] Edit name     [2] Edit password     [3] Edit age     [4] Set card     [5] Remove card     [6] Back to account options     [7] Exit: ");
					try {
						char command = sc.nextLine().trim().charAt(0);
						if (command == '1') {
							editName(id);
						}
						else if (command == '2') {
							editPassword(id);
						}
						else if (command == '3') {
							editAge(id);
						}
						else if (command == '4') {
							setCard(id);
						}
						else if (command == '5') {
							removeCard(id);
						}
						else if (command == '6') {
							break;
						}
						else if (command == '7') {
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
					accountMain();
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
	
	private void editName(String id) {
		System.out.println();
		System.out.print("What is your new name: ");
		String name = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("update customer set uName = ? where uID =" + id,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			int rowCount = stmt.executeUpdate();
			if (rowCount > 0) {
				System.out.println();
				System.out.println("Name edited successfully. Your name is now: " + name );
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
	
	private void editPassword(String id) {
		System.out.println();
		System.out.print("What is your new password: ");
		String password = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("update customer set password = ? where uID =" + id,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, password);
			int rowCount = stmt.executeUpdate();
			if (rowCount > 0) {
				System.out.println();
				System.out.println("Password edited successfully. Your password is now: " + password );
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
	
	private void editAge(String id) {
		System.out.println();
		System.out.print("What is your new age: ");
		String age = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("update customer set age = ? where uID =" + id,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, age);
			int rowCount = stmt.executeUpdate();
			if (rowCount > 0) {
				System.out.println();
				System.out.println("Age edited successfully. Your age is now: " + age );
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
	
	private void setCard(String id) {
		System.out.println();
		System.out.print("Set a new card number: ");
		String cardNumber = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("update customer set cardNumber = ? where uID =" + id,
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, cardNumber);
			int rowCount = stmt.executeUpdate();
			if (rowCount > 0) {
				System.out.println();
				System.out.println("Card edited successfully. Your card number is now: " + cardNumber );
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
	
	private void removeCard(String id) {
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("update customer set cardNumber = NULL where uID =" + id,
					Statement.RETURN_GENERATED_KEYS);
			int rowCount = stmt.executeUpdate();
			if (rowCount > 0) {
				System.out.println();
				System.out.println("Removed card successfully." );
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
	
	private void deleteAccount() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		System.out.print("Enter your password: ");
		String password = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select uName from Customer where uID =" + id +" and password ='" + password + "'",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.executeQuery(); 
			System.out.println();
			if (!rs.next()) {
				System.out.println("Account could not be found. Please try again.");
			} else {
				System.out.println("Are you sure you want to delete " + rs.getString("uName") + "'s Account?");
				while (true) {
					System.out.println();
					System.out.println("Please select an option:");
					System.out.print("[1] Yes     [2] Exit: ");
					try {
						char command = sc.nextLine().trim().charAt(0);
						if (command == '1') {
							PreparedStatement stmt1 = null;
							try {
								stmt1 = myConn.prepareStatement("delete from customer where uID =" + id,
										Statement.RETURN_GENERATED_KEYS);
								int rowCount = stmt1.executeUpdate();
								if (rowCount > 0) {
									System.out.println();
									System.out.println("Account deleted successfully." );
								}
							} catch (SQLException exc) {
								System.out.println();
								System.out.println("An error occured. Error: => " + exc.getMessage());
							} finally {
								try {
									stmt.close();
									break;
								} catch (SQLException exc) {
									System.out.println();
									System.out.println("An error occured. Error: => " + exc.getMessage());
								}
							}
						}
						else if (command == '2') {
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
			}
		} catch (SQLException exc) {
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt.close();
				accountMain();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}
	
	private void showMovie() {
		System.out.println();
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			stmt = myConn.prepareStatement("select * from movie;");
			ResultSet rs = stmt.executeQuery();
			System.out.println("---------------------------");
			System.out.println("|     Movie Showtimes     |");
			while (rs.next()) {
				String mtitle = rs.getString("title");
				String mid = rs.getString("movieID");
				if( mtitle.length() <= 25 ) {
					int titleLength = mtitle.length();
					int numOfSpaces = 25 - titleLength;
					System.out.println("|-------------------------|");
					if(numOfSpaces % 2 == 0) {
						String space = "";
						for(int i = 0; i < numOfSpaces/2; i++) {
							space = space + " ";
						}
						System.out.println("|" + space + mtitle + space + "|");
					}
					else {
						String space = "";
						for(int i = 0; i < (numOfSpaces - 1)/2; i++) {
							space = space + " ";
						}
						System.out.println("|" + space + mtitle + space + " |");
					}
					System.out.println("|-------------------------|");
				}
				else {
					System.out.println("|-------------------------|");
					System.out.println("|" + mtitle + "|");
					System.out.println("|-------------------------|");
				}
				stmt2 = myConn.prepareStatement("select * from showtime where movieID=?;");
				stmt2.setString(1, mid);
				ResultSet rs2 = stmt2.executeQuery();
				if (!rs2.isBeforeFirst())
					System.out.println("| No available showtimes. |");
				while (rs2.next()) {
					String st = rs2.getString("startTime");
					String sid = rs2.getString("showID");
					System.out.println("| " + "showID: " + sid + " | " + st + " |");
				}
			}
			System.out.println("---------------------------");
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
		System.out.println("Search showtimes");
		System.out.println("Enter movie title:");
		String title = sc.nextLine().trim();

	}

	private void resevation() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String uid = sc.nextLine().trim();
		System.out.print("Enter a showID: ");
		String sid = sc.nextLine().trim();
		System.out.print("Enter desired number of tickets: ");
		String tickets = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("insert into reservation(uID, showID, numofTicket) values(?, ?, ?);",
					Statement.RETURN_GENERATED_KEYS);
			if (sid.isEmpty() || tickets.isEmpty()) {
				System.out.println();
				System.out.println("Please provide showID & number of tickets!");
			} else {
				stmt.setString(1, uid);
				stmt.setString(2, sid);
				stmt.setString(3, tickets);
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					System.out.println();
					System.out.println("Reservation made successfully. Your reservation ID is: " + rs.getInt(1));
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

	private void cancelReservation() {
		System.out.println();
		System.out.print("Enter a reservation ID: ");
		String rid = sc.nextLine().trim();

		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("delete from Reservation where rid= ? ");
			if (rid.isEmpty()) {
				System.out.println();
				System.out.println("Please enter reservation ID:");
			} else {
				stmt.setString(1, rid);
				int bool = stmt.executeUpdate();
				if (bool == 0) {
					System.out.println();
					System.out.println("unable to delete\n");
				}
				else {
					System.out.println();
					System.out.printf("Reservation %s cancelled successfully!\n", rid);
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

}
