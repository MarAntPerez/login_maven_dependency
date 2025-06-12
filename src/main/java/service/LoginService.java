package service;

import java.lang.System.Logger;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;

import dto.UserData;
import entity.UserEntity;
import repository.IdRepository;
import repository.UserRepository;

public class LoginService {
	
	HashMap<String, UserEntity> users;
	HashMap<String, String> ids;
	UserRepository userRepository;
	IdRepository idRepository;
	
	public LoginService(){
		userRepository = new UserRepository();
		idRepository = new IdRepository();
		users = userRepository.load();
		ids = idRepository.load();
	}
	
	public String auth(UserData data) {
		return new String();
	}
	
	public boolean userRegister(UserData data) {
		return true;
	}
	
	public boolean verifyToken(String token) {
		return true;
	}
	
	private String generateToken(UserData data) {
		return new String();
	}
	
//	private UserEntity userDtoToEntity(UserData user) {
//		if(user == null) {
//		}
//	}

}
