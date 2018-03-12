package com.fcsbbc.net.p2p.udp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration {
	private  static Logger logger = LoggerFactory.getLogger(Configuration.class.getName());
	private static Properties props;
	
	// 在初始化时就加载配置文件（只需要加载一次）
	static{
		loadDefaultProperties();
	}
	
	private static void loadDefaultProperties(){
		InputStream inStream = null;
		try {
			URL url = Configuration.class.getClassLoader().getResource("system.properties");
			String path = URLDecoder.decode(url.getPath(), "UTF-8");
			inStream = new FileInputStream(path);
			
			props = new Properties();
			props.load(inStream);
			
			logger.info("--------------system.properties load successfully!----------------");
			
		} catch (Exception e) {
			logger.error("system.properties load failed!");
		}finally{
			if (inStream !=null) {
				try {
					inStream.close();
				} catch (IOException e) {
					logger.error("file closed failed!");
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String getString(String key){
		return props.getProperty(key);
	}
	
	public static ArrayList<String> getList(String result){
		ArrayList<String> resultList = new ArrayList<>();
		if (result != null) {
			String[] splitResult = result.split(",");
			for (int i = 0; i < splitResult.length; i++) {
				resultList.add(splitResult[i]);
			}
		}
		return resultList;
	}
	public static String getUniqueValue(String key){
		String uniqueValue = null;
		String result = props.getProperty(key);
		ArrayList<String> resultList = getList(result);
		int size = resultList.size();
		if (size > 0) {
			Random random = new Random();
			int randomIndex = random.nextInt(size);
			uniqueValue = resultList.get(randomIndex);
		}
		return uniqueValue;
	}
	public static int getInt(String key){
		return Integer.parseInt(props.getProperty(key));
	}
	
	// 测试ClassLoader.getResource()
//	public static void main(String[] args) throws Exception {
//		System.out.println(getUniqueValue("server.ip"));
//	}
}
