package repository;

import java.util.HashMap;

import entity.UserEntity;

public class UserRepository {

	public HashMap<String, UserEntity> load(){
		return new HashMap<String, UserEntity>();
	}
	
	public boolean save(UserEntity newUser) {
		return true;
	}
	
}
