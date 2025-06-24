package service;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dto.UserData;
import entity.IdEntity;
import entity.UserEntity;
import enums.TokenDuration;
import exceptions.AesFailedException;
import exceptions.ErrorLoadAesException;
import exceptions.ExpiredTokenException;
import exceptions.TokenInvalidException;
import exceptions.UserNotFoundException;
import repository.IdRepository;
import repository.UserRepository;

public class LoginService {

	private static final Logger LOG = LogManager.getLogger(LoginService.class);
	private static final int INDEX_OF_ID = 0;
	private static final int INDEX_OF_DATE = 1;
	private static final String CONTEXT_ERROR = "context: ";
	private static final String FILE_IDS = "ids";
	private static final String FILE_USERS = "users";
	private HashMap<String, UserEntity> users;
	private HashMap<String, String> ids;
	private UserRepository userRepository;
	private IdRepository idRepository;
	private AesService aesService;
	private TokenDuration tokenDuration;

	public LoginService(TokenDuration tokenDuration) throws ErrorLoadAesException {
		this.tokenDuration = tokenDuration;
		userRepository = new UserRepository();
		idRepository = new IdRepository();
		setUsers(userRepository.load());
		setIds(idRepository.load());
		try {
			aesService = new AesService();
		} catch (NoSuchAlgorithmException e) {
			LOG.error(e);
			LOG.error(CONTEXT_ERROR, e);
			throw new ErrorLoadAesException();
		}
	}

	public LoginService(AesService aesService, UserRepository userRepository, IdRepository idRepository,
			TokenDuration tokenDuration) {
		this.aesService = aesService;
		this.userRepository = userRepository;
		this.idRepository = idRepository;
		this.tokenDuration = tokenDuration;
	}

	public boolean userRegister(UserData data)
			throws IOException, AesFailedException, InvalidAlgorithmParameterException {
		if (data == null) {
			LOG.error("Error, datos no validos");
			return false;
		}

		UserEntity userEntity = userDtoToEntity(data);

		try {

			UserEntity userEncripted;

			userEncripted = encryptUserEntity(userEntity);

			String idEncrypted = aesService.encrypt(UUID.randomUUID().toString());
			userEncripted.setId(idEncrypted);
			IdEntity idEntity = new IdEntity();
			idEntity.setId(idEncrypted);
			idEntity.setUsername(userEncripted.getUsername());
			users.put(idEncrypted, userEncripted);
			ids.put(idEntity.getUsername(), idEntity.getId());
			LOG.info("Usuario encriptado con exito");
			if (userRepository.save(users, FILE_USERS) && idRepository.save(ids, FILE_IDS)) {
				LOG.info("Usuario guardado con exito");
				return true;
			}

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			LOG.error("Error al cifrar/descifrar datos. {}", userEntity);
			LOG.error(CONTEXT_ERROR, e);
			throw new AesFailedException();
		}
		return false;
	}

	public String auth(UserData data) throws UserNotFoundException, InvalidAlgorithmParameterException {
		if (data == null) {
			LOG.error("Error datos no validos");
			throw new UserNotFoundException();
		}

		UserEntity user = userDtoToEntity(data);

		try {
			String usernameEncrypted = aesService.encrypt(user.getUsername());
			String id = searchId(usernameEncrypted);
			String token;
			if (id != null) {
				token = generateToken(id);
				LOG.info("Token generado con exito {}", token);
				return token;
			} else {
				throw new UserNotFoundException();
			}

		} catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			LOG.error("Los datos ingresados no son correctos. {}", data.getUsername());
			LOG.error(CONTEXT_ERROR, e);
			throw new UserNotFoundException();
		}
	}

	public boolean verifyToken(String token) throws TokenInvalidException, ExpiredTokenException {
		try {
			String[] tokenDivide = token.split("_");
			if (tokenDivide.length != 2) {
				LOG.error("Formato de token no valido");
				return false;
			}
			String id = tokenDivide[INDEX_OF_ID];
			LOG.debug("ID: {}", id);
			String date = tokenDivide[INDEX_OF_DATE];

			LocalDateTime tokenTime = LocalDateTime.parse(date);
			LocalDateTime now = LocalDateTime.now();

			Duration duration = Duration.between(tokenTime, now);
			long secondsPassed = duration.getSeconds();

			if (secondsPassed <= tokenDuration.getSeconds()) {
				LOG.info("Token valido, tiempo trancurrido {}", secondsPassed);
				return true;
			} else {
				LOG.error("Token expirado.");
				throw new ExpiredTokenException();
			}
		} catch (DateTimeException e) {
			LOG.error("Fecha de token no valida", e);
			throw new TokenInvalidException();
		} catch (ExpiredTokenException e) {
			throw e;
		} catch (Exception e) {
			LOG.error("Error al verificar el token", e);
			throw new TokenInvalidException();
		}
	}

	private String generateToken(String id) {
		return id + "_" + LocalDateTime.now().toString();
	}

	private UserEntity userDtoToEntity(UserData user) {
		UserEntity entity = new UserEntity();
		entity.setUsername(user.getUsername());
		entity.setPsw(user.getPsw());

		LOG.info("DTO convertido a Entity: {}", entity.getUsername());
		LOG.debug("DTO Convertido a Entity:{} ", entity);

		return entity;
	}

	private UserEntity encryptUserEntity(UserEntity userEntityOriginal)
			throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		UserEntity userEntityEncrypted = new UserEntity();
		userEntityEncrypted.setUsername(aesService.encrypt(userEntityOriginal.getUsername()));
		userEntityEncrypted.setPsw(aesService.encrypt(userEntityOriginal.getPsw()));
		return userEntityEncrypted;
	}

	private String searchId(String username) {
		return ids.get(username);
	}

	public Map<String, UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Map<String, UserEntity> users) {
		this.users = (HashMap<String, UserEntity>) users;
	}

	public Map<String, String> getIds() {
		return ids;
	}

	public void setIds(Map<String, String> ids) {
		this.ids = (HashMap<String, String>) ids;
	}

}
