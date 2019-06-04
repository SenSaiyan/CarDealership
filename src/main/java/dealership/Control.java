package dealership;

import java.util.Scanner;

public class Control {
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in); 
		System.out.println("Enter Username");
		String name = scan.nextLine();
		System.out.println("Enter Password");
		String pass = scan.nextLine();
		scan.close();
		Queries.login(name, pass);
	}
}
