package repository;

import java.util.HashMap;

import entity.IdEntity;

public class IdRepository {

	public HashMap<String, String> load(){
		return new HashMap<>();
	}
	
	public boolean save(IdEntity newId) {
		return true;
	}
	
}
