package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.utils.ConnectionUtil;

import dealership.User;
import dealership.Employee;
import dealership.Queries;
import dealership.Customer;

public class UserOracle implements UserDAO{
	
	static ConnectionUtil cu = null;
	static Connection conn = null;
	static Logger log = Logger.getLogger(UserOracle.class);



	@Override
	public void makeAccount(String name, String pass) {
		Scanner scan = new Scanner(System.in); 
		System.out.println("No user found. Create one with current credentials? "
				+ "Press 1 for yes, 2 to  return to login");
		String input = scan.nextLine();
		int choice = Integer.parseInt(input);
		switch(choice) {
		case 1:
			Customer temp = new Customer(name, pass);
			try {
				String q2 = "insert into users (userid, username, pass, usertype) values(?, ?, ?, 'customer')";					
				PreparedStatement statement = conn.prepareStatement(q2);
				statement.setInt(1, temp.getId());
				statement.setString(2, name);
				statement.setString(3, pass);
				statement.executeUpdate();
				System.out.println("help me");
				login(name,pass);
			} catch(Exception e){
				System.out.println("Something went wrong when creating new user");
			}				
		case 2:
			Queries.runAccount();
		}
		
	}

	public void login(String name, String pass) {
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
				Queries.options(emp);
			} else if (accountType.equals("customer")) {
				Customer cust = new Customer(name, pass);
				Queries.options(cust);
			} else {
				makeAccount(name, pass);
			}
		} catch(SQLException except){
			makeAccount(name, pass);
		}
	}

}
