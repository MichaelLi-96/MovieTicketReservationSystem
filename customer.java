import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import com.mysql.cj.util.StringUtils;

//Customer constructor class
public class customer {
	static Scanner sc = new Scanner(System.in);
	dbconnection mt = new dbconnection();
	Connection myConn = mt.myConn;

	//Establish Database Connection
	public class dbconnection {
		public Connection myConn;

		public dbconnection() {
			try {
				myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/TicketReservation?useSSL=true",
						"root",
						//put in mysql password here
						//Michael's password: Mi19Li96
						//Vivian's password: currybreadchai
						"Mi19Li96");
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
	}
	
	// Main menu for customers
	//
	public void customerMain() {
		while (true) {
			System.out.println("\nPlease select a customer option:");
			System.out.print("\n[1] Account     \n[2] Movies     \n[3] See Purchases    \n[4] Logout\n\n");
			try {
				char command = sc.nextLine().trim().charAt(0);

				//View Account
				if (command == '1') {
					accountMain();
				}

				//View Movies
				else if (command == '2') {
					moviesMain();
				}

				//View Purchases
				else if (command == '3') {
					purchasesMain();
				}

				//Quit program
				else if (command == '4') {
					System.out.println("\nLogged out of customer.\n");
					return;
				}

				//Error message for invalid input
				else {
					System.out.println();
					System.out.println("Invalid command.");
				}

			} catch (Exception e) {
				System.out.println();
				System.out.println("An error occurred. Please try again.");
			}
			return;
		}
	}
	
