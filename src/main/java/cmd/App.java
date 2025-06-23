package cmd;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
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
import enums.TokenDuration;
import exceptions.AesFailedException;
import exceptions.ErrorLoadAesException;
import exceptions.ExpiredTokenException;
import exceptions.TokenInvalidException;
import exceptions.UserNotFoundException;
import service.AesService;
import service.LoginService;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, IOException, ErrorLoadAesException, AesFailedException,
			TokenInvalidException, UserNotFoundException, InvalidAlgorithmParameterException, ExpiredTokenException {

		Logger LOG = LogManager.getLogger(App.class);

		LOG.info("KMLKFNNDNFLK");

		LoginService login = new LoginService(TokenDuration.THIRTY_SECONDS);
		AesService aes = new AesService();

		UserData user = new UserData();
		user.setUsername("username");
		user.setPsw("1234");
		if (login.userRegister(user)) {
			LOG.info("Usuario registrado con exito");
		}
		login.userRegister(null);
		login.auth(user);
		UserEntity uE = new UserEntity(user.getUsername(), user.getPsw(),
				aes.encrypt(UUID.randomUUID().toString()) + "_" + LocalDateTime.now());
		login.verifyToken(uE.getId());
		login.verifyToken("JAJA, token inventadote");
		login.auth(null);

	}
}
