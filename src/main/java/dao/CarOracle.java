package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import dealership.Car;
import com.revature.utils.ConnectionUtil;


public class CarOracle implements CarDAO{
	private static ConnectionUtil cu;
	private static Connection conn;
	
	private CarOracle() {
		cu = ConnectionUtil.getConnectionUtil();
		conn = cu.getConnection();
	}

	@Override
	public String getName(Car car) {
		String name = car.getColor() + " " + car.getYear() + " " + car.getMake() + " " + car.getModel() + " with " + car.getMiles() + " miles.";

		return name;
	}


	@Override
	public void addCarToLot(Car car) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void showOwned(int userid) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void viewLot() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void removeCar(Car car) {
		// TODO Auto-generated method stub
		
	}
	
}
