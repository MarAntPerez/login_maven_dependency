package service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import dto.UserData;
import enums.TokenDuration;
import exceptions.AesFailedException;
import exceptions.ErrorLoadAesException;
import exceptions.ExpiredTokenException;
import exceptions.TokenInvalidException;
import exceptions.UserNotFoundException;
import repository.IdRepository;
import repository.UserRepository;

class LoginServiceTest {

	@Test
	void userRegisterTest()
			throws ErrorLoadAesException, InvalidAlgorithmParameterException, IOException, AesFailedException {
		TokenDuration tokenDuration = TokenDuration.THIRTY_SECONDS;
		LoginService loginService = new LoginService(tokenDuration);
		UserData data = new UserData();
		data.setUsername("Username");
		data.setPsw("psw");
		assertFalse(loginService.userRegister(null));
		assertTrue(loginService.userRegister(data));
	}

	@Test
	void authTest() throws ErrorLoadAesException, InvalidAlgorithmParameterException, UserNotFoundException,
			IOException, AesFailedException {
		TokenDuration tokenDuration = TokenDuration.THIRTY_SECONDS;
		LoginService loginService = new LoginService(tokenDuration);
		UserData data = new UserData();
		data.setUsername("Username");
		data.setPsw("psw");
		assertTrue(loginService.userRegister(data));
		assertNotNull(loginService.auth(data));
		assertThrows(UserNotFoundException.class, () -> {
			loginService.auth(null);
		});
	}

	@Test
	void verifyTokenTest() throws ErrorLoadAesException, TokenInvalidException, ExpiredTokenException {
		TokenDuration tokenDuration = TokenDuration.THIRTY_SECONDS;
		LoginService loginService = new LoginService(tokenDuration);
		assertFalse(loginService.verifyToken("tokeninvalido"));
		assertThrows(TokenInvalidException.class, () -> {
			loginService.verifyToken("id_2024-13-99T25:61:00");
		});
		LocalDateTime now = LocalDateTime.now();
		String token = "id_" + now.toString();
		assertTrue(loginService.verifyToken(token));
		LocalDateTime oldTime = LocalDateTime.now().minusMinutes(5);
		String oldToken = "id_" + oldTime.toString();
		assertThrows(ExpiredTokenException.class, () -> {
			loginService.verifyToken(oldToken);
		});
		assertThrows(TokenInvalidException.class, () -> {
			loginService.verifyToken(null);
		});
	}

//	@Mock
//	private AesService aesService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private IdRepository idRepository;


}
