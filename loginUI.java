
import java.util.Scanner;

public class loginUI {

	static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {
		doMainMenu();

	}

	public static void doMainMenu() {

		while (true) {
			System.out.println("Welcome to movie ticket reservation system!");
			System.out.println("Please select options:");
			System.out.print("[1]customer   [2]admin  [3]quit: ");
			try {
				char command = sc.nextLine().trim().charAt(0);

				if (command == '1')
					customerlogin();
				else if (command == '2')
					adminLogin();
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

	private static void adminLogin() {
		// TODO Auto-generated method stub
		//System.out.println("admin");
		admin am = new admin();
		am.adminMain();

	}

	private static void customerlogin() {
		// TODO Auto-generated method stub
	//	System.out.println("customer");
		customer cm = new customer();
		cm.customMain();

	}

}
