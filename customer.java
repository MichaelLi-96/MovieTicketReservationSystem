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
			System.out.print("[1] Account     [2] Showtimes     [3] See Purchases     [4] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					accountMain();
				}
				else if (command == '2') {
					showtimeMain();
				}
				else if (command == '3') {
					purchasesMain();
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
	
	private void accountMain() {
		while (true) {
			System.out.println();
			System.out.println("Please select an account option:");
			System.out.print("[1] Create an Account     [2] Edit Your Account     [3] Delete Your Account     [4] Back To Customer Options     [5] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					register();
				}
				else if (command == '2') {
					editAccountOptions();
				}
				else if (command == '3') {
					deleteAccount();
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
		customerMain();
	}

	private void showtimeMain() {
		while (true) {
			System.out.println();
			System.out.println("Please select a showtime option:");
			System.out.print("[1] Show Movies     [2] Make Reservation     [3] Cancel Reservation     [4] Back to Customer Options     [5] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					showMovieOptions();
				}
				else if (command == '2') {
					resevation();
				}
				else if (command == '3') {
					cancelReservation();
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
		customerMain();
	}
	
	private void purchasesMain() {
		while (true) {
			System.out.println();
			System.out.println("Please select an option:");
			System.out.print("[1] Show Amount Of Tickets Bought     [2] Show Total Amount Of Money Spent     [3] Back to Customer Options     [4] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					showAmountOfTickets();
				}
				else if (command == '2') {
					showTotalAmountOfMoneySpent();
				}
				else if (command == '3') {
					break;
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
		customerMain();
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
			stmt = myConn.prepareStatement("INSERT into customer(uName, password, age) values(?,?,?);",
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
					System.out.println("Registered successfully. Your ID is: " + rs.getInt(1));
				}
				else {
					System.out.println("Register was not successful.");
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
	
	private void editAccountOptions() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		System.out.print("Enter your password: ");
		String password = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select uName from Customer where uID =" + id +" and password ='" + password + "';",
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
					System.out.println("[1] Edit Name     [2] Edit Password     [3] Edit Age     [4] Set Card");
					System.out.print("[5] Remove Card     [6] Back To Account Options     [7] Exit: ");
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
			stmt = myConn.prepareStatement("update customer set uName = ? where uID =" + id + ";",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			int rowCount = stmt.executeUpdate();
			System.out.println();
			if (rowCount > 0) {
				System.out.println("Name edited successfully. Your name is now: " + name );
			}
			else {
				System.out.println("Name was not edited successfully. Your name is: " + name );
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
			stmt = myConn.prepareStatement("update customer set password = ? where uID =" + id + ";",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, password);
			int rowCount = stmt.executeUpdate();
			System.out.println();
			if (rowCount > 0) {
				System.out.println("Password edited successfully. Your password is now: " + password );
			}
			else {
				System.out.println("Password was not edited successfully. Your password is: " + password );
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
			stmt = myConn.prepareStatement("update customer set age = ? where uID =" + id + ";",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, age);
			int rowCount = stmt.executeUpdate();
			System.out.println();
			if (rowCount > 0) {
				System.out.println("Age edited successfully. Your age is now: " + age );
			}
			else {
				System.out.println("Age was not edited successfully. Your age is: " + age );
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
			stmt = myConn.prepareStatement("update customer set cardNumber = ? where uID =" + id + ";",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, cardNumber);
			int rowCount = stmt.executeUpdate();
			System.out.println();
			if (rowCount > 0) {
				System.out.println("Card edited successfully. Your card number is now: " + cardNumber );
			}
			else {
				System.out.println("Card was not edited successfully. Your card number is: " + cardNumber);
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
			stmt = myConn.prepareStatement("update customer set cardNumber = NULL where uID =" + id + ";",
					Statement.RETURN_GENERATED_KEYS);
			int rowCount = stmt.executeUpdate();
			System.out.println();
			if (rowCount > 0) {
				System.out.println("Removed card successfully." );
			}
			else {
				System.out.println("Card was not successfully.");
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
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			stmt1 = myConn.prepareStatement("select uName from Customer where uID =" + id +" and password ='" + password + "';",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt1.executeQuery(); 
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
							try {
								stmt2 = myConn.prepareStatement("delete from customer where uID =" + id + ";",
										Statement.RETURN_GENERATED_KEYS);
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
				stmt1.close();
				accountMain();
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}
	
	private void showMovieOptions() {
		while (true) {
			System.out.println();
			System.out.println("Please select an option:");
			System.out.println("[1] Show All Showtimes For A Movie     [2] Show All Movies Alphabetically     [3] Show All Movies By Earliest Showtime");
			System.out.print("[4] Show All Movies By Latest Showtime     [5] Back To Showtime Options     [4] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					showAllShowtimesOfOneMovie();
				}
				else if (command == '2') {
					showAllMoviesAlphabetically();
				}
				else if (command == '3') {
					showAllMoviesByShowtimeAsc();
				}
				else if (command == '4') {
					showAllMoviesByShowtimeDesc();
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
		showtimeMain();
	}
	
	private void showAllShowtimesOfOneMovie() {
		System.out.println();
		System.out.print("Enter a movie title: ");
		String title = sc.nextLine().trim();
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			stmt1 = myConn.prepareStatement("select * from movie where title ='" + title + "';");
			ResultSet rs = stmt1.executeQuery();
			if(!rs.next()) {
				System.out.println("That movie does not exist in our library.");
			}
			else {
				System.out.println("---------------------------");
				System.out.println("|     Movie Showtimes     |");
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
				stmt2 = myConn.prepareStatement("select * from showtime where movieID = ? order by showDate, startTime;");
				stmt2.setString(1, mid);
				rs = stmt2.executeQuery();
				if (!rs.isBeforeFirst()) {
					System.out.println("| No available showtimes. |");
				}
				while (rs.next()) {
					String sid = rs.getString("showID");
					String showDate = rs.getString("showDate");
					String startTime = rs.getString("startTime");
					if (rs.isFirst()) {
						System.out.println("|      showID: " + sid + "       |");
						System.out.println("|  " + showDate + " | " + startTime + "  |");
					}
					else {
						System.out.println("|                         |");
						System.out.println("|      showID: " + sid + "       |");
						System.out.println("|  " + showDate + " | " + startTime + "  |");
					}
				}
				System.out.println("---------------------------");
				stmt2.close();
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
	
	private void showAllMoviesAlphabetically() {
		System.out.println();
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			stmt1 = myConn.prepareStatement("select * from movie order by title;");
			ResultSet rs = stmt1.executeQuery();
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
				stmt2 = myConn.prepareStatement("select * from showtime where movieID = ? order by showDate, startTime;");
				stmt2.setString(1, mid);
				ResultSet rs2 = stmt2.executeQuery();
				if (!rs2.isBeforeFirst())
					System.out.println("| No available showtimes. |");
				while (rs2.next()) {
					String sid = rs2.getString("showID");
					String showDate = rs2.getString("showDate");
					String startTime = rs2.getString("startTime");
					if (rs2.isFirst()) {
						System.out.println("|      showID: " + sid + "       |");
						System.out.println("|  " + showDate + " | " + startTime + "  |");
					}
					else {
						System.out.println("|                         |");
						System.out.println("|      showID: " + sid + "       |");
						System.out.println("|  " + showDate + " | " + startTime + "  |");
					}
				}
			}
			System.out.println("---------------------------");
			stmt2.close();
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
	
	private void showAllMoviesByShowtimeAsc() {
		System.out.println();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select * from movie, showtime where movie.movieID = showtime.movieID order by showDate ASC, startTime ASC;");
			ResultSet rs = stmt.executeQuery();
			System.out.println("---------------------------");
			System.out.println("|     Movie Showtimes     |");
			while (rs.next()) {
				String mtitle = rs.getString("title");
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
				
				String sid = rs.getString("showID");
				String showDate = rs.getString("showDate");
				String startTime = rs.getString("startTime");
				System.out.println("|      showID: " + sid + "       |");
				System.out.println("|  " + showDate + " | " + startTime + "  |");
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
	
	private void showAllMoviesByShowtimeDesc() {
		System.out.println();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select * from movie, showtime where movie.movieID = showtime.movieID order by showDate DESC, startTime DESC;");
			ResultSet rs = stmt.executeQuery();
			System.out.println("---------------------------");
			System.out.println("|     Movie Showtimes     |");
			while (rs.next()) {
				String mtitle = rs.getString("title");
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
				
				String sid = rs.getString("showID");
				String showDate = rs.getString("showDate");
				String startTime = rs.getString("startTime");
				System.out.println("|      showID: " + sid + "       |");
				System.out.println("|  " + showDate + " | " + startTime + "  |");
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
	
	private void resevation() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		System.out.print("Enter your password: ");
		String password = sc.nextLine().trim();
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			stmt1 = myConn.prepareStatement("select uName from Customer where uID =" + id +" and password ='" + password + "';",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt1.executeQuery(); 
			System.out.println();
			if (!rs.next()) {
				System.out.println("Account could not be found. Please try again.");
			} 
			else {
				System.out.print("Enter a showID: ");
				String sId = sc.nextLine().trim();
				System.out.print("Enter desired number of tickets: ");
				String tickets = sc.nextLine().trim();
				try {
					stmt2 = myConn.prepareStatement("insert into reservation(uID, showID, numOfTicket) values(?, ?, ?);",
							Statement.RETURN_GENERATED_KEYS);
					if (sId.isEmpty() || tickets.isEmpty()) {
						System.out.println();
						System.out.println("Please provide showID & number of tickets!");
					} else {
						stmt2.setString(1, id);
						stmt2.setString(2, sId);
						stmt2.setString(3, tickets);
						stmt2.executeUpdate();
						rs = stmt2.getGeneratedKeys();
						System.out.println();
						if (rs.next()) {
							System.out.println("Reservation made successfully. Your reservation ID is: " + rs.getInt(1));
						}
						else {
							System.out.println("Reservation was unsuccessful.");
						}
					}
				} catch (SQLException exc) {
					System.out.println();
					System.out.println("An error occured. Error: => " + exc.getMessage());
				} finally {
					try {
						stmt2.close();
					} catch (SQLException exc) {
						System.out.println();
						System.out.println("An error occured. Error: => " + exc.getMessage());
					}
				}
			}
		} catch (SQLException exc) {
			System.out.println();
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt1.close();
			} catch (SQLException exc) {
				System.out.println();
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}

	private void cancelReservation() {
		System.out.println();
		System.out.print("Enter the reservation ID: ");
		String rId = sc.nextLine().trim();
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		try {
			stmt1 = myConn.prepareStatement("select * from Reservation, Customer where Reservation.uID = Customer.uID and rID =" + rId + ";",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt1.executeQuery(); 
			System.out.println();
			if (!rs.next()) {
				System.out.println("Reservation could not be found. Please try again.");
			} 
			else {
				System.out.print("What is your ID: ");
				String id = sc.nextLine().trim();
				System.out.print("What is your password: ");
				String password = sc.nextLine().trim();
				System.out.print("What is the showID: ");
				String sId = sc.nextLine().trim();
				if(rs.getString("uID").equals(id) && rs.getString("password").equals(password) && rs.getString("showID").equals(sId)) {
					try {
						stmt2 = myConn.prepareStatement("delete from Reservation where rID= ?;");
						stmt2.setString(1, rId);
						int rowCount = stmt2.executeUpdate();
						System.out.println();
						if (rowCount > 0) {
							System.out.println("Reservation: " + rId + " was cancelled successfully!");
						}
						else {
							System.out.println("Reservation: " + rId + " was not cancelled successfully!");
						}
					} catch (SQLException exc) {
						System.out.println();
						System.out.println("An error occured. Error: => " + exc.getMessage());
					} finally {
						try {
							stmt2.close();
						} catch (SQLException exc) {
							System.out.println();
							System.out.println("An error occured. Error: => " + exc.getMessage());
						}
					}
				}
				else {
					System.out.println("Incorrect customer ID and/or password and/or show ID for the reservation. Please try again.");
				}
			}
		} catch (SQLException exc) {
			System.out.println();
			System.out.println("An error occured. Error: => " + exc.getMessage());
		} finally {
			try {
				stmt1.close();
			} catch (SQLException exc) {
				System.out.println();
				System.out.println("An error occured. Error: => " + exc.getMessage());
			}
		}
	}
	
	private void showAmountOfTickets() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		System.out.print("Enter your password: ");
		String password = sc.nextLine().trim();
		PreparedStatement stmt = null;
		try {
			stmt = myConn.prepareStatement("select total, uName, password from (select uID, SUM(numOfTicket) as 'total' from Reservation group by uID) as totalTable, customer where totalTable.uID = customer.uID and totalTable.uID =" + id + ";",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.executeQuery();
			System.out.println();
			if (rs.next() && rs.getString("password").equals(password)) {
				System.out.println(rs.getString("uName") + " has bought " + rs.getInt("total") + " tickets.");
			}
			else {
				System.out.println("Account could not be found. Please try again.");
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
	
	private void showTotalAmountOfMoneySpent() {
		
	}
}
