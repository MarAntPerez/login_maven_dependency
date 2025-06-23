package service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesService {

	private static final String KEY = "=18F\\j]/8z5FSeg|d!CoM_v&006O)R/J?8569/n14KD'+`XVU}";
	private static final String AES_TRANSFORMATION = "AES/CBC/NoPadding";
	private static final String ALGORITHM = "AES";
	private static final byte[] FIXED_IV = new byte[16];

	private SecretKey secretKey;

	public AesService() throws NoSuchAlgorithmException {
		byte[] keyBytes = KEY.getBytes(StandardCharsets.UTF_8);
		byte[] keyAdjusted = new byte[16];
		System.arraycopy(keyBytes, 0, keyAdjusted, 0, Math.min(keyBytes.length, 16));
		this.secretKey = new SecretKeySpec(keyAdjusted, ALGORITHM);
	}

	public String encrypt(String word) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		byte[] data = pad(word.getBytes(StandardCharsets.UTF_8));

		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		IvParameterSpec ivSpec = new IvParameterSpec(FIXED_IV);

		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
		byte[] encrypted = cipher.doFinal(data);

		return Base64.getEncoder().encodeToString(encrypted);
	}

	public String decrypt(String encryptedBase64) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
		byte[] ciphertext = Base64.getDecoder().decode(encryptedBase64);

		Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(FIXED_IV));

		byte[] decrypted = cipher.doFinal(ciphertext);
		return new String(decrypted, StandardCharsets.UTF_8).trim();
	}

	private byte[] pad(byte[] data) {
		int blockSize = 16;
		int paddedLength = ((data.length + blockSize - 1) / blockSize) * blockSize;
		byte[] padded = new byte[paddedLength];
		System.arraycopy(data, 0, padded, 0, data.length);
		return padded;
	}

}
