package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class IdRepositoryTest {
	
	@Test
	void loadTest() throws FileNotFoundException {
		IdRepository idRepo = new IdRepository();

		File file = new File("Datos/ids.txt");
		file.getParentFile().mkdirs();

		try (PrintWriter out = new PrintWriter(file)) {
			out.println("usuario1=1234");
			out.println("usuario2=5678");
		}

		HashMap<String, String> ids = (HashMap<String, String>) idRepo.load();
		assertNotNull(ids);
		assertEquals("1234", ids.get("usuario1"));

		ids = (HashMap<String, String>) idRepo.load();
	}
	
	@Test
	void saveTest() throws IOException {
		IdRepository idRepo = new IdRepository();

		HashMap<String, String> ids = new HashMap<>();
		ids.put("Username", "Id");

		assertTrue(idRepo.save(ids, "ids"));
		File file = new File("Datos/ids.txt");
		assertTrue(file.exists());
		file.delete();

		File subPath = new File("Datos/No");
		subPath.getParentFile().mkdirs();
		if (subPath.exists()) subPath.delete();
		assertTrue(subPath.createNewFile());

		boolean result = idRepo.save(ids, "No/Escribible");

		assertFalse(result, "Se esperaba que save devolviera false por IOException");

		subPath.delete();
	}
	
	@Test
	void loadShouldCatchIOExceptionTest() {
		IdRepository idRepo = new IdRepository();

		File file = new File("Datos/ids.txt");
		if (file.exists()) {
			file.delete();
		}

		assertTrue(file.mkdir(), "No se pudo crear 'Datos/ids.txt' como directorio");

		Map<String, String> ids = idRepo.load();

		assertNotNull(ids);
		assertTrue(ids.isEmpty(), "Se esperaba que el mapa estuviera vacío tras el fallo de lectura");

		file.delete();
	}


}
