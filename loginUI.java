import java.util.Scanner;

public class loginUI {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		mainMenu();
	}

	public static void mainMenu() {
		while (true) {
			System.out.println("Welcome to movie ticket reservation system!");
			System.out.println("Please select an option:");
			System.out.print("[1] Customer     [2] Admin     [3] Exit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);

				if (command == '1')
					customerlogin();
				else if (command == '2')
					adminLogin();
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

	private static void adminLogin() {
		admin am = new admin();
		am.adminMain();
	}

	private static void customerlogin() {
		customer cm = new customer();
		cm.customerMain();
	}

}
