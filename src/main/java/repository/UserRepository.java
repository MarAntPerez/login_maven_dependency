package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import entity.UserEntity;

public class UserRepository {

	public HashMap<String, UserEntity> load(){
		HashMap<String, UserEntity> users = new HashMap<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("Datos/users.txt"))) {
			String line;
			while((line = reader.readLine()) != null) {
				String parts[] = line.split("=", 2);
				if(parts.length == 2) {
					UserEntity user = new UserEntity();
					user = user.fromString(line);
					if(user != null) {
						users.put(parts[0], user);
					}
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return users;
	}
	
	public boolean save(HashMap<String, UserEntity> users) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Datos/users.txt"))){
			for(Map.Entry<String, UserEntity> entry : users.entrySet()){
				writer.write(entry.getKey() + "=" + entry.getValue());
				writer.newLine();
			}
		}catch (IOException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
