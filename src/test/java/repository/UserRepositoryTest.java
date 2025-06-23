package repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import entity.UserEntity;

class UserRepositoryTest {
	
	@Test
	void loadTest() throws FileNotFoundException {
		UserRepository userRepo = new UserRepository();
		File file = new File("Datos/users.txt");
		file.getParentFile().mkdirs();
		try (PrintWriter out = new PrintWriter(file)){
			out.println("1234=Username,psw,1234");
			out.println("2345=Username_2,psw_2,2345");
		}
		HashMap<String, UserEntity> users = (HashMap<String, UserEntity>) userRepo.load();
		assertNotNull(users);
	}
	
	@Test
	void saveTest() throws IOException {
		UserRepository userRepo = new UserRepository();
		HashMap<String, UserEntity> users = new HashMap<>();
		UserEntity entity = new UserEntity("Username", "psw", "1234");
		users.put(entity.getId(), entity);
		
		assertTrue(userRepo.save(users, "users"));
		File file = new File("Datos/users.txt");
		assertTrue(file.exists());
		file.delete();
		
		File subPath = new File("Datos/No");
		subPath.getParentFile().mkdirs();
		if (subPath.exists()) subPath.delete();
		assertTrue(subPath.createNewFile());
		
		boolean result = userRepo.save(users, "No/Escribible");

		assertFalse(result, "Se esperaba que save devolviera false por IOException");

		subPath.delete();
	}
	
	@Test
	void loadShouldCatchIOExceptionTest() {
		UserRepository userRepo = new UserRepository();
		File file = new File("Datos/ids.txt");
		if (file.exists()) {
			file.delete();
		}

		assertTrue(file.mkdir(), "No se pudo crear 'Datos/users.txt' como directorio");
		HashMap<String, UserEntity> users = (HashMap<String, UserEntity>) userRepo.load();
		assertNotNull(users);
		assertTrue(users.isEmpty(), "Se esperaba que el mapa estuviera vacío tras el fallo de lectura");
		file.delete();
	}

}
