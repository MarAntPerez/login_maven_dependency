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

public class IdRepository {

	private static final Logger LOG = LogManager.getLogger(IdRepository.class);

	public Map<String, String> load() {
		HashMap<String, String> ids = new HashMap<>();

		try (BufferedReader reader = new BufferedReader(new FileReader("Datos/ids.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("=", 2);
				if (parts.length == 2) {
					ids.put(parts[0], parts[1]);
				}
			}
		} catch (IOException e) {
			LOG.error("context: ", e);
		}
		return ids;
	}

	public boolean save(Map<String, String> ids, String fileName) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Datos/" + fileName + ".txt"))) {
			for (Map.Entry<String, String> entry : ids.entrySet()) {
				writer.write(entry.getKey() + "=" + entry.getValue());
				writer.newLine();
			}
			return true;
		} catch (IOException e) {
			LOG.error("context: ", e);
			return false;
		}
	}

}
