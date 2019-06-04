package dealership;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import dao.CarDAO;
import dao.CarOracle;
import dao.UserDAO;
import dao.UserOracle;

import com.revature.utils.ConnectionUtil;

import dealership.Car;
import dealership.User;
import dealership.Customer;
import dealership.Employee;

public class Queries {
	public static ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	public static Connection conn = cu.getConnection(); //sql library
	
	
	public Queries(){
		
	}
	
	public static void login(String name, String pass){
		String q = "select * from dealerusers where username=? and pass=?";
		String accountType = "";
		try{
			PreparedStatement statement = conn.prepareStatement(q);
			statement.setString(1, name);
			statement.setString(2, pass);
			ResultSet users = statement.executeQuery();
			if (users.next() && users.getRow() > 0) {
				accountType = users.getString(4);
			} 
			if (accountType.equals("employee")) {
				Employee emp = new Employee(name, pass);
				options(emp);
			} else if (accountType.equals("customer")) {
				Customer cust = new Customer(name, pass);
				options(cust);
			} else {
				Customer cust = new Customer(name, pass);
				makeAccount(cust, name, pass);
			}
		} catch(SQLException except){
			Customer cust = new Customer(name, pass);
			makeAccount(cust, name, pass);
		}
	}
	
	public static void makeAccount(Customer user, String name, String pass) {
		Scanner scan = new Scanner(System.in); 
		System.out.println("No user found. Create one with current credentials? "
				+ "Press 1 for yes, 2 to  return to login");
		String input = scan.nextLine();
		int choice = Integer.parseInt(input);
		switch(choice) {
		case 1:
			System.out.println("test");
			Customer temp = new Customer(name, pass);
			try {
				String q2 = "insert into users (userid, username, pass, usertype) values(?, ?, ?, 'customer')";					
				PreparedStatement statement = conn.prepareStatement(q2);
				statement.setInt(1, getUserId(user));
				System.out.println("userid "+getUserId(user));
				statement.setString(2, name);
				System.out.println("name "+name);
				statement.setString(3, pass);
				System.out.println("pass "+pass);
				statement.executeUpdate();
				//System.out.println("help me");
				options(user);
			} catch(Exception e){
				System.out.println("Something went wrong when creating new user");
			}				
		case 2:
			runAccount();
		}
	}
	
