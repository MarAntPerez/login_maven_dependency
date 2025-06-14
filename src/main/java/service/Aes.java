package service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Aes {

	private static final String KEY = "=18F\\j]/8z5FSeg|d!CoM_v&006O)R/J?8569/n14KD'+`XVU}";
	private static final String ALGORITMO_AES = "AES";

	private SecretKey secretKey;

	public Aes() throws NoSuchAlgorithmException {
		byte[] keyOnBytes = KEY.getBytes(StandardCharsets.UTF_8);
		byte[] keyAdjusted = new byte[16];
		System.arraycopy(keyOnBytes, 0, keyAdjusted, 0, Math.min(keyOnBytes.length, 16));
		secretKey = new SecretKeySpec(keyAdjusted, "AES");
	}

	public String encrypt(String word) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException  {
		Cipher cipher = Cipher.getInstance(ALGORITMO_AES);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] idEncrypted = cipher.doFinal(word.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(idEncrypted);
		
	}

	public String decrypt(String word) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance(ALGORITMO_AES);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] idDecrypt = cipher.doFinal(Base64.getDecoder().decode(word));
		return new String(idDecrypt, StandardCharsets.UTF_8);
	}

}
