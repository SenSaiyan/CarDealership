package dealership;
import java.util.Scanner;
//import java.sql.*;

public class Employee extends User{

	public Employee(String name, String pass) {
		super(name, pass);
	}
	
//	public static void main(String[] args){
//		Scanner scan = new Scanner(System.in); 
//		System.out.println("Welcome " + name + ", how can I assist you today?");
//		System.out.println("Press 1 to View Lot, Press 2 to View Offers");
//		String select = scan.nextLine();
//		if (select == "1"){
//			viewLot();
//		} else if (select == "2"){
//			offers();
//		} else {
//			System.out.println("Invalid input!");
//			select = scan.nextLine();
//		}
//	}

	public String[] viewLot(){
		String[] options = {"Press 1 add car to lot", "Press 2 to remove Car from Lot"};
		return options;
	}
	
	public String[] offers(){
		String[] options = {"Press 1 to approve offer", "Press 2 to reject offer"};
		return options;
	}
}