	public static void options(User user) {
		int userid = getUserId(user);
		System.out.println("userid "+userid);
		
		Scanner scan = new Scanner(System.in);
		String carid_in = "";
		int carid = 0;
		if (user.getClass() == Customer.class) {				
			System.out.println("Press 1 to view lot, 2 to make offer, 3 to view owned cars, 4 to view owed payments, 5 to make payment, 6 to log out");
			String input = scan.nextLine();
			int choice = Integer.parseInt(input);
			switch(choice) {
			case 1:
				System.out.println("Make sure to remember the four digit id number of the car you want to make offers on!");
				viewLot(user);
			case 2:
				System.out.println("Enter 4-digit car ID number");
				carid_in = scan.nextLine();
				carid = Integer.parseInt(carid_in);
				System.out.println("Enter price negotiated");
				String price_in = scan.nextLine();
				int price = Integer.parseInt(price_in);
				System.out.println("Enter desired loan duration in number of months");
				String leninmonths_in = scan.nextLine();
				int leninmonths = Integer.parseInt(leninmonths_in);
				makeOffer(user, carid, userid, leninmonths, price);
			case 3:
				showOwned(user, getUserId(user));
				//options(user);
			case 4:
				System.out.println("Enter 4-digit car ID number");
				carid_in = scan.nextLine();
				carid = Integer.parseInt(carid_in);
				viewRemainingPayments(user, carid, getUserId(user));
			case 5:
				System.out.println("Enter 4-digit car ID number");
				carid_in = scan.nextLine();
				carid = Integer.parseInt(carid_in);
				makePayment(user, carid, userid);
			case 6:
				runAccount();
			default:
				System.out.println("Invalid choice");
			}
		} else if (user.getClass() == Employee.class) {
			System.out.println("Press 1 to View Lot, Press 2 to View Offers, Press 3  to respond to offer, Press 4 to add a car to the lot, "
					+ "Press 5 to remove car from lot, Press 6 to log out");
			String input = scan.nextLine();
			int choice = Integer.parseInt(input);
			switch(choice) {
			case 1:
				System.out.println("Make sure to remember the four digit id number of the car you want to make offers on!");
				viewLot(user);
			case 2:
				System.out.println("Viewing all offers on all cars");
				viewAllOffers(user);
			case 3:
				System.out.println("Enter 4-digit car ID number");
				carid_in = scan.nextLine();
				carid = Integer.parseInt(carid_in);
				System.out.println("Enter ID of customer whose offer you want to accept");
				String userid_in = scan.nextLine();
				int UID_input = Integer.parseInt(userid_in);
				System.out.println("Press 1 to accept offer, 2 to reject");
				String rejectoraccept_in = scan.nextLine();
				int rejectoraccept = Integer.parseInt(rejectoraccept_in);
				respondToOffer(user, carid, userid, rejectoraccept);
			case 4:
				//Car(String make, String model, String year, String mileage, String color, int carid, int stickerPrice) 
				System.out.println("Enter car color");
				String newColor = scan.nextLine();
				System.out.println("Enter car make");
				String newMake = scan.nextLine();
				System.out.println("Enter car model");
				String newModel = scan.nextLine();
				System.out.println("Enter car year");
				String newYear = scan.nextLine();
				System.out.println("Enter car mileage");
				String newMileage = scan.nextLine();
				System.out.println("Enter price");
				int price = Integer.parseInt(scan.nextLine());
				Car car = new Car(newMake, newModel, newYear, newMileage, newColor);
				addCar(user, car);
			case 5:
				System.out.println("Enter 4-digit ID of car you want to remove");
				int rm_carid = Integer.parseInt(scan.nextLine());
				removeCar(user, rm_carid);
			case 6:
				runAccount();
			default:
				System.out.println("Invalid choice");
				scan.close();
			}
		}
	}
	
