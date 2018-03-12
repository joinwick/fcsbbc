package com.fcsbbc.cryptology;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.utils.ConfigMessage;

public class Base58 {
	private static Logger logger = LoggerFactory.getLogger(Base58.class.getName());
	//	Base58 character set
	private static final String BASE58CHARACTERSET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
	private static final BigInteger BASE = BigInteger.valueOf(BASE58CHARACTERSET.length());
	
	public static void main(String[] args) {
		String initialMessage = "IF1801@1900.05:10:阿海:Buy:Open:@2017-12-03 15:24:59";
		byte[] encodeByte = null;
		try {
			encodeByte = initialMessage.getBytes(ConfigMessage.ENCODE_FORMAT_UTF8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String encodeString = base58encode(encodeByte);
		System.out.println("encodeString = " + encodeString);
		String decodeMessage = encodeString;
		byte[] decodeByte = base58decode(decodeMessage);
		String decodeString = "";
		try {
			decodeString = new String(decodeByte, ConfigMessage.ENCODE_FORMAT_UTF8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("decodeString = " + decodeString);
		if (decodeString.equals(initialMessage)) {
			System.out.println("True!!!");
		}
	}
	
	/**
	 * Base58 encode
	 * @param message	encoding byte set
	 * @return	encoded string
	 */
	public static String base58encode(byte[] message) {
		BigInteger res = new BigInteger(1, message);
		StringBuilder stringBuilder = new StringBuilder();
		while (res.compareTo(BASE) >= 0) {
			BigInteger[] ret = res.divideAndRemainder(BASE);
			stringBuilder.insert(0, BASE58CHARACTERSET.charAt(ret[1].intValue()));
			res = ret[0];
		}
		stringBuilder.insert(0, BASE58CHARACTERSET.charAt(res.intValue()));
		for (byte b : message) {
			if (b == 0) {
				stringBuilder.insert(0, BASE58CHARACTERSET.charAt(0));
			}
			else {
				break;
			}
		}
//		return stringBuilder.toString();
		return String.valueOf(stringBuilder);
	}
	
	/**
	 * Base58 decode
	 * @param message	decoding string
	 * @return	decoded byte set
	 */
	public static byte[] base58decode(String message) {
		byte[] decodeByte = null;
		if (message != null && message.length() > 0) {
			BigInteger bigInteger = BigInteger.ZERO;
			int messageLength = message.length();
			for (int i = messageLength - 1; i >= 0; i--) {
				int base58Index = BASE58CHARACTERSET.indexOf(message.charAt(i));
				if (base58Index == -1) {
					logger.error("IllegalArgumentException");
//					throw new IllegalArgumentException();
				}
				bigInteger = bigInteger.add(BASE.pow(messageLength - 1 - i).multiply(BigInteger.valueOf(base58Index)));
			}
			byte[] bytes = bigInteger.toByteArray();
			boolean zeroOneFlag = bytes.length > 1 && bytes[0] == 0 && bytes[1] < 0;
			int fillZeroCount = 0;
			for (; fillZeroCount < messageLength && message.charAt(fillZeroCount) == BASE58CHARACTERSET.charAt(0); fillZeroCount++);
			decodeByte = new byte[bytes.length - (zeroOneFlag ? 1 : 0) + fillZeroCount];
			System.arraycopy(bytes, zeroOneFlag ? 1 : 0, decodeByte, fillZeroCount, decodeByte.length - fillZeroCount);
		}
		return decodeByte;
	}
}
