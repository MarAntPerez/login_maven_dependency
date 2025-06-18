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
			while((line = reader.readLine()) != null) {
				String[] parts = line.split("=", 2);
				if(parts.length == 2) {
					UserEntity user = new UserEntity();
					user = user.fromString(line);
					if(user != null) {
						users.put(parts[0], user);
					}
				}
			}
		}catch(IOException e) {
			LOG.error("context: ", e);
		}
		return users;
	}
	
	public boolean save(Map<String, UserEntity> users) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Datos/users.txt"))){
			for(Map.Entry<String, UserEntity> entry : users.entrySet()){
				writer.write(entry.getKey() + "=" + entry.getValue());
				writer.newLine();
			}
		}catch (IOException e){
			LOG.error("context: ", e);
			return false;
		}
		return true;
	}
	
}
