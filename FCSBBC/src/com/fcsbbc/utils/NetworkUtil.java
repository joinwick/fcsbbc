/**
 * 
 */
package com.fcsbbc.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luo.changshu
 *
 */
public class NetworkUtil {
	private static Logger logger = LoggerFactory.getLogger(NetworkUtil.class.getName());
	
	public static boolean ipAddressCheck(String ipAddress) {
		boolean ipAddressValid = false;
		if (null != ipAddress && !ipAddress.equals("")) {
			StringBuffer regex = new StringBuffer().
					append("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.").
					append("([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.").
					append("([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.").
					append("([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
			Pattern pattern = Pattern.compile(regex.toString());
			ipAddressValid = pattern.matcher(ipAddress).matches();
		}
		return ipAddressValid;
	}
	
	public static boolean networkConnectionCheck(String publicIPAddress) {
		boolean networkConnection = false;
		if (ipAddressCheck(publicIPAddress)) {
			BufferedReader bufferedReader = null;
	        try {
	        	Process process = Runtime.getRuntime().exec("ping " + publicIPAddress);
	            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	            String line = null; 
	            StringBuffer stringBuffer = new StringBuffer(); 
	            while ((line = bufferedReader.readLine()) != null) { 
	            	stringBuffer.append(line); 
	            }
	            if (null != stringBuffer  && !stringBuffer.toString().equals("")) {
	            	if (stringBuffer.toString().indexOf("TTL") > 0) {
	            		networkConnection = true;
	            	}
	            }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.info("Network is interrupt, please check this!");
			}finally {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.info("BufferedReader close failed!");
				}
			}
		}
		return networkConnection;
	}
	
	public static void main(String[] args) {
		String publicIPAddress = ConfigMessage.SERVER_IP;
		System.out.println("publicIPAddress : " + publicIPAddress);
		System.out.println("publicIPAddress valid: " + ipAddressCheck(publicIPAddress));
		System.out.println("network connection : " + networkConnectionCheck(publicIPAddress));
	}
}
