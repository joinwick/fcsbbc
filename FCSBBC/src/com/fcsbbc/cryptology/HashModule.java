package com.fcsbbc.cryptology;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fcsbbc.utils.CharacterConvert;

/**
 * @author luo.changshu
 *
 */
public class HashModule {
	private static Logger logger = LoggerFactory.getLogger(HashModule.class.getName());
	private static final String RIPEMD160 = "RipeMD160";
	private static final String SHA256 = "SHA-256";
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/**
	 * ripemd160(sha256(byte[]))
	 * @param message
	 * @return	byte[]
	 */
	public static byte[] hash160(byte[] message) {
		byte[] hashResult = null;
		if (message != null && message.length > 0) {
			hashResult = ripemd160(sha256(message));
		}
		return hashResult;
	}
	/**
	 * sha256(sha256(byte[]))
	 * @param message
	 * @return	byte[]
	 */
	public static byte[] hash256(byte[] message) {
		byte[] hashResult = null;
		if (message != null && message.length > 0) {
			hashResult = sha256(sha256(message));
		}
		return hashResult;
	}
	/**
	 * ripemd160(byte[])
	 * @param message
	 * @return	byte[]
	 */
	public static byte[] ripemd160(byte[] message) {
		byte[] hashResult = null;
		if (message != null && message.length > 0) {
			try {
				MessageDigest messageDigest = MessageDigest.getInstance(RIPEMD160);
				hashResult = messageDigest.digest(message);
			} catch (NoSuchAlgorithmException e) {
				// TODO: handle exception
				logger.error("NoSuchAlgorithmException:RipeMD160", e.getMessage());
			}
		}
		return hashResult;
	}
	/**
	 * sha256(byte[])
	 * @param message
	 * @return	byte[]
	 */
	public static byte[] sha256(byte[] message) {
		byte[] hashResult = null; 
		if (message != null && message.length > 0) {
			try {
				MessageDigest messageDigest = MessageDigest.getInstance(SHA256);
				hashResult = messageDigest.digest(message);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				logger.error("NoSuchAlgorithmException:SHA-256", e.getMessage());
			}
		}
		return hashResult;
	}
	/**
	 * sha256(byte[], int, int)
	 * @param message
	 * @param offset
	 * @param length
	 * @return	byte[]
	 */
	public static byte[] sha256(byte[] message, int offset, int length) {
		if (offset != 0 || length != message.length) {
			byte[] array = new byte[length];
			System.arraycopy(message, offset, array, 0, length);
			message = array;
		}
		return sha256(message);
	}
	/**
	 * sha256(String)
	 * @param message
	 * @param hexStringFlag
	 * @return	HexString/String
	 */
	public static String sha256(String message, boolean hexStringFlag) {
		String result = "";
		byte[] temp_result = {};
		if (message != null && message.length() > 0) {
			Digest digest = new SHA256Digest();
			byte[] convertToByte = CharacterConvert.convertStringToByte(message);
			digest.update(convertToByte, 0, convertToByte.length);
			temp_result = new byte[digest.getDigestSize()];
			digest.doFinal(temp_result, 0);
		}
		if (temp_result != null && temp_result.length > 0) {
			if (hexStringFlag) {
				result = CharacterConvert.convertByteToHexString(temp_result);
			}
			else {
				result = CharacterConvert.convertByteToString(temp_result);
			}
		}
		return result;
	}
	/**
	 * ripe160(String)
	 * @param message
	 * @return	HexString
	 */
	public static String ripe160(String message, boolean hexStringFlag) {
		String result = "";
		byte[] temp_result = {};
		if (message != null && message.length() > 0) {
			Digest digest = new RIPEMD160Digest();
			byte[] convertToByte = CharacterConvert.convertStringToByte(message);
			digest.update(convertToByte, 0, convertToByte.length);
			temp_result = new byte[digest.getDigestSize()];
			digest.doFinal(temp_result, 0);
		}
		if (temp_result != null && temp_result.length > 0) {
			if (hexStringFlag) {
				result = CharacterConvert.convertByteToHexString(temp_result);
			}
			else {
				result = CharacterConvert.convertByteToString(temp_result);
			}
		}
		return result;
	}
	
//	public static void main(String[] args) {
//		String user_password = "111111";
//		//	traditional method
//		byte[] convert_byte = null;
//		try {
//			convert_byte = user_password.getBytes(ConfigMessage.ENCODE_FORMAT_UTF8);
//			byte[] sha256_password = sha256(convert_byte);
//			String traditional_HexString = CharacterConvert.convertByteToHexString(sha256_password);
//			String traditional_String = new String(sha256_password, ConfigMessage.ENCODE_FORMAT_UTF8);
//			System.out.println("traditional_HexString:" + traditional_HexString);
//			System.out.println("traditional_String:" + traditional_String);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//	new method
//		String new_HexString = sha256(user_password, true);
//		String new_String = sha256(user_password, false);
//		System.out.println("new_HexString:" + new_HexString);
//		System.out.println("new_String:" + new_String);
//	}
}
