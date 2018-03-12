package com.fcsbbc.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.cryptology.HashModule;
import com.fcsbbc.net.p2p.udp.UserInfoBean;

public class UtilTool {
	
	private static Logger logger = LoggerFactory.getLogger(UtilTool.class);
	
	/**
	 * 获取主机的IP信息
	 * @return	String类型IP信息
	 */
	public static String getLocalAddress(){
		String ip = null;
		ArrayList<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
				logger.info("local network interface is: " + networkInterface.getName());
				if (networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.isVirtual()) {
					Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						InetAddress addr = (InetAddress) addresses.nextElement();
						if (addr != null && !addr.isLoopbackAddress()  
								&& addr instanceof Inet4Address 
								&& addr.getHostAddress().indexOf("/") == -1) {
							logger.info("Local IP = " + addr.getHostAddress());
							ipList.add(addr.getHostAddress());
						}
					}
				}
			}
			if (ipList.size() > 0) {
				System.out.println("initial localIP:" + ipList.get(0));
				String localIP = null;
				try {
					if (isWindowsOS()) {
						localIP = InetAddress.getLocalHost().getHostAddress();
//						localIP = getIP(InetAddress.getLocalHost());
						System.out.println("Windows initial LocalIP:" + localIP);
						logger.info("Windows transfer LocalIP:" + getIP(InetAddress.getLocalHost()));
					}
					else {
						boolean bFindIP = false;
						Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface.getNetworkInterfaces();
						while (netInterfaces.hasMoreElements()) {
							if(bFindIP){
								break;
							}
							NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
							Enumeration<InetAddress> ips = ni.getInetAddresses();
							while (ips.hasMoreElements()) {
								InetAddress tempInetAddress = (InetAddress) ips.nextElement();
								if( tempInetAddress.isSiteLocalAddress() && 
										!tempInetAddress.isLoopbackAddress() && 
										tempInetAddress.getHostAddress().indexOf(":")==-1){
									localIP = getIP(tempInetAddress);
									System.out.println("Linux LocalIP:" + localIP);
						            bFindIP = true;
						            break; 
						           }
							}
						}
					}
					System.out.println("evluation localIP:" + localIP);
					for (int i = 0; i < ipList.size(); i++) {
						if (ipList.get(i).equals(localIP) || localIP.contains(ipList.get(i))) {
							ip = ipList.get(i);
						}
					}
					if (ip == null) {
						Random random = new Random();
						int index = random.nextInt(ipList.size());
						ip = ipList.get(index);
						System.out.println("choice initial LocalIP:" + localIP);
					}
				} catch (UnknownHostException e) {
					logger.error("please check os type(current support windows & liunx)!");
				}
			}
		} catch (SocketException e) {
			logger.error("Can not Get Valid IP!");
		}
		return ip;
	}
	/**
	 * 
	 * @return
	 */
	public static boolean isWindowsOS(){
		boolean windowOSFlag = false;
		String osName = System.getProperty("os.name");
		if(osName.toLowerCase().indexOf("windows")>-1){
			windowOSFlag = true;
		}
		return windowOSFlag;
	}
	/**
	 * 获取InetAddress
	 * @param ip
	 * @return
	 */
	public static InetAddress getInetAddress(String ip){
		InetAddress inetAddress = null;
		if (ip != null) {
			try {
				inetAddress = InetAddress.getByName(ip);
			} catch (UnknownHostException e) {
//				e.printStackTrace();
				logger.error("Can not get InetAddress from String-format IP!");
			}
		}
		return inetAddress;
	}
	/**
	 * 获取IP(String)
	 * @param inetAddress
	 * @return
	 */
	public static String getIP(InetAddress inetAddress){
		String ip = null;
		if (inetAddress != null) {
			ip = inetAddress.toString();
//			System.out.println("ip= " + ip);
			if (ip != null) {
				if (ip.contains("/")) {
					ip = ip.substring(ip.lastIndexOf("/") + 1 );
				}
				String regexString=".*(\\d{3}(\\.\\d{1,3}){3}).*";
				ip = ip.replaceAll(regexString,"$1");
			}
//			if (ip != null) {
//				String regexString=".*(\\d{3}(\\.\\d{1,3}){3}).*";
//				ip = ip.replaceAll(regexString,"$1");
//			}
		}
		return ip;
	}
