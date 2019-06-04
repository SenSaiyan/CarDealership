package dao;
import dealership.User;

public interface UserDAO {
	public void makeAccount(String name, String pass);
	public void login(String name, String pass);

}
