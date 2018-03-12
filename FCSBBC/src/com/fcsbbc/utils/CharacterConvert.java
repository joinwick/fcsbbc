package com.fcsbbc.utils;

import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Define convert method
 * 1.convertByteToHexString(byte[])
 * 2.convertHexStringToByte(String)
 * 3.convertStringToByte(String)
 * 4.convertByteToString(byte[])
 * @author luo.changshu
 *
 */
public class CharacterConvert {
	private static Logger logger = LoggerFactory.getLogger(CharacterConvert.class.getName());
	/**
	 * Convert Byte Array To Hex String
	 * @param message
	 * @return	HexString
	 */
	public static String convertByteToHexString(byte[] message) {
		String result = "";
		if (message != null && message.length > 0) {
			for (int i = 0; i < message.length; i++) {
				int temp = message[i] & 0xFF;
				String temp_result = Integer.toHexString(temp);
				if (temp_result.length() < 2) {
					result += "0" + temp_result;
				} else {
					result += temp_result;
				}
			}
		}
		return result;
	}
	/**
	 * Convert Hex String To Byte Array
	 * @param message
	 * @return	byte[]
	 */
	public static byte[] convertHexStringToByte(String message) {
		byte[] result = null;
		if (message != null && message.length() > 0) {
			result = new byte[message.length() / 2];
			for (int i = 0; i < result.length; i++) {
				String temp = message.substring(2 * i, 2 * i + 2);
				result[i] = ((byte)Integer.parseInt(temp, 16));
			}
		}
		return result;
	}
	/**
	 * Convert String To Byte
	 * @param message
	 * @return	byte[]
	 */
	public static byte[] convertStringToByte(String message) {
		byte[] result = null;
		if (message != null && message.length() > 0) {
			try {
				result = message.getBytes(ConfigMessage.ENCODE_FORMAT_UTF8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("Unsupported Encoding Exception:String2Byte", e.getMessage());
			}
		}
		return result;
	}
	/**
	 * Convert Byte To String
	 * @param message
	 * @return	String
	 */
	public static String convertByteToString(byte[] message) {
		String result = "";
		if (message != null && message.length > 0) {
			try {
				result = new String(message, ConfigMessage.ENCODE_FORMAT_UTF8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("Unsupported Encoding Exception:Byte2String", e.getMessage());
			}
		}
		return result;
	}
}
