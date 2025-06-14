package service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		if (data == null) {
			LOG.error("Error, datos no validos");
		}

		UserEntity userEntity = userDtoToEntity(data);
		Aes aes = new Aes();
		System.out.println("DTO convertido a Entity");

		if (userEntity != null) {
			UserEntity userEncripted = encryptUserEntity(userEntity);
			String idEncrypted = aes.encrypt(UUID.randomUUID().toString());
			userEncripted.setId(idEncrypted);
			IdEntity idEntity = new IdEntity();
			idEntity.setId(idEncrypted);
			idEntity.setUsername(userEncripted.getUsername());
			users.put(idEncrypted, userEntity);
			LOG.info("Usuario encriptado con exito");
			if (userRepository.save(users)) {
				ids = idRepository.save(idEntity);
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
			System.out.println("Error datos no validos");
			LOG.error("Error datos no validos");
			return null;
		}

		Aes aes = new Aes();
		UserEntity user = userDtoToEntity(data);
		String id = searchId(aes.encrypt(user.getUsername()));
		String token;
		if (id != null) {
			token = generateToken(id);
			System.out.println("Token generado con exito: " + token);
			LOG.info("Token generado con exito {}", token);
			return token;
		}

		System.out.println("Los datos ingresados no son correctos");
		LOG.error("Los datos ingresados no son correctos.");
		return null;
	}

	public boolean verifyToken(String token) {
		String[] tokenDivide = token.split("_");
		String id = tokenDivide[0];
		String date = tokenDivide[1];
		return true;
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
