package dealership;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.utils.ConnectionUtil;

public class Car {
	public static ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	public static Connection conn = cu.getConnection();
	
	private String make;
	private String model;
	private String year;
	private String mileage;
	private String color;
	private int carid;
	private int stickerPrice;
	public int[] offers;
	
	public Car(String make, String model, String year, String mileage, String color) {
		this.make = make;
		this.model = model;
		this.year = year;
		this.color = color;
		this.stickerPrice = stickerPrice;
		this.mileage = mileage;
		this.carid = getId();
	}
	
	public String getName() {
		String name = color + " " + year + " " + make + " " + model + " with " + mileage + " miles.";
		return name;
	}

	public String getModel() {
		return model;
	}
	
	public String getMiles() {
		return mileage;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getMake() {
		return make;
	}
	
	public String getColor() {
		return color;
	}
	
	public int getStickerPrice() {
		return stickerPrice;
	}

	/**
	 * @return
	 */
	public int getId() {
//		String q = "select count(carid) ct from lot";
//		int ret = 1234;
//		try {
//			PreparedStatement statement = conn.prepareStatement(q);
//			ResultSet ids = statement.executeQuery();
//			ret += ids.getInt("ct");
//		} catch(SQLException except) {
//			System.out.println("Something went wrong when creating an id in car");
//		}
//		return ret;
		//System.out.println((int)((Math.random()*(9999-1000)+1)+1000));
		return (int)((Math.random()*(9999-1000)+1)+1000);
	}

	
}
