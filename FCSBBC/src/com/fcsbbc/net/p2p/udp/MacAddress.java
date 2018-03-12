package com.fcsbbc.net.p2p.udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class MacAddress {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();  
        System.out.println("Operation System=" + getOSName());  
        System.out.println("Mac Address=" + getMACAddress());  
        System.out.println("通过ip获取mac地址为:" + getMACAddress("172.19.104.111"));  
        long end = System.currentTimeMillis();  
        System.out.println("获取电脑MAC地址时间为:" + (end - start));
	}
	/**
	 * 获取操作系统名称
	 * @return
	 */
	public static String getOSName(){
		String os = "";
		os = System.getProperty("os.name");
		return os;
	}
	
	/**
	 * 根据操作系统类型获取MAC地址
	 * @return
	 */
	public static String getMACAddress(){
		String macAddress = null;
		String os = getOSName();
		if (os.startsWith("Windows")) {
			String command = "cmd.exe /c ipconfig /all";
			Process process;
			try {
				process = Runtime.getRuntime().exec(command);
				BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream(), System.getProperty("file.encoding")));
				String line;
				while ((line = bReader.readLine()) != null) {
					System.err.println("line=" + line);
					if (line.indexOf("Physical Address") > 0) {
						int index = line.indexOf(":");
						index += 2;
						macAddress = line.substring(index);
						break;
					}
				}
				bReader.close();
//				return macAddress.trim();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if (os.startsWith("Linux")) {
			String command = "/bin/sh -c ifconfig -a";
			Process process;
			try {
				process = Runtime.getRuntime().exec(command);
				BufferedReader bReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				while ((line = bReader.readLine()) != null) {
					if (line.indexOf("HWaddr") > 0) {
						int index = line.indexOf("HWaddr") + "HWaddr".length();
						macAddress = line.substring(index);
						break;
					}
				}
				bReader.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
//		macAddress = macAddress.trim();
		return macAddress;
	}
	/**
	 * 根据IP地址获取MAC地址
	 * @param ip
	 * @return
	 */
	public static String getMACAddress(String ip){
		String str = "", strMAC = "", macAddress = "";
		try {
            Process pp = Runtime.getRuntime().exec("nbtstat -a " + ip);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (int i = 1; i < 100; i++) {
                str = input.readLine();
                System.out.println("str=" + str);
                if (str != null) {
                    if (str.indexOf("MAC Address") > 1) {
                        strMAC = str.substring(str.indexOf("MAC Address") + 14, str.length());
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            return "Can't Get MAC Address!";
        }
        if (strMAC.length() < 17) {
            return "Error!";
        }
        macAddress = strMAC.substring(0, 2) + ":" + strMAC.substring(3, 5) + ":" + strMAC.substring(6, 8) + ":" + strMAC.substring(9, 11) + ":"
                + strMAC.substring(12, 14) + ":" + strMAC.substring(15, 17);
        return macAddress;
    }
}
