package service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dto.UserData;
import entity.IdEntity;
import entity.UserEntity;
import repository.IdRepository;
import repository.UserRepository;

public class LoginService {

	private static final Logger LOG = LogManager.getLogger(LoginService.class);
	private HashMap<String, UserEntity> users;
	private HashMap<String, String> ids;
	private UserRepository userRepository;
	private IdRepository idRepository;

	public LoginService() {
		userRepository = new UserRepository();
		idRepository = new IdRepository();
		setUsers(userRepository.load());
		setIds(idRepository.load());
	}

	public boolean userRegister(UserData data) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {
		if (data == null) {
			LOG.error("Error, datos no validos");
			return false;
		}

		UserEntity userEntity = userDtoToEntity(data);
		Aes aes = new Aes();

		if (userEntity != null) {
			UserEntity userEncripted = encryptUserEntity(userEntity);
			String idEncrypted = aes.encrypt(UUID.randomUUID().toString());
			userEncripted.setId(idEncrypted);
			IdEntity idEntity = new IdEntity();
			idEntity.setId(idEncrypted);
			idEntity.setUsername(userEncripted.getUsername());
			users.put(idEncrypted, userEntity);
			ids.put(idEntity.getUsername(), idEntity.getId());
			LOG.info("Usuario encriptado con exito");
			if (userRepository.save(users) && idRepository.save(ids)) {
				LOG.info("Usuario guardado con exito");
				return true;
			}
		}

		LOG.error("Error al registrar el usuario");
		return false;
	}

	public String auth(UserData data) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		if (data == null) {
			LOG.error("Error datos no validos");
			return null;
		}

		Aes aes = new Aes();
		UserEntity user = userDtoToEntity(data);
		String id = searchId(aes.encrypt(user.getUsername()));
		String token;
		if (id != null) {
			token = generateToken(id);
			LOG.info("Token generado con exito {}", token);
			return token;
		}

		LOG.error("Los datos ingresados no son correctos.");
		return null;
	}

	public boolean verifyToken(String token) {
		try {
			String[] tokenDivide = token.split("_");
			if (tokenDivide.length != 2) {
				LOG.error("Formato de token no valido");
				return false;
			}
			String id = tokenDivide[0];
			String date = tokenDivide[1];

			LocalDateTime tokenTime = LocalDateTime.parse(date);
			LocalDateTime now = LocalDateTime.now();

			Duration duration = Duration.between(tokenTime, now);
			long secondsPassed = duration.getSeconds();

			if (secondsPassed <= 30) {
				LOG.info("Token valido, tiempo trancurrido {}", secondsPassed);
				return true;
			} else {
				LOG.warn("Token expirado.");
				return false;
			}
		} catch (DateTimeException e) {
			LOG.error("Fecha de token no valido", e);
			return false;
		} catch (Exception e) {
			LOG.error("Error al verificate el token", e);
			return false;
		}
	}

	private String generateToken(String id) {
		return id + "_" + LocalDateTime.now().toString();
	}

	private UserEntity userDtoToEntity(UserData user) {
		if (user == null) {
			LOG.error("No fue posible convertir el objeto DTO a Entity");
			return null;
		}

		UserEntity entity = new UserEntity();
		entity.setUsername(user.getUsername());
		entity.setPsw(user.getPsw());

		LOG.info("DTO convertido a Entity");
		return entity;
	}

	private UserEntity encryptUserEntity(UserEntity userEntityOriginal) throws NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Aes aes = new Aes();
		UserEntity userEntityEncrypted = new UserEntity();
		userEntityEncrypted.setUsername(aes.encrypt(userEntityOriginal.getUsername()));
		userEntityEncrypted.setPsw(aes.encrypt(userEntityOriginal.getPsw()));
		return userEntityEncrypted;
	}

	private String searchId(String username) {
		return ids.get(username);
	}

	public HashMap<String, UserEntity> getUsers() {
		return users;
	}

	public void setUsers(HashMap<String, UserEntity> users) {
		this.users = users;
	}

	public HashMap<String, String> getIds() {
		return ids;
	}

	public void setIds(HashMap<String, String> ids) {
		this.ids = ids;
	}

}