//	public static void main(String[] args){
//		///47.92.135.245:8102
//		try {
////			System.out.println(InetAddress.getByName("/47.92.135.245").getHostAddress());
//			System.out.println(getIP(InetAddress.getByName("47.92.135.245")));
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	/**
	 * 传输的信息经过Base64编码
	 * @param originalInfo
	 * @return
	 */
	public static String getEncode64Info(String originalInfo){
		String encode64 = null;
		if (originalInfo != null) {
			try {
				encode64 = Base64.getEncoder().encodeToString(originalInfo.getBytes(ConfigMessage.ENCODE_FORMAT_UTF8));
			} catch (UnsupportedEncodingException e) {
				logger.error("Encoding Failed, Unsupported Encoding!");
//				e.printStackTrace();
			}
		}
		return encode64;
	}
	/**
	 * 接收的信息经过Base64解码
	 * @param encode64Info
	 * @return
	 */
	public static String getDecode64Info(String encode64Info){
		String decode64 = null;
		if (encode64Info != null) {
			try {
				decode64 = new String(Base64.getDecoder().decode(encode64Info.getBytes(ConfigMessage.ENCODE_FORMAT_UTF8)), ConfigMessage.ENCODE_FORMAT_UTF8);
			} catch (UnsupportedEncodingException e) {
				logger.error("Decoding Failed, Unsupported Encoding!");
//				e.printStackTrace();
			}
		}
		
		return decode64;
	}
	public static byte[] getEncode64Bytes(String info){
		byte[] buf = null;
		try {
			buf = info.getBytes(ConfigMessage.ENCODE_FORMAT_UTF8);
		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
			logger.error("Unsupported Encoding-String Convert Byte[]!");
		}
		return buf;
	}
	public static ConcurrentHashMap<String, UserInfoBean> extractUserInfoBean(String receivedEncodedInfo){
		ConcurrentHashMap<String, UserInfoBean> cMap = new ConcurrentHashMap<String, UserInfoBean>();
		if (receivedEncodedInfo != null) {
			String decodeInfo = UtilTool.getDecode64Info(receivedEncodedInfo);
			String[] temp = decodeInfo.split(ConfigMessage.MESSAGE_SPLIT_FLAG);
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].startsWith(ConfigMessage.NODE_LOCAL_INFO_FLAG)) {
//					UserInfoBean userInfo = UserInfoBean.getInstance();
					UserInfoBean userInfo = new UserInfoBean();
					String[] ipDetails = temp[i].split(ConfigMessage.INFO_SPLIT_FLAG);
					String localIP = ipDetails[0].split(ConfigMessage.DATA_SPLIT_FLAG)[1];
					int localPort = Integer.parseInt(ipDetails[0].split(ConfigMessage.DATA_SPLIT_FLAG)[2]);
					String natIP = ipDetails[1].split(ConfigMessage.DATA_SPLIT_FLAG)[1];
					int natPort = Integer.parseInt(ipDetails[1].split(ConfigMessage.DATA_SPLIT_FLAG)[2]);
//					String updateTimeStamp = ipDetails[2].split(ConfigMessage.DATA_SPLIT_FLAG)[1];
//					System.out.println(localIP + ":" +localPort + "----------" + natIP + ":" + natPort);
					userInfo.setHostIP(localIP);
					userInfo.setHostPort(localPort);
					userInfo.setNatIP(natIP);
					userInfo.setNatPort(natPort);
					userInfo.setConnFlag(false);
//					userInfo.setUpdateTimeStamp(updateTimeStamp);
					String uniqueAddress = userInfo.getUniqueAddress();	//172.19.104.111:105.22.114.123
//					System.out.println(uniqueAddress + userInfo.isConnFlag());
					if (!cMap.containsKey(uniqueAddress)) {
						cMap.put(uniqueAddress, userInfo);
					}
					else {
						logger.info("IP Address(" + uniqueAddress + ")Exists!");
					}
				}
			}
		}
		return cMap;
	}
