package com.fcsbbc.net.p2p.udp;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.UtilTool;
/**
 * 将共享数据封装在对象(ClientData)中（操作数据的方法也在该对象中完成）
 * @author luo.changshu
 *
 */
public class ClientData {
	private static Logger logger = LoggerFactory.getLogger(ClientData.class);
	//单例模式
	private static class Holder{
		private final static ClientData INSTANCE_CLIENT_DATA = new ClientData();
	}
	
	private ClientData(){
		
	}
	
	public static ClientData getInstance(){
		return Holder.INSTANCE_CLIENT_DATA;
	}
	
	private ConcurrentHashMap<String, UserInfoBean> cMap = new ConcurrentHashMap<String, UserInfoBean>();
//	private ConcurrentHashMap<String, UserInfoBean> cMap;
	
	public synchronized ConcurrentHashMap<String, UserInfoBean> getcMap() {
		return cMap;
	}

	public synchronized void setcMap(ConcurrentHashMap<String, UserInfoBean> cMap) {
		this.cMap = cMap;
	}
	/**
	 * 判断是否所有的客户端都已经成功建立网络连接
	 * @return
	 */
	public synchronized boolean judgeConnResult(){
		boolean connFlag = true;
		ArrayList<UserInfoBean> clientList = getAllClientList();
		int size = clientList.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				if (!clientList.get(i).isConnFlag()) {
					connFlag = false;
				}
			}
		}
		return connFlag;
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public synchronized boolean containsKey(String key){
		boolean containFlag = false;
		if (cMap.containsKey(key)) {
			containFlag = true;
		}
		return containFlag;
	}
	/**
	 * 依据key获取对象
	 * @param key
	 * @return
	 */
	public synchronized UserInfoBean getUserInfoBean(String key){
		UserInfoBean userInfoBean = new UserInfoBean();
		if (key != null && cMap.containsKey(key)) {
			userInfoBean = cMap.get(key);
		}
		return userInfoBean;
	}
	/**
	 * 添加在线的客户端
	 * @param id
	 * @param client
	 */
	public synchronized void addClient(String key, UserInfoBean userInfoBean){
//		System.out.println("key=" + key + "userInfoBean=" + userInfoBean);
		if (!cMap.containsKey(key)) {
			cMap.put(key, userInfoBean);
			logger.info("Add Client(" + key + ") Successfully!");
		}else {
			logger.error("Client(" + key + ") Exists!");
		}
		
	}
	/**
	 * 判断添加UserInfoBean是否成功
	 * @param userInfoBean
	 * @param key
	 * @return
	 */
	public synchronized boolean addClient(UserInfoBean userInfoBean, String key){
		boolean addedFlag = false;
		if (!cMap.containsKey(key)) {
			cMap.put(key, userInfoBean);
			addedFlag = true;
		}
		else {
			logger.error("Client(" + key + ") Exists!");
		}
		return addedFlag;
	}
	/**
	 * 删除离线的客户端或拒绝服务的客户端
	 * @param key
	 */
	public synchronized void removeInvalidClient(String key){
		if (cMap.containsKey(key)) {
			cMap.remove(key);
			logger.info("Remove Client(" + key + ")");
		}else {
			logger.error("Client(" + key + ") does't exist!");
		}
	}
	/**
	 * 
	 * @param connSuccess
	 * @return
	 */
	public synchronized int getClientCount(boolean connSuccess){
		if (!connSuccess) {
			return cMap.size();
		}
		else {
			Set<String> keySet = cMap.keySet();
			Iterator<String> keys = keySet.iterator();
			int count = 0;
			while (keys.hasNext()) {
				String key = keys.next();
				UserInfoBean userInfoBean = cMap.get(key);
				if (userInfoBean.isConnFlag()) {
					count ++;
				}
			}
			return count;
		}
	}
	/**
	 * 打印当前在线节点列表
	 */
	public synchronized void showAllClient(){
		Set<String> keySet = cMap.keySet();
		Iterator<String> keys = keySet.iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			UserInfoBean userInfoBean = cMap.get(key);
			System.out.println(userInfoBean.getUserInfo());
		}
	}
	/**
	 * 得到指定数量的当前在线节点列表-String类型
	 * @return
	 */
	public synchronized String getRandomClient(int nodeCount, UserInfoBean requestNode){
		StringBuffer sBuffer = new StringBuffer();
		if (nodeCount > 0) {	//需要查询的节点数大于0
			ArrayList<Integer> choiceNodeList = new ArrayList<Integer>();
			String localUniqueAddress = requestNode.getUniqueAddress();	//localIP:natIP
			Map<Integer, String> tempClients = new TreeMap<Integer, String>();	//remove self & restructure
			Set<String> keySet = cMap.keySet();
			Iterator<String> keys = keySet.iterator();
//			int userCount = getClientCount();
			int index = 0;
			UserInfoBean userInfoBean;
			while (keys.hasNext()) {
				String key = keys.next();
				userInfoBean = cMap.get(key);
				String onlineClient = userInfoBean.getUniqueAddress();
				if (!onlineClient.equals(localUniqueAddress)) {
					tempClients.put(index, onlineClient);
					index ++;
				}
			}
			choiceNodeList = UtilTool.generateRandomNumber(nodeCount, index);
			int size = choiceNodeList.size() - 1;
//			System.err.println("size=" + size);
			if (size >= 0) {
				for (int i = 0; i < size; i++) {
					userInfoBean = cMap.get(tempClients.get(choiceNodeList.get(i)));
					sBuffer.append(userInfoBean.getUserInfo()).append(ConfigMessage.MESSAGE_SPLIT_FLAG);
				}
				userInfoBean = cMap.get(tempClients.get(choiceNodeList.get(size)));
				sBuffer.append(userInfoBean.getUserInfo());
			}
			
//			if (userCount > nodeCount) {//在线的节点数量大于请求连接的节点数量-需要随机选取指定数量的在线节点
//				int index = 0;
//				UserInfoBean userInfoBean;
//				while (keys.hasNext()) {
//					String key = keys.next();
//					userInfoBean = cMap.get(key);
//					String onlineClient = userInfoBean.getUniqueAddress();
//					if (!localUniqueAddress.equals(onlineClient)) {
//						tempClients.put(index, onlineClient);
//						index ++;
//					}
//				}
//				choiceNodeList = UtilTool.generateRandomNumber(nodeCount, index);
//				int size = choiceNodeList.size() - 1;
//				for (int i = 0; i < size; i++) {
//					userInfoBean = cMap.get(tempClients.get(choiceNodeList.get(i)));
//					sBuffer.append(userInfoBean.getUserInfo()).append(ConfigMessage.MESSAGE_SPLIT_FLAG);
//				}
//				userInfoBean = cMap.get(tempClients.get(choiceNodeList.get(size)));
//				sBuffer.append(userInfoBean.getUserInfo());
//			}
			//返回服务器中已存在的全部节点信息给请求节点
//			else {
//				
//				while (keys.hasNext()) {
//					String key = keys.next();
//					UserInfoBean userInfoBean = cMap.get(key);
//					sBuffer.append(userInfoBean.getUserInfo()).append(ConfigMessage.MESSAGE_SPLIT_FLAG);
//				}
//			}
		}
		return sBuffer.toString();
	}
	/**
	 * 得到全部数量的当前在线节点列表-String类型
	 * @return
	 */
	public synchronized String getAllClient(){
		StringBuffer sBuffer = new StringBuffer();
		if (getClientCount(true) > 0) {
			ArrayList<UserInfoBean> clientList = getAllClientList();
			int size = clientList.size();
			if (size == 1) {
				sBuffer.append(clientList.get(size - 1).getUserInfo());
			}
			else if (size > 1) {
				for (int i = 0; i < size - 1; i++) {
					sBuffer.append(clientList.get(i).getUserInfo()).append(ConfigMessage.MESSAGE_SPLIT_FLAG);
				}
				sBuffer.append(clientList.get(size - 1).getUserInfo());
			}
		}
		//返回服务器中已存在的全部节点信息给请求节点
		return sBuffer.toString();
	}
	
