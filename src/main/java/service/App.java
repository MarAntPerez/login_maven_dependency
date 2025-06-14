package service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import dto.UserData;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		LoginService login = new LoginService();

		UserData user = new UserData();
		user.setUsername("username");
		user.setPsw("1234");
		if(login.userRegister(user)) {
			System.out.println("Usuario registrado");
		}
		login.userRegister(null);
		
		System.out.println("Token en app: " + login.auth(user));
	}
}
