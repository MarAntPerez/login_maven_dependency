package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import entity.UserEntity;

public class UserRepository {

	private static final Logger LOG = LogManager.getLogger(UserRepository.class);

	public Map<String, UserEntity> load(){
		HashMap<String, UserEntity> users = new HashMap<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("Datos/users.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				UserEntity user = fromString(line);
				if (user != null) {
					String id = extractId(line);
					users.put(id, user);
				}
			}
		}catch(IOException e) {
			LOG.error("context: ", e);
		}
		return users;
	}
	
	public boolean save(Map<String, UserEntity> users, String fileName) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Datos/" + fileName + ".txt"))){
			for (Map.Entry<String, UserEntity> entry : users.entrySet()) {
				writer.write(entry.getKey() + "=" + toString(entry.getValue()));
				writer.newLine();
			}
			return true;
		}catch (IOException e){
			LOG.error("context: ", e);
			return false;
		}
	}
	
	private String toString(UserEntity userEntity) {
	    return userEntity.getUsername() + "," + userEntity.getPsw();
	}

	private UserEntity fromString(String data) {
		String[] parts = data.split("=", 2);
		if (parts.length != 2) return null;
		String[] userParts = parts[1].split(",", 2);
		if (userParts.length != 2) return null;

		UserEntity user = new UserEntity();
		user.setUsername(userParts[0]);
		user.setPsw(userParts[1]);
		return user;
	}
	
	private String extractId(String data) {
		String[] parts = data.split("=", 2);
		return (parts.length == 2) ? parts[0] : "";
	}

	
}
