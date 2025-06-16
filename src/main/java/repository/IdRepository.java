package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IdRepository {

	public HashMap<String, String> load(){
		HashMap<String, String> ids = new HashMap<>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader("Datos/ids.txt"))){
			String line;
			while((line = reader.readLine()) != null) {
				String[] parts = line.split("=", 2);
				if(parts.length == 2) {
					ids.put(parts[0], parts[1]);
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}
		return ids;
	}
	
	public boolean save(HashMap<String, String> ids) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter("Datos/ids.txt"))){
			for(Map.Entry<String, String> entry : ids.entrySet()) {
				writer.write(entry.getKey() + "=" +entry.getValue());
				writer.newLine();
			}
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
}
