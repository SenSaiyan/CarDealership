package dao;

import dealership.Car;

public interface CarDAO {
	public void addCarToLot(Car car);
	public void showOwned(int userid);
	public void viewLot();
	public String getName(Car car);
	public void removeCar(Car car);
}