	// Account menu
	//
	private void accountMain() {
		while (true) {
			System.out.println("\nPlease select an account option:");
			System.out.print("\n[1] Create an Account     \n[2] Edit Your Account     \n[3] Delete Your Account     \n[4] Back To Customer Options\n\n");
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
	
	// Movie menu
	//
	private void moviesMain() {
		while (true) {
			System.out.println("\nPlease select a Showtime option:");
			System.out.print("\n[1] Show Movies     \n[2] Make Reservation     \n[3] Cancel Reservation     \n[4] Rate a Movie    \n[5] Back to Customer Options\n\n");
			try {

				//Show Movies
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					showMovieOptions();
				}
				//Make Reservation
				else if (command == '2') {
				 reservation();
				}
				//Cancel Reservation
				else if (command == '3') {
					cancelReservation();
				}
				//Rate Movie
				else if (command == '4') {
					rateMovie();
				}
				//Back
				else if (command == '5') {
					break;
				}

				//Invalid Command
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
	
	// Purchases menu
	//
	private void purchasesMain() {
		while (true) {
			System.out.println("\nPlease select an option:");
			System.out.println("[1] Show Purchase History     \n[2] Show Amount Of Tickets Bought     \n[3] Show Total Amount Of Money Spent \n[4] Back to Customer Options");
			try {
				//Show amount of tickets bought
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					showPurchaseHistory();
				}
				//Show total amount of money spent
				else if (command == '2') {
					showAmountOfTickets();
				}
				//Back
				else if (command == '3') {
					showTotalAmountOfMoneySpent();
				}
				//Quit
				else if (command == '4') {
					break;
				}
				//Invalid Input
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
	
	// Register a customer
	//
	private void register() {
		//Input Customer.name
		System.out.print("\nEnter your name: ");
		String name = sc.nextLine().trim();
		//Input Customer.password
		System.out.print("Enter a password: ");
		String password = sc.nextLine().trim();
		//Input Customer.age
		System.out.print("Enter your age: ");
		String age = sc.nextLine().trim();
			//Check if age is valid
		if(!(StringUtils.isStrictlyNumeric(age) && age.length() >= 2 && Integer.parseInt(age)>13)) {
			System.out.println();
			System.out.println("Input for customer age was denied. Minimum age is 13. Please try again.");
		}
		else {
			PreparedStatement stmt = null;
			System.out.println();
			//Insert user into Customer table
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
					
					//Store results
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
	}
	
	// Sign in to access account options menu
	//
	private void editAccountOptions() {
		//Input Customer.ID
		System.out.print("\nEnter your ID: ");
		String id = sc.nextLine().trim();
			//Check if Customer ID is valid
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
			System.out.println();
			System.out.println("Input for customer ID was denied. Customer ID consists of 4 characters. Please try again.");
		}
		//Input Customer.password
		else {
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
						System.out.println("[1] Edit Name     \n[2] Edit Password     \n[3] Edit Age     \n[4] Set Credit Card Number   \n[5] Remove Credit Card     \n[6] Back To Account Options     \n[7] Exit");
						try {
							//Edit Customer.name
							char command = sc.nextLine().trim().charAt(0);
							if (command == '1') {
								editName(id);
							}
							//Edit Customer.password
							else if (command == '2') {
								editPassword(id);
							}
							//Edit Customer.age
							else if (command == '3') {
								editAge(id);
							}

							//Edit Customer.card
							else if (command == '4') {
								setCard(id);
							}
							//Delete Customer.card
							else if (command == '5') {
								removeCard(id);
							}
							//Back
							else if (command == '6') {
								break;
							}
							//Quit
							else if (command == '7') {
								System.out.println("\nGoodbye.");
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
	}
	
	// Customer can edit their name
	//
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
	
	// Customer can edit their password
	//
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
	
	// Customer can edit their age
	//
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
	
	// Customer can set their card
	//
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
	
	// Customer can remove their card
	//
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
	
	// Customer can delete their account
	//
	private void deleteAccount() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
			System.out.println();
			System.out.println("Inputted customer ID was not accepted. Please try again.");
		}
		else {
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
					System.out.println("Are you sure you want to delete " + rs.getString("uName") + "'s Account?");
					while (true) {
						System.out.print("\n[1] Yes     [2] Cancel\n");
						try {
							String command = sc.nextLine().trim();
							if (command.equals("1")) {
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
		accountMain();
	}
	
	// Movie options menu
	//
	private void showMovieOptions() {
		while (true) {
			System.out.println("\nPlease select an option:");
			System.out.println("\n[1] Show All Showtimes For A Movie \n[2] Show All Movies Alphabetically     \n[3] Show All Movies By Earliest Showtimes  \n[4] Show All Movies By Latest Showtime     \n[5] Back To Showtime Options\n\n");
			try {
				char command = sc.nextLine().trim().charAt(0);
				if (command == '1') {
					showAllShowtimesOfOneMovie();
				}
				else if (command == '2') {
					currentShow();
					//showAllMoviesAlphabetically();
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
				else {
					System.out.println("\nInvalid command.");
				}
			} catch (Exception e) {
				System.out.println("\nAn error occurred. Please try again.");
			}
		}
		moviesMain();
	}
	
	// Show all the show times of just one movie
	//
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

		private void currentShow() {
		System.out.println();
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
	
	// Show all the movies in our database alphabetically
	//
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
	
	// Show all the movies in our database sorted by earliest show time
	//
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
	
	// Show all the movies in our database sorted by latest show time
	//
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
	
	// Customer can make a reservation for a movie
	//
	private void reservation() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
			System.out.println();
			System.out.println("Inputted customer ID was not accepted. Please try again.");
		}
		else {
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
	}
	
	// Customer can cancel a reservation for a movie
	//
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
				System.out.print("Enter your ID: ");
				String id = sc.nextLine().trim();
				if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
					System.out.println();
					System.out.println("Inputted customer ID was not accepted. Please try again.");
				}
				else {
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
	
	// Customer can rate a movie, can only rate that movie once
	//
	private void rateMovie() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String uID = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(uID) && uID.length() == 4)) {
			System.out.println();
			System.out.println("Inputted customer ID was not accepted. Please try again.");
		}
		else {
			System.out.print("Enter your password: ");
			String password = sc.nextLine().trim();
			PreparedStatement stmt1 = null;
			PreparedStatement stmt2 = null;
			PreparedStatement stmt3 = null;
			PreparedStatement stmt4 = null;
			try {
				stmt1 = myConn.prepareStatement("select uName from Customer where uID =" + uID +" and password ='" + password + "';",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt1.executeQuery(); 
				System.out.println();
				if (!rs1.next()) {
					System.out.println("Account could not be found. Please try again.");
				} 
				else {
					System.out.print("Enter a movie title: ");
					String title = sc.nextLine().trim();
					stmt2 = myConn.prepareStatement("select * from movie where title ='" + title + "';");
					ResultSet rs2 = stmt2.executeQuery();
					if(!rs2.next()) {
						System.out.println("That movie does not exist in our library.");
					}
					else {
						String movieID = rs2.getString("movieID");
						String trueTitle = rs2.getString("title");
						stmt3 = myConn.prepareStatement("select * from Rating where uID =" + uID +" and movieID =" + movieID + ";",
								Statement.RETURN_GENERATED_KEYS);
						ResultSet rs3 = stmt3.executeQuery(); 
						System.out.println();
						if (rs3.next()) {
							System.out.println("You can only rate a movie once. Cannot rate again.");
						} 
						else {
							System.out.print("Rate the movie from 0 - 10: ");
							String rating = sc.nextLine().trim();
							if(StringUtils.isStrictlyNumeric(rating) && rating.length() <= 2) {
								if(Integer.parseInt(rating) >= 0 && Integer.parseInt(rating) <= 10) {
									try {
										stmt4 = myConn.prepareStatement("insert into rating(movieID, uID, rating) values(?, ?, ?);",
												Statement.RETURN_GENERATED_KEYS);
										stmt4.setString(1, movieID);
										stmt4.setString(2, uID);
										stmt4.setString(3, rating);
										int rowCount = stmt4.executeUpdate();
										System.out.println();
										if (rowCount > 0) {
											System.out.println("You rated the movie \"" + trueTitle + "\" " + rating + " out of 10." );
										}
										else {
											System.out.println("The rating was not successful. Please try again.");
										}
									} catch (SQLException exc) {
										System.out.println();
										System.out.println("An error occured. Error: => " + exc.getMessage());
									} finally {
										try {
											stmt4.close();
										} catch (SQLException exc) {
											System.out.println();
											System.out.println("An error occured. Error: => " + exc.getMessage());
										}
									}
								}
								else {
									System.out.println();
									System.out.println("The rating was not successful. It must be a whole number between 0 - 10.");
								}
							}
							else {
								System.out.println();
								System.out.println("The rating was not successful. It must be a whole number between 0 - 10.");
							}
						}
						stmt3.close();
					}
					stmt2.close();
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
	}
	
	// Show the customer his/her purchase(reservation) history
	//
	private void showPurchaseHistory() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
			System.out.println();
			System.out.println("Inputted customer ID was not accepted. Please try again.");
		}
		else {
			System.out.print("Enter your password: ");
			String password = sc.nextLine().trim();
			PreparedStatement stmt1 = null;
			PreparedStatement stmt2 = null;
			PreparedStatement stmt3 = null;
			try {
				stmt1 = myConn.prepareStatement("select total, uName, password from (select uID, SUM(numOfTicket) as 'total' from Reservation group by uID) as totalTable, Customer where totalTable.uID = Customer.uID and totalTable.uID =" + id + ";",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt1.executeQuery();
				System.out.println();
				if (rs1.next() && rs1.getString("password").equals(password)) {
					stmt2 = myConn.prepareStatement("select * from Reservation where uID = ? order by resDate DESC;",
							Statement.RETURN_GENERATED_KEYS);
					stmt2.setString(1, id);
					ResultSet rs2 = stmt2.executeQuery();
					System.out.println("---------------------------");
					System.out.println("|  Your Purchase History  |");
					while (rs2.next()) {
						String numberOfTickets = rs2.getString("numOfTicket");
						String dateBought = rs2.getString("resDate");
						String showtimeID = rs2.getString("showID");
						stmt3 = myConn.prepareStatement("select * from Showtime, Movie where Showtime.movieID = Movie.movieID and showID = ?;",
								Statement.RETURN_GENERATED_KEYS);
						stmt3.setString(1, showtimeID);
						ResultSet rs3 = stmt3.executeQuery();
						if(rs3.next()) {
							String mtitle = rs3.getString("title");
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
						}
						else {
							System.out.println();
							System.out.println("Movie title could not be found.");
						}
						
						int numTicketLength = numberOfTickets.length();
						int numOfSpaces = 4 - numTicketLength;
						if(numOfSpaces % 2 == 0) {
							String space = "";
							for(int i = 0; i < numOfSpaces/2; i++) {
								space = space + " ";
							}
							System.out.println("|  Tickets Purchased: " + numberOfTickets + space + " |");
						}
						else {
							String space = "";
							for(int i = 0; i < (numOfSpaces - 1)/2; i++) {
								space = space + " ";
							}
							System.out.println("|  Tickets Purchased: " + numberOfTickets + space + "  |");
						}
						
						System.out.println("|   " + dateBought + "   |");
					}
				}
				else {
					System.out.println("Account could not be found. Please try again.");
				}
				System.out.println("---------------------------");
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			} finally {
				try {
					stmt1.close();
					stmt2.close();
				} catch (SQLException exc) {
					System.out.println("An error occured. Error: => " + exc.getMessage());
				}
			}
		}
	}
	
	// Show the customer how many tickets they have bought in total
	//
	private void showAmountOfTickets() {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
			System.out.println();
			System.out.println("Inputted customer ID was not accepted. Please try again.");
		}
		else {
			System.out.print("Enter your password: ");
			String password = sc.nextLine().trim();
			PreparedStatement stmt = null;
			try {
				stmt = myConn.prepareStatement("select total, uName, password from (select uID, SUM(numOfTicket) as 'total' from Reservation group by uID) as totalTable, Customer where totalTable.uID = Customer.uID and totalTable.uID =" + id + ";",
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
	}
	
	// Show the customer how much money they have spent on tickets in total
	//
	private void showTotalAmountOfMoneySpent() throws ParseException {
		System.out.println();
		System.out.print("Enter your ID: ");
		String id = sc.nextLine().trim();
		if(!(StringUtils.isStrictlyNumeric(id) && id.length() == 4)) {
			System.out.println();
			System.out.println("Inputted customer ID was not accepted. Please try again.");
		}
		else {
			System.out.print("Enter your password: ");
			String password = sc.nextLine().trim();
			PreparedStatement stmt1 = null;
			PreparedStatement stmt2 = null;
			PreparedStatement stmt3 = null;
			PreparedStatement stmt4 = null;
			try {
				stmt1 = myConn.prepareStatement("select * from Reservation, Customer where Reservation.uID = Customer.uID and Reservation.uID =" + id + ";",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmt1.executeQuery();
				stmt3 = myConn.prepareStatement("select price from Ticket where ticketType = 'AM';",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs3 = stmt3.executeQuery();
				stmt4 = myConn.prepareStatement("select price from Ticket where ticketType = 'PM';",
						Statement.RETURN_GENERATED_KEYS);
				ResultSet rs4 = stmt4.executeQuery();
				System.out.println();
				if(rs1.next() && rs3.next() && rs4.next() && rs1.getString("password").equals(password)) {
					String name = rs1.getString("uName");
					rs1.beforeFirst();
					double total = 0;
					while (rs1.next()) {
						int numberOfTicketsPerShowtime = rs1.getInt("numOfTicket");
						stmt2 = myConn.prepareStatement("select * from Showtime where showID = " + rs1.getInt("showID") + ";" ,
								Statement.RETURN_GENERATED_KEYS);
						ResultSet rs2 = stmt2.executeQuery();
						if(rs2.next()) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
							if(dateFormat.parse(dateFormat.format(rs2.getTime("startTime"))).after(dateFormat.parse("00:00:00")) && 
							   dateFormat.parse(dateFormat.format(rs2.getTime("startTime"))).before(dateFormat.parse("12:00:00"))) {
									double totalCostPerShowtime = numberOfTicketsPerShowtime * rs3.getDouble("price");
									total += totalCostPerShowtime;
							}
							else if(dateFormat.parse(dateFormat.format(rs2.getTime("startTime"))).after(dateFormat.parse("12:00:00")) && 
									dateFormat.parse(dateFormat.format(rs2.getTime("startTime"))).before(dateFormat.parse("24:00:00"))) {{
										double totalCostPerShowtime = numberOfTicketsPerShowtime * rs4.getDouble("price");
										total += totalCostPerShowtime;
									}
							}
						}				
					}
					System.out.println(name + " has spent " + total + " dollars on movie tickets.");
					stmt2.close();
				}
				else {
					System.out.println("Account could not be found. Please try again.");
				}
			} catch (SQLException exc) {
				System.out.println("An error occured. Error: => " + exc.getMessage());
			} finally {
				try {
					stmt1.close();
					stmt3.close();
					stmt4.close();
				} catch (SQLException exc) {
					System.out.println("An error occured. Error: => " + exc.getMessage());
				}
			}
		}
	}
	
}