//	public static void main(String[] args){
//		String info = "Get_Random_Nodes_OK,172.26.14.248:8102,101.231.205.185:51021,LocalIP:223.166.55.180:8102#NATIP:223.166.55.180:8102#ConnTime:1500915288156,SERVER,1.20170616:baron_0.1";
//		String encodeInfo = UtilTool.getEncode64Info(info);
//		extractUserInfoBean(encodeInfo);
//	}
	
	public static ArrayList<UserInfoBean> getAllClientList(ConcurrentHashMap<String, UserInfoBean> cMap){
		ArrayList<UserInfoBean> clientList = new ArrayList<UserInfoBean>();
		if (cMap.size() > 0) {
			Set<String> keySet = cMap.keySet();
			Iterator<String> keys = keySet.iterator();
			while (keys.hasNext()) {
				String key = keys.next();
				UserInfoBean userInfoBean = cMap.get(key);
				clientList.add(userInfoBean);
			}
		}
		return clientList;
	}
	
	//	get local time-stamp
	public static Long getLocalTimeStamp(){
		Long timeStamp = 0L;
//		Calendar calendar = Calendar.getInstance(Locale.CHINA);
//		timeStamp = calendar.getTimeInMillis();
		timeStamp = System.currentTimeMillis();
		return timeStamp;
	}
	//	get UTC time-stamp
	public static Long getUTCTimeStamp(){
		Long timeStamp = 0L;
//		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		Calendar calendar = Calendar.getInstance(Locale.getDefault());	//取得本地时间
		int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);			//取得时间偏移量
		int dstOffset = calendar.get(Calendar.DST_OFFSET);				//取得夏令时差
		calendar.add(Calendar.MILLISECOND, - (zoneOffset + dstOffset));	//从本地时间里扣除这些差量，即可以取得UTC时间
		timeStamp = calendar.getTimeInMillis();
		return timeStamp;
	}
	
	public static String formatDate(Date dateTime, int timeFormat) {
		if (timeFormat == 0) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
		}
		else if (timeFormat == 1) {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(dateTime);
		}
		else {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateTime);
		}
	}
	
	public static String formatDate(Date dateTime, TimeZone timeZone, int timeFormat) {
		String formatedDateTime = "";
		SimpleDateFormat simpleDateFormat;
		if (timeFormat == 0) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		else if (timeFormat == 1) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
		else {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		simpleDateFormat.setTimeZone(timeZone);
		simpleDateFormat.format(dateTime);
		return formatedDateTime;
	}
	
	//	get date time format
	public static String getNormalTime(Long timeStamp, int timeFormat){
		String normalTime = null;
		Date date = new Date(timeStamp);
		normalTime = formatDate(date, timeFormat);
		return normalTime;
	}
	
	public static String timestampToDateTime(String timestamp, int timeFormat) {
		String datetime = "";
		SimpleDateFormat simpleDateFormat = null;
        if (timeFormat == 0) {
        	simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
        else if (timeFormat == 1) {
        	simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
        if (timestamp != null && timestamp.length() > 0) {
        	long datetimeLong = new Long(timestamp);
            Date date = new Date(datetimeLong);
            datetime = simpleDateFormat.format(date);
		}
        return datetime;
	}
	
	public static SimpleDateFormat GetDateFormat(TimeZone timeZone, int timeFormat) {
		SimpleDateFormat simpleDateFormat = null;
		if (timeFormat == 0) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		else if (timeFormat == 1) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
		else {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		}
		simpleDateFormat.setTimeZone(timeZone);
		return simpleDateFormat;
	}
	
	public static String datetimeToTimeStamp(String datetime, int timeFormat) {
		String timeStamp = "";
		SimpleDateFormat simpleDateFormat = null;
		if (timeFormat == 0) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		else if (timeFormat == 1) {
			simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		}
        Date date;
		try {
			if (datetime != null && datetime.length() > 0) {
				date = simpleDateFormat.parse(datetime);
				Long timeStampLong = date.getTime();
		        timeStamp = String.valueOf(timeStampLong);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.info("Parse Exception,please check parameters!" + e.getMessage());
		}
		return timeStamp;
	}
	
	public static String UTCToLocal(String utcTimeStamp, int utcTimeFormat) {
		String localDateTime = "";
		if ((utcTimeStamp != null && utcTimeStamp.length() > 0) && utcTimeFormat >= 0) {
			String utcDateTime = timestampToDateTime(utcTimeStamp, utcTimeFormat);
			
			SimpleDateFormat utcSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			utcSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date UtcDate = null;
	        try {
	            UtcDate = utcSimpleDateFormat.parse(utcDateTime);
	        } catch (Exception e) {
	        	logger.info("Parse Exception,please check parameters!" + e.getMessage());
	        }
			Calendar calendar = Calendar.getInstance(Locale.getDefault());
			utcSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//			utcSimpleDateFormat.setTimeZone(TimeZone.getDefault());
			utcSimpleDateFormat.setTimeZone(calendar.getTimeZone());
			localDateTime = utcSimpleDateFormat.format(UtcDate.getTime());
		}
		return localDateTime;
	}
	
	public static String UTCTimeStampToLocalDateTime(String utcTimeStamp, int utcTimeFormat, int localTimeFormat) {
		String localDateTime = "";
		SimpleDateFormat simpleDateFormat = null;
//		Date utcDateTime = new Date(Long.parseLong(utcTimeStamp));
		if ((utcTimeStamp != null && utcTimeStamp.length() > 0) && utcTimeFormat >= localTimeFormat) {
			String utcDateTime = timestampToDateTime(utcTimeStamp, utcTimeFormat);
			simpleDateFormat = GetDateFormat(TimeZone.getTimeZone("UTC"), utcTimeFormat);
			Date UtcDateTime = null;
			try {
				UtcDateTime = simpleDateFormat.parse(utcDateTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (utcDateTime != null) {
				Calendar calendar = Calendar.getInstance(Locale.getDefault());
				simpleDateFormat = GetDateFormat(calendar.getTimeZone(), localTimeFormat);
				localDateTime = simpleDateFormat.format(UtcDateTime.getTime());
			}
		}
		return localDateTime;
	}
	
	public static String SourceTimeToDestinationTime(String convertTime, TimeZone sourceTimeZone, TimeZone destinationTimeZone, int sourceTimeFormat, int destinationTimeFormat) {
		String destinationTime = "";
		SimpleDateFormat simpleDateFormat = null;
//		Date utcDateTime = new Date(Long.parseLong(utcTimeStamp));
		if ((convertTime != null && convertTime.length() > 0) && sourceTimeFormat >= destinationTimeFormat && (sourceTimeZone != null && destinationTimeZone != null)) {
//			String utcDateTime = timestampToDateTime(convertTime, utcTimeFormat);
			simpleDateFormat = GetDateFormat(sourceTimeZone, sourceTimeFormat);
			Date UtcDateTime = null;
			try {
				UtcDateTime = simpleDateFormat.parse(convertTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (convertTime != null) {
//				Calendar calendar = Calendar.getInstance(Locale.getDefault());
				simpleDateFormat = GetDateFormat(destinationTimeZone, destinationTimeFormat);
				destinationTime = simpleDateFormat.format(UtcDateTime.getTime());
			}
		}
		return destinationTime;
	}
	
	public static ArrayList<Integer> generateRandomNumber(int randomNo, int ClientCount){
		ArrayList<Integer> randomNumber = new ArrayList<Integer>();
		if (randomNo <= ClientCount) {
			ArrayList<Integer> tempClientList = new ArrayList<Integer>();
			for (int i = 0; i < ClientCount; i++) {
				tempClientList.add(i);
			}
			Random random = new Random();
			int index = 0;
			while (index < randomNo) {
				int tempClientSize = tempClientList.size();
				int randomInt = random.nextInt(tempClientSize);
				randomNumber.add(tempClientList.get(randomInt));
				int value = tempClientList.get(randomInt);
				Iterator<Integer> iterator = tempClientList.iterator();
				while (iterator.hasNext()) {
					if (iterator.next() == value) {
						iterator.remove();
					}
				}
				index ++;
			}
		}else {
			for (int i = 0; i < ClientCount; i++) {
				randomNumber.add(i);
			}
		}
		
		return randomNumber;
	}
	/**
	 * store [ip,port]
	 * @param receivedEncodedInfo
	 * @param index
	 * @return
	 */
	public static ArrayList<String> getDestinationIP(String receivedEncodedInfo, int index){
		ArrayList<String> destinationIP = new ArrayList<String>();
		if (receivedEncodedInfo != null && index > 0) {
			String receivedInfo = UtilTool.getDecode64Info(receivedEncodedInfo);
			String[] ipInfo = receivedInfo.split(ConfigMessage.MESSAGE_SPLIT_FLAG)[index].split(ConfigMessage.DATA_SPLIT_FLAG);
			for (int i = 0; i < ipInfo.length; i++) {
				destinationIP.add(ipInfo[i]);
			}
		}
		return destinationIP;
	}
	
	public static String removeFilePath(String filePath){
		String fileName = null;
		if (filePath != null && filePath.length() > 0) {
			if ("/".equals(getFileSeparator())) {
				if(filePath.matches("^[A-z]:/S+$")) {
					fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
				}
			}
			else if ("\\".equals(getFileSeparator())) {
				if(filePath.matches("^[A-z]:\\\\\\S+$")) {
					fileName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
				}
			}
		}
		return fileName;
    }
	
	public static boolean filePathCheck(String filePath) {
		boolean filePathValid = false;
//		String reg = "([^<>/\\\\|:\"\"\\*\\?]+)\\.\\w+$+";
		String reg = "([^<>/\\\\\\|:\"\"\\*\\?]+)\\.\\w+$";
		Matcher matcher = Pattern.compile(reg).matcher(filePath);
		if (matcher.find()) {    
		     filePathValid = true;
		}
		return filePathValid;
	}
	
	public static boolean fileValid(String filePath) {
		boolean fileValid = false;
		if (filePath != null && filePath.length() > 0) {
			File file = new File(filePath);
			if (file.exists()) {
				System.out.println("File Exists!");
				if (!file.isDirectory()) {
					fileValid = true;
				}
			}
		}
		return fileValid;
	}
	
	public static int fileValidCheck(String filePath) {
		int fileCheck = -1;
		if (filePath != null && filePath.length() > 0) {
			File file = new File(filePath);
			if (file.exists()) {
				if (!file.isDirectory()) {
					fileCheck = 0;
				}
				else {
					fileCheck = 2;
				}
			}
			else {
				fileCheck = 1;
			}
		}
		return fileCheck;
	}
	
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
	
	public static String getFileSuffix(String filePath) {
		return filePath.substring(filePath.lastIndexOf(".") + 1);
	}
	
	public static String cryptologyTelephone(String telephone) {
		String cryptologyTelephone = null;
		if (telephone != null && telephone.length() > 0) {
			cryptologyTelephone = telephone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
		}
		return cryptologyTelephone;
	}
	
	
	/** 
     * 大陆号码或香港号码均可 
     */  
    public static boolean isPhoneLegal(String telephone) {  
        return isChinaPhoneLegal(telephone) || isHKPhoneLegal(telephone);  
    }  
  
    /** 
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有:
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */  
    public static boolean isChinaPhoneLegal(String telephone) {
    	boolean telephoneValid = false;
    	if (telephone != null && telephone.length() > 0) {
    		String telephoneRegularExpression = "^((13[0-9])|(14[579])|(15[^4])|(16[6])|(17[0-1,3,5-8])|(18[0-9])|(19[8-9]))\\d{8}$";
            Pattern pattern = Pattern.compile(telephoneRegularExpression);
            Matcher matcher = pattern.matcher(telephone);
            telephoneValid = matcher.matches();
		}
        return telephoneValid;
    }  
  
    /** 
     * 香港手机号码8位数，5|6|8|9开头+7位任意数 
     */  
    public static boolean isHKPhoneLegal(String telephone) {
    	boolean telephoneValid = false;
    	if (telephone != null && telephone.length() > 0) {
    		String telephoneRegularExpression = "^(5|6|8|9)\\d{7}$";
            Pattern pattern = Pattern.compile(telephoneRegularExpression);
            Matcher matcher = pattern.matcher(telephone);
            telephoneValid = matcher.matches();
    	}
        return telephoneValid;  
    }
	
    public static int generateRandomIdentityCode() {
    	return (int) ((Math.random() * 9 + 1) * 100000);
    }
    
    /**
     * 验证密码是否符合下列要求:①同时有数字和字母;②长度最小6;③最大16
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
    	boolean passwordValid = false;
    	if (password != null && password.length() > 0) {
    		String passwordRegularExpression = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
    		Pattern pattern = Pattern.compile(passwordRegularExpression);
    		Matcher matcher = pattern.matcher(password);
    		passwordValid = matcher.matches();
		}
    	return passwordValid;
    }
    
    /**
     * 验证用户角色与其标志是否相符
     * @param memberType
     * @param userRole
     * @return
     */
    public static boolean isMatchedUserRole(String memberType, int userRole) {
    	boolean memberTypeMatched = false;
    	if (null != memberType && !memberType.equals("") && userRole > 0) {
			switch (userRole) {
			case 1:
				if (LocalizeConfig.USER_ROLE_CUSTOMER.equals(memberType)) {
					memberTypeMatched = true;
				}
				break;
			case 2:
				if (LocalizeConfig.USER_ROLE_MEMBER.equals(memberType)) {
					memberTypeMatched = true;
				}
				break;
			case 3:
				if (LocalizeConfig.USER_ROLE_REGULATOR.equals(memberType)) {
					memberTypeMatched = true;
				}
				break;
			default:
				logger.info("User Role should be in[1, 2, 3]!");
				break;
			}
		}
    	return memberTypeMatched;
    }
    
    public static int convertUserRole(String memberType) {
    	int userRole = -1;
    	if (null != memberType && !memberType.equals("")) {
			if (memberType.equals(LocalizeConfig.USER_ROLE_CUSTOMER)) {
				userRole = 1;
			}
			else if (memberType.equals(LocalizeConfig.USER_ROLE_MEMBER)) {
				userRole = 2;
			}
			else if (memberType.equals(LocalizeConfig.USER_ROLE_REGULATOR)) {
				userRole = 3;
			}
			else {
				userRole = 0;
			}
		}
    	return userRole;
    }
    
	public static void main(String[] args){
		System.out.println(HashModule.sha256("1111", true));
		Object[] objects = new Object[8];
		System.out.println(null != objects[1] && !objects[0].equals(""));
		String filePath = "D:\\DIP\\P1_HistogramEqualization.cer";
		System.out.println("Valid file      : " + fileValid(filePath));
		String telephone = "19981241244";
		String password = " ";
		System.out.println("Valid telephone : " + isChinaPhoneLegal(telephone));
		System.out.println("Valid password  : " + isValidPassword(password));
		System.out.println("Valid userrole  : " + isMatchedUserRole("会员", 2));
		String str1 = null;
		String str2 = "1";
		System.out.println("equal check : " + str2.equals(str1));
		System.out.println("String Test : " + "".length());
		System.out.println("DateTime    : " + timestampToDateTime(String.valueOf("1519537513701"), 1));
//		System.out.println(removeFilePath("c:\\my"));
//		System.out.println(filePathCheck("c:\\\\my\\\\my\\\\my\\\\ny\\\\my\\\\my\\\\yy\\\\yy\\\\yy\\\\yy\\\\yy\\\\yy\\\\y\\\\my"));
//		System.out.println(getFileSuffix("/too/my.sq"));
//		String dataTime = "2018-02-02 09:15:48.556";
		Long localTimeStamp = getLocalTimeStamp();
//		String localDataTime = getNormalTime(localTimeStamp, 1);
//		System.out.println("Local Time1 = " + localDataTime);
		String localDateTime2 = timestampToDateTime(String.valueOf(localTimeStamp), 1);
		System.out.println("Local Time2 = " + localDateTime2);
		Long UTCTimeStamp = getUTCTimeStamp();
		System.out.println("UTC    Time = " + timestampToDateTime(String.valueOf(UTCTimeStamp), 1));
//		System.out.println("LTS = " + localTimeStamp);
//		System.out.println("UTS = " + UTCTimeStamp);
//		System.out.println("UTC-LOC = " + UTCTimeStampToLocalDateTime(String.valueOf(UTCTimeStamp), 1, 1));
		String sourceTime = timestampToDateTime(String.valueOf(UTCTimeStamp), 1);
		System.out.println("UTC-LOC     = " + SourceTimeToDestinationTime(sourceTime, TimeZone.getTimeZone("UTC"), ConfigMessage.LOCAL_TIME_ZONE, 1, 1));
		System.out.println("UTC-GMT     = " + SourceTimeToDestinationTime(sourceTime, TimeZone.getTimeZone("UTC"), TimeZone.getTimeZone("Asia/Shanghai"), 1, 1));
		
		String convert_result = SourceTimeToDestinationTime("2017-12-06 10:00:00.000", ConfigMessage.LOCAL_TIME_ZONE, ConfigMessage.UTC_TIME_ZONE, 1, 1);
		System.out.println("Convert Res = " + datetimeToTimeStamp(convert_result, 1));
//		sourceTime = timestampToDateTime(String.valueOf(localTimeStamp), 1);
//		String[] timeList = {"2018-02-01 16:16:23", "2018-02-01 16:17:23", "2018-02-01 16:18:23",
//				"2018-02-01 16:19:23", "2018-02-01 16:20:23", "2018-02-01 16:21:23", "2018-02-01 16:22:23",
//				"2018-02-01 16:23:23", "2018-02-01 16:24:23", "2018-02-01 16:25:23", "2018-02-01 16:26:23",
//				"2018-02-01 16:27:23", "2018-02-01 16:27:23", "2018-02-01 16:29:23", "2018-02-01 16:30:23"};
//		
//		for (int i = 0; i < timeList.length; i++) {
//			String currTime = timeList[i] + ".123";
//			String utcTimeStamp = SourceTimeToDestinationTime(currTime, ConfigMessage.LOCAL_TIME_ZONE, TimeZone.getTimeZone("UTC"), 1, 1);
//			System.out.println("LOC-UTC = " + datetimeToTimeStamp(utcTimeStamp, 1));
//		}
		
		
		
		//		String UTC = "2018-02-02 02:10:18.088";
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
////        System.out.println(TimeZone.getTimeZone("UTC"));
//        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date UtcDate = null;
//        try {
//            UtcDate = simpleDateFormat.parse(UTC);
//        } catch (Exception e) {
//            return;
//        }
//        System.out.println("UtcDate = " + UtcDate);
//
//        SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        localFormater.setTimeZone(TimeZone.getDefault());
//        String localTime = localFormater.format(UtcDate.getTime());
//        System.out.println("UtcDate.getTime() = " + UtcDate.getTime());
//        System.out.println("local time = " + localTime);
		
		
//		String timestamp = "1517505746113";
		
//		System.out.println(getLocalAddress());
////		try {
////			System.out.println(InetAddress.getLocalHost().getHostAddress());
////		} catch (UnknownHostException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////		System.err.println(getEncode64Info("localIP"));
////		System.err.println(getDecode64Info("bG9jYWxJUA=="));
//		
//		System.out.println(generateRandomNumber(2, 7));
////		System.out.println(System.currentTimeMillis());
//		Long timeStamp = getUTCTimeStamp();
//		System.out.println(getNormalTime(timeStamp));
//		
////		Calendar calendar = Calendar.getInstance();
////		calendar.setTimeZone(TimeZone.getTimeZone("GMT-8"));
////		Long date = calendar.getTimeInMillis();
////		System.out.println(getNormalTime(date));
//		System.out.println(getNormalTime(getLocalTimeStamp()));
//		InetAddress inetAddress = null;
//		try {
//			inetAddress = InetAddress.getLocalHost();
//			System.out.println(inetAddress.toString());
//			System.out.println(getIP(inetAddress));
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String initialInfo = ConfigMessage.CLIENT_CONN_SERVER_FLAG_OK + "," 
//		+ "LocalIP:172.19.104.113:8103#NATIP:101.231.205.185:53804#ConnTime:1497564881061,"
//		+ "LocalIP:172.19.104.112:8103#NATIP:101.231.205.185:53803#ConnTime:1497564881061," 
//		+ "LocalIP:172.19.104.111:8103#NATIP:101.231.205.185:53805#ConnTime:1497564881061,"
//		+ "LocalIP:172.19.104.114:8103#NATIP:101.231.205.185:53802#ConnTime:1497564881061,"
//		+ "1.20170603:baron_0.1";
//		String encode64_initialInfo = UtilTool.getEncode64Info(initialInfo);
//		InetAddress hostIP = UtilTool.getInetAddress("172.19.104.111");
//		InetAddress natIP = UtilTool.getInetAddress("101.231.205.185");
//		Long updateTimeStamp = UtilTool.getUTCTimeStamp();
//		UserInfoBean requestNode = new UserInfoBean(hostIP, 8102, natIP, 53805, updateTimeStamp);
//		System.out.println("LocalClient:" + requestNode.getUserInfo());
////		String encodeInfo = UtilTool.getEncode64Info(initialInfo);
//		ClientData clientData = ClientData.getInstance();	//实例化单例模式对象
//		ConcurrentHashMap<String, UserInfoBean> cMap = UtilTool.extractUserInfoBean(encode64_initialInfo);
//		clientData.setcMap(cMap);	//初始化对象集合
//		System.out.println(clientData.getClientCount(false));
////		String clientList = clientData.getAllClient();
////		System.out.println(clientList.split(",").length);
//		
////		System.out.println(clientData.getAllClient(3, requestNode));
//		
//		String updateInfo = ConfigMessage.CLIENT_CONN_SERVER_FLAG_OK + "," 
//				+ "LocalIP:172.19.104.116:8103#NATIP:101.231.205.185:53804#ConnTime:1497564881061,"
//				+ "LocalIP:172.19.104.117:8103#NATIP:101.231.205.185:53803#ConnTime:1497564881061,"
//				+ "1.20170603:baron_0.1";
//		String encode64_updateInfo = UtilTool.getEncode64Info(updateInfo);
//		if (clientData.getClientCount(false) > 0) {
//			ConcurrentHashMap<String, UserInfoBean> cMap2 = UtilTool.extractUserInfoBean(encode64_updateInfo);
//			System.out.println(cMap2.size());
//			//逐项添加
////			ArrayList<UserInfoBean> clientList = UtilTool.getAllClientList(cMap2);
//			clientData.setcMap(cMap);
//			ArrayList<UserInfoBean> clientList = clientData.getAllClientList();
//			int size = clientList.size();
//			if (size > 0) {
//				for (int i = 0; i < size; i++) {
//					UserInfoBean userInfoBean = clientList.get(i);
//					String key = userInfoBean.getUniqueAddress();
//					clientData.addClient(key, userInfoBean);
//					System.out.println(userInfoBean.isConnFlag());
//				}
//			}
//		}
//		else {
//			clientData.setcMap(cMap);
//		}
//		
//		System.out.println(clientData.getClientCount(false));
//		
////		System.out.println(getEncode64Info("术语介绍"));
//		String encode64 = getEncode64Info("Client_Conn_Server_OK,172.21.114.207:8102,101.231.205.185:8102,NONE,1.20170616:baron_0.1");
//		System.out.println(encode64);
////		String parentString="lcslcs/127.0.0.1:8080/ipipi";
////		String regexString=".*(\\d{3}(\\.\\d{1,3}){3}).*";
////		String IPString=parentString.replaceAll(regexString,"$1");
////		System.out.print(IPString);
//		
//		System.out.println(getDestinationIP(encode64, 2).get(0)+ ":" + getDestinationIP(encode64, 2).get(1));
////		String[] test = "12".split(",");
////		System.out.println(test.length);
	}
	
}