//	/**
//	 * 获取节点列表-String类型
//	 * @return
//	 */
//	public synchronized String getAllClient(){
//		StringBuffer sBuffer = new StringBuffer();
//		if (getClientCount(false) > 0) {
//			ArrayList<UserInfoBean> clientList = getAllClientList();
//			int size = clientList.size();
//			if (size == 1) {
//				sBuffer.append(clientList.get(size - 1).getUserInfo());
//			}
//			else if (size > 1) {
//				for (int i = 0; i < size - 1; i++) {
//					sBuffer.append(clientList.get(i).getUserInfo()).append(ConfigMessage.MESSAGE_SPLIT_FLAG);
//				}
//				sBuffer.append(clientList.get(size - 1).getUserInfo());
//			}
//		}
//		//返回服务器中已存在的全部节点信息给请求节点
//		return sBuffer.toString();
//	}
	
	/**
	 * 获取需要建立连接的节点数量
	 * @param connFlag	[1:default min, 2:default max, 3:self define, 4:default all]
	 * @param selfDefine	[fill with valid num when connFlag = 3]
	 * @return
	 */
	public synchronized int getConnNum(int connFlag, int selfDefine){
		int connNum = 0;
		int connSuccessNum = getClientCount(true);
		switch (connFlag) {
		case 1:	//default min
			if (getClientCount(false) == 0) {
				connNum = ConfigMessage.DEFAULT_MIN_CONNECT_NODE;
			}
			else if (connSuccessNum < ConfigMessage.DEFAULT_MIN_CONNECT_NODE) {
				connNum = ConfigMessage.DEFAULT_MIN_CONNECT_NODE - connSuccessNum;
			}
			break;
		case 2:	//default max
			if (getClientCount(false) == 0) {
				connNum = ConfigMessage.DEFAULT_MAX_CONNECT_NODE;
			}
			else if (connSuccessNum < ConfigMessage.DEFAULT_MAX_CONNECT_NODE) {
				connNum = ConfigMessage.DEFAULT_MAX_CONNECT_NODE - connSuccessNum;
			}
			break;
		case 3:	//self define
			if (getClientCount(false) == 0 && selfDefine > 0) {
				connNum = selfDefine;
			}
			else if (connSuccessNum < selfDefine) {
				connNum = selfDefine - connSuccessNum;
			}
			break;
		case 4:	//default all
			connNum = 9999999;
			break;
		default:
			break;
		}
		return connNum;
	}
	/**
	 * 得到当前在线节点列表-ArrayList类型
	 * @return
	 */
	public synchronized ArrayList<UserInfoBean> getAllClientList(){
		ArrayList<UserInfoBean> clientList = new ArrayList<UserInfoBean>();
		Set<String> keySet = cMap.keySet();
		Iterator<String> keys = keySet.iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			UserInfoBean userInfoBean = cMap.get(key);
			clientList.add(userInfoBean);
		}
		return clientList;
	}
	/**
	 * 
	 * @param key
	 * @return
	 */
	public synchronized String getNatAddress(String key){
		String natAddress = null;
		if (key != null && cMap.containsKey(key)) {
			UserInfoBean userInfoBean = getUserInfoBean(key);
			natAddress = userInfoBean.getNATAddress();
		}
		return natAddress;
	}
	
