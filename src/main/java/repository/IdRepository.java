package repository;

import java.util.HashMap;

import entity.IdEntity;

public class IdRepository {

	public HashMap<String, String> load(){
		return new HashMap<>();
	}
	
	public HashMap<String, String> save(IdEntity newId) {
		HashMap<String, String> ids = new HashMap<String, String>();
		ids.put(newId.getUsername(), newId.getId());
		return ids;
	}
	
}
