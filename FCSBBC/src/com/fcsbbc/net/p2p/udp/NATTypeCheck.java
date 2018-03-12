package com.fcsbbc.net.p2p.udp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NATTypeCheck {
	private static Logger logger = LoggerFactory.getLogger(NATTypeCheck.class.getName());
    
	public static List<String> getNatType(String py_path, String py_file) {
		List<String> res_list = new ArrayList<String>();
		String py_file_path = py_path + "/" + py_file;
		File file = new File(py_file_path);  
        if (!file.exists()){  
        	logger.error("Python File does not exist!");  
        }
        else {
        	try {
 			   String call_command = "python" + " " + py_file_path;
 			   Process pr = Runtime.getRuntime().exec(call_command);
 			   BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
 			   String line = "";
 			   while ((line = in.readLine()) != null) {
 				   res_list.add(line);
 			   }
 			   in.close();
 			   pr.waitFor();
 		  } catch (Exception e) {
 			  logger.error(e.getMessage());
 		  }
		}
		return res_list;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String py_path = "src/com/scripts";
		String py_file = "nat_type_check.py";
		List<String> res_list = getNatType(py_path, py_file);
		if (res_list != null && !res_list.isEmpty() && res_list.size() == 1) {
			System.out.println(res_list.get(0));
		}
	}
//	private static final Map<String, String> DEFAULTS_MAP;  
//  static  
//  {  
//  	DEFAULTS_MAP = new HashMap<String, String>();  
//  	DEFAULTS_MAP.put("stun_port", "3478");  
//  	DEFAULTS_MAP.put("source_ip", "0.0.0.0");
//  	DEFAULTS_MAP.put("source_port", "54320");
//  }
//	public static List<String> getNatType() {
//		List<String> res_list = new ArrayList<String>();
//		try {
//			String py_path = "src/com/net/p2p/udp" + "/nat_type_check.py";
////			System.out.println(py_path);
//			Process pr = Runtime.getRuntime().exec("python" + " " + py_path);
//			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//			String line = "";
//			while ((line = in.readLine()) != null) {
//				res_list.add(line);
//			}
//			in.close();
//			pr.waitFor();
//		  } catch (Exception e) {
//			  e.printStackTrace();
//		  }
//		return res_list;
//	}
//	public final static String[] STUN_SERVERS = {
//			"stun.ekiga.net", 
//			"stun.ideasip.com", 
//			"stun.voiparound.com", 
//			"stun.voipbuster.com", 
//			"stun.voipstunt.com", 
//			"stun.voxgratia.org"};
//	public final static String[] stun_servers_list = STUN_SERVERS;
	
////    stun attributes
//    String MappedAddress = "0001";
//    String ResponseAddress = "0002";
//    String ChangeRequest = "0003";
//    String SourceAddress = "0004";
//    String ChangedAddress = "0005";
//    String Username = "0006";
//    String Password = "0007";
//    String MessageIntegrity = "0008";
//    String ErrorCode = "0009";
//    String UnknownAttribute = "000A";
//    String ReflectedFrom = "000B";
//    String XorOnly = "0021";
//    String XorMappedAddress = "8020";
//    String ServerName = "8022";
//    String SecondaryAddress = "8050";	//Non standard extension
////    types for a stun message
//    String BindRequestMsg = "0001";
//    String BindResponseMsg = "0101";
//    String BindErrorResponseMsg = "0111";
//    String SharedSecretRequestMsg = "0002";
//    String SharedSecretResponseMsg = "0102";
//    String SharedSecretErrorResponseMsg = "0112";
}
