package dealership;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

//import java.sql.*;

public class Customer extends User{
	
	
	public Customer(String name, String pass) {
		super(name, pass);
	}
	
//	public static void main(String[] args){
//		Scanner scan = new Scanner(System.in); 
//		System.out.println("Welcome! How may I help you today?");
//		System.out.println("Press 1 to View Lot, Press 2 to View Owned Cars");
//		String select = scan.nextLine();
//		if (select == "1"){
//			viewLot();
//		} else if (select == "2"){
//			offers();
//		} else {
//			System.out.println("Invalid input!");
//			select = scan.nextLine();
//		}
//		
//	}
	
	public int getId(){
		String q = "select userid from dealerusers where username=? and pass=?";
		try {
			PreparedStatement statement = conn.prepareStatement(q);
			statement.setString(1, name);
			statement.setString(2, pass);
			ResultSet rs = statement.executeQuery();
			//System.out.println(rs.getRow());
			while(rs.next()) {
				int id = rs.getInt("userid");
			}
		} catch (SQLException except) {
			System.out.println("Something went wrong with getting user id"); 
		}
		return id;
	}
	
	public static ArrayList<String> owned = new ArrayList<String>();
	
	public int getLastId() {
		String q = "select userid from dealerusers";
		try {
			PreparedStatement statement = conn.prepareStatement(q);
			ResultSet rs = statement.executeQuery();
			if (rs.isLast()) {
				return id = rs.getInt("pass");
			}
		} catch (SQLException except) {
			System.out.println("Something went wrong with getting last id"); 
		}
		return id;
	}
	
	public void getOwned() {
		for(String car:owned) {
			System.out.println(car);
		}
	}
	
}
