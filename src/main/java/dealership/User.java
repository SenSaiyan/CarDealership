package dealership;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.utils.ConnectionUtil;

public abstract class User {
	public static ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	public static Connection conn = cu.getConnection();
	
	protected static String name;
	protected static String pass;
	protected static int id;
	
	public User(String name, String pass){
		this.name = name;
		this.pass = pass;
	}
		
	public String getName(){
		return name;
	}
	
	public int getId() {
//		String q = "select carid from dealerusers";
//		try {
//			PreparedStatement statement = conn.prepareStatement(q);
//			ResultSet ids = statement.executeQuery();
//			if (ids.isLast()) {
//				return ids.getInt("userid") + 1;
//			}
//		} catch(SQLException except) {
//			System.out.println("Something went wrong when creating an id in car");
//		}
//		return 0;
		return (int)((Math.random()*(9999-1000)+1)+1000);
	}

	public static String getPass() {
		return pass;
	}

	public static void setPass(String pass) {
		User.pass = pass;
	}

	public static void setName(String name) {
		User.name = name;
	}
	
	//abstract public String[] viewLot();
}