//	/**
//	 * 得到当前在线节点
//	 * @return
//	 */
//	public synchronized ConcurrentHashMap<String, UserInfoBean> getAllClientMap(){
//		return cMap;
//	}
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {
//		ClientData clientData = new ClientData();
		ClientData clientData = ClientData.getInstance();
//		System.err.println(clientData.getClientCount(true));
		Long timeStamp = UtilTool.getUTCTimeStamp();
		UserInfoBean  ipb = null;
		String[] ip = {"172.19.104.112"};	//"172.19.104.111", "172.19.104.112", "172.19.104.113", "172.19.104.114", "172.19.104.115", "172.19.104.111"
		for (int i = 0; i < ip.length; i++) {
			ipb = new UserInfoBean();
			InetAddress natAddress = null, localAddress = null;
			try {
				natAddress = InetAddress.getByName("101.231.205.185");
				localAddress = InetAddress.getByName(ip[i]);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			ipb.setNatIP(natAddress);
			ipb.setNatPort(53803+i);
			ipb.setHostIP(localAddress);
			ipb.setHostPort(8103);
			ipb.setUpdateTimeStamp(timeStamp);
			ipb.setConnFlag(true);
			String key = ipb.getUniqueAddress();
			clientData.addClient(key, ipb);
		}
		
		System.out.println(clientData.getAllClient());
		InetAddress localAddress = null, natAddress = null;
		try {
			localAddress = InetAddress.getByName("172.19.104.111");
			natAddress = InetAddress.getByName("101.231.205.185");
			System.out.println(localAddress + ":" + natAddress);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		UserInfoBean localClient = new UserInfoBean(localAddress, 8103, natAddress, 53803, timeStamp, true);
//		removeInvalidClient("172.19.104.111:101.231.205.185");
//		showAllClient();
		String clients = clientData.getRandomClient(2, localClient);
		System.out.println("clients=" + clients.length() + " ClientsList:" + clients);
		if (clients != null && clients.length() > 0) {
			System.out.println("not null!");
			String[] clientArray = clients.split(ConfigMessage.MESSAGE_SPLIT_FLAG);
			for (int i = 0; i < clientArray.length; i++) {
				System.out.println(clientArray[i]);
			}
		}

		
		
//		ArrayList<UserInfoBean> userInfoBeans = getAllClientList();
//		for (UserInfoBean userInfo : userInfoBeans) {
//			System.err.println(userInfo.getHostAddress());
//		}
//		String clientList = getAllClient();
//		System.err.println(clientList);
//		String key = "172.19.104.112:101.231.205.185";
//		System.out.println(clientData.getNatAddress(key));
//		System.out.println(clientData.getUserInfoBean(key).isConnFlag());
	}
	
}
