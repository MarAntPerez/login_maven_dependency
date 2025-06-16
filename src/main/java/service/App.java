package service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dto.UserData;
import entity.UserEntity;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		
		  Logger LOG = LogManager.getLogger(App.class);

		LOG.info("KMLKFNNDNFLK");

		LoginService login = new LoginService();
		Aes aes = new Aes();

		UserData user = new UserData();
		user.setUsername("username");
		user.setPsw("1234");
		if(login.userRegister(user)) {
			System.out.println("Usuario registrado");
		}
		login.userRegister(null);
		login.auth(user);
		UserEntity uE = new UserEntity(user.getUsername(), user.getPsw(), aes.encrypt(UUID.randomUUID().toString()) + "_" + LocalDateTime.now());
		login.verifyToken(uE.getId());
		login.verifyToken("JAJA, token inventadote");
		
	}
}