	public static int getUserId(User user) {
		int userid = 0;
		try {
			PreparedStatement statement = conn.prepareStatement("select userid from dealerusers where username=?");
			statement.setString(1, user.getName());
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				userid = rs.getInt("userid");
			}
		} catch(SQLException except) {
			System.out.println("Something went wrong when getting userid");
		}
		return userid;
	}
	
	public static ArrayList<Customer> userList(){
		ArrayList<Customer> myList = new ArrayList<Customer>();
		String q = "select * from dealerusers where usertype='customer'";
		try {
			PreparedStatement statement = conn.prepareStatement(q);
			ResultSet users = statement.executeQuery();
			while(users.next()) {			
				Customer temp = new Customer(users.getString("username"), users.getString("pass"));
				myList.add(temp);
			}
		} catch (SQLException except) {
			System.out.println("something went wrong when creating array userList");
		}
		return myList;
	}
		
	public static void viewLot(User user) {
		String q = "select * from lot";
		try {
			PreparedStatement statement = conn.prepareStatement(q);
			ResultSet lot = statement.executeQuery();
			while (lot.next()) {
				System.out.println("ID " + lot.getInt(1) + ": " + lot.getString(6) + " " + lot.getInt(2) + " " + lot.getString(3) 
				+ " " + lot.getString(4) + " with " + lot.getInt(5) + " miles");
				
			}
		} catch(SQLException except) {
			System.out.println("Something went wrong when viewing lot");
		}
		options(user);
	}
	
	public static void addCar(User user, Car car){
		String q = "insert into lot (carid, caryear, carmake, carmodel, mileage, color) values(?, ?, ?, ?, ?, ?)";
		try{
			PreparedStatement statement = conn.prepareStatement(q);
			statement.setInt(1, car.getId());
			statement.setString(2, car.getYear());
			statement.setString(3, car.getMake());
			statement.setString(4, car.getModel());
			statement.setString(5, car.getMiles());
			statement.setString(6, car.getColor());
			statement.executeUpdate();
			System.out.println("Car added successfully.");
		} catch(Exception except) {
			System.out.println("Something went wrong when adding a car!");
		}
		options(user);
	}
	
	public static void removeCar(User user, int carid) {
		String q1 = "delete from lot where carid=?";
		String q2 = "delete from offers where carid=?";
		try {
			PreparedStatement statement1 = conn.prepareStatement(q1);
			PreparedStatement statement2 = conn.prepareStatement(q2);
			statement1.setInt(1, carid);
			statement2.setInt(1, carid);
			statement1.executeUpdate();
			System.out.println("Car deleted from lot successfully");
			statement2.executeUpdate();
			System.out.println("Respective offers removed successfully");
		} catch(Exception except) {
			System.out.println("Something went wrong when removing a car and its offers");
		}
		options(user);
	}
	
	public static void makeOffer(User user, int carid, int userid, int leninmonths, int price){
		System.out.println("entered function");
		String q = "insert into offers(carid, userid, leninmonths, totalprice, rejectoraccept) values(?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement statement = conn.prepareStatement(q);
			statement.setInt(1, carid);
			System.out.println("carid "+carid);
			statement.setInt(2, userid);
			System.out.println("userid " +userid);
			statement.setInt(3, leninmonths);
			System.out.println("leninmonths "+leninmonths);
			statement.setInt(4, price);
			System.out.println("totlprice " +price);
			statement.setInt(5, 0);
			statement.executeUpdate();
			System.out.println("Offer made successfully.");
		} catch (Exception except) {
			System.out.println("Something went wrong when making an offer");
			except.printStackTrace();
		}
		options(user);
	}
	
	public static void makePayment(User user, int carid, int userid){
//		String q = "select * from offers where carid=? and userid=? and rejectoraccept=2";
		String q1 = "select * from owned where carid=? and userid?";
		int total = 0;
		int length = 0;
		int amountpaid = 0;
		int thispayment = 0;
		
		try {
			PreparedStatement statement = conn.prepareStatement(q1);
			statement.setInt(1, carid);
			statement.setInt(2, userid);
			ResultSet loan = statement.executeQuery();
			while (loan.next()) {
				total = loan.getInt(5);
				amountpaid = loan.getInt(3);
				length = loan.getInt(4);
			}
		} catch (SQLException except) {
			System.out.println("Something went wrong when retreiving loan data");
		}
		
		System.out.println("Press 1 to make monthly payment, 2 to enter custom amount");
		Scanner scan = new Scanner(System.in);				
		if (scan.nextInt() == 1) {
			int monthly = total/length;
			try {
				PreparedStatement monthlypayment = conn.prepareStatement("insert into payments(carid, userid, amountpaid, leninmonths,"
						+ " totalprice, thispayment) values(?, ?, ?, ?, ?, ?)");
				monthlypayment.setInt(1, carid);
				monthlypayment.setInt(2, userid);
				monthlypayment.setInt(3, amountpaid + monthly);
				monthlypayment.setInt(4, length - 1);			
				monthlypayment.setInt(5, total);
				monthlypayment.setInt(6, monthly);
			} catch (SQLException except) {
				System.out.println("Something went wrong when updating payment table on monthly fee");
			}
		
		} else if (scan.nextInt() == 2) {
			System.out.println("Enter desired amount: ");
			int pay = scan.nextInt();
			try {
				PreparedStatement monthlypayment = conn.prepareStatement("insert into payments(carid, userid, amountpaid, leninmonths,"
						+ " totalprice, thispayment) values(?, ?, ?, ?, ?, ?)");
				monthlypayment.setInt(1, carid);
				monthlypayment.setInt(2, userid);
				monthlypayment.setInt(3, amountpaid + pay);
				monthlypayment.setInt(4, length-1);
				monthlypayment.setInt(5, total);
				monthlypayment.setInt(6, pay);
			} catch (SQLException except) {
				System.out.println("Something went wrong when updating payment table on custom amount");
			}
		
		}
		System.out.println("Payment completed.");
		options(user);
	}
	
	public static void viewAllOffers(User user) {
		String q = "select * from offers where rejectoraccept in (0, 1)";
		try {
			PreparedStatement statement = conn.prepareStatement(q);
			ResultSet offers = statement.executeQuery();
			while(offers.next()) {
				System.out.println("User ID " + offers.getInt("userid") + " on Car ID " + offers.getInt("carid") + " for total "
				+ offers.getInt("totalprice") + " paid over " + offers.getInt("leninmonths") + " months");
			}
			
		} catch(Exception except) {
			System.out.println("Something went wrong when viewing all offers");
		}
		options(user);
	}
	
	public static void viewRemainingPayments(User user, int carid, int userid){
		String q = "select * from payments where carid=? and userid=?";
		
		
		try {
			PreparedStatement statement = conn.prepareStatement(q);
			statement.setInt(1, carid);
			statement.setInt(2, userid);
			ResultSet payments = statement.executeQuery();
			while(payments.next()) {
				System.out.println("Still need to pay " + (payments.getInt("totalprice")-payments.getInt("amountpaid")) + " for car with ID " + carid);
			}
		} catch(Exception except) {
			System.out.println("Something went wrong when viewing remaining payments");
		}
		options(user);
	}
	
	public static void respondToOffer(User user, int carid, int userid, int rejectoraccept){
		System.out.println("carid "+carid);
		System.out.println("userid "+userid);
		String q = "update offers set rejectoraccept=2 where carid=? and userid=?";
		String q2 = null;
		String q3 = "select * from lot";
		String q4 = "insert into owned(carid, userid, totalprice, paid, debt) values (?, ?, ?, ?, ?)";
		String q5 = "select * from offers where carid=? and userid=?";
		
		int totalprice = 0;
		int duration = 0;
		int paid = 0;
		
		try {
			PreparedStatement statement = conn.prepareStatement(q5);
			statement.setInt(1, carid);
			statement.setInt(2, userid);
			ResultSet rs = statement.executeQuery();
			System.out.println("asdf");
			while(rs.next()) {
				duration = rs.getInt("leninmonths");
				System.out.println("duration "+duration);
				totalprice = rs.getInt("totalprice");
				System.out.println("totalprice "+totalprice);
			}
		} catch(SQLException except) {
			System.out.println("Something went wrong when retreiving loan data");
		}
		
		if (rejectoraccept == 1) {
			q2 = "delete from offers where carid=? and userid not in (?)";
		}
		try{			
			PreparedStatement statement = conn.prepareStatement(q);
			statement.setInt(1, carid);
			statement.setInt(2, userid);
			statement.executeUpdate();
			if (q2 != null) {
				PreparedStatement statement2 = conn.prepareStatement(q2);
				statement2.setInt(1, carid);
				statement2.setInt(2, userid);
				statement2.executeUpdate();
				System.out.println("Removing other offers of that came car");
			}
		} catch(Exception except) {
			System.out.println("Something went wrong when responding to an offer.");
		}
		
		try {
			PreparedStatement findcars = conn.prepareStatement(q4);
			findcars.setInt(1, carid);
			findcars.setInt(2, userid);
			findcars.setInt(3, totalprice);
			findcars.setInt(4, paid);
			findcars.setInt(5, totalprice-paid);
			findcars.executeUpdate();			
		
		} catch (SQLException except) {
			System.out.println("Something went wrong when associating cars to owners");
		}
			
		options(user);
	}
	
	public static void showOwned(User user, int userid) {
		String q = "select * from owned where userid=?";
		try {
			PreparedStatement statement = conn.prepareStatement(q);
			statement.setInt(1, userid);
			ResultSet rs = statement.executeQuery();
			while(rs.next()) {
				System.out.println("You own car with ID "+rs.getInt(1));
			}
		} catch (SQLException except) {
			System.out.println("Something went wrong with getting owned vehicles.");
		}
		options(user);
	}
	
	public static void main(String[] args) {
		runAccount();
	}
	
	public static void runAccount() {
		
		System.out.println("Enter Username: ");
		Scanner scan = new Scanner(System.in);
		String name = scan.nextLine();
		System.out.println("Enter password: ");
		String pass = scan.nextLine();
		//scan.close();
		login(name, pass);
	}
}
