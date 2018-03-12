package com.fcsbbc.net.p2p.udp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.UtilTool;

public class ClientCommand {
	
	private static Logger logger = LoggerFactory.getLogger(ClientCommand.class);
	private static ClientData clientData = ClientData.getInstance();
	private static ConcurrentHashMap<String, UserInfoBean> cMap;
//	public static void main(String[] args) {
////		String IP = UtilTool.getLocalAddress();
////		int Port = ConfigMessage.SYSTEM_PORT;
////		String receivedInfo = "Client_Conn_Server_OK,172.19.104.111:8102,111.111.111.111:8102,SERVER,1.20170616:baron_0.1";
////		String receivedInfo = "Get_Random_Nodes_OK,172.19.104.111:8102,111.111.111.111:8102,"
////				+ "LocalIP:172.1.1.2:8106#NATIP:101.231.205.186:8102#ConnTime:1499904113127,"
////				+ "LocalIP:101.231.205.186:8106#NATIP:101.231.205.186:8106#ConnTime:1499904113127,"
////				+ "LocalIP:172.1.1.5:8106#NATIP:101.231.205.186:8105#ConnTime:1499904113127,"
////				+ "LocalIP:172.1.1.4:8106#NATIP:101.231.205.186:8104#ConnTime:1499904113127,"
////				+ "LocalIP:172.1.1.1:8106#NATIP:101.231.205.186:8101#ConnTime:1499904113127,"
////				+ "SERVER,1.20170616:baron_0.1";
////		String receivedInfo = "Client_Conn_Client_OK,172.19.104.111:8102,172.19.19.19:8102,101.231.205.186:8103,SERVER,1.20170616:baron_0.1";
////		String receivedInfo = "Client_Conn_Client_OK,172.19.104.111:8102,111.111.111.111:8102,101.231.205.186:8104,SERVER,1.20170616:baron_0.1";
////		String receivedInfo = "Connect_Request_OK,172.19.104.111:8102,172.19.104.112:8102,CLIENT,1.20170603:baron_0.1";
////		String receivedInfo = "Client_Request_Client,172.19.104.111:8102,101.231.205.186:8104,111.111.111.111:8102,SERVER,1.20170616:baron_0.1";
////		String receivedInfo = "Client_Conn_NAT,101.231.205.186:8104,111.111.111.111:8102,CLIENT,1.20170603:baron_0.1";
//		String receivedInfo = "Client_Conn_NATs,172.19.104.112:8102,172.19.104.111:8102,SERVER,1.20170603:baron_0.1";
////		String receivedInfo = "Client_Reject_Client_Conn,172.19.104.112:8102,172.19.104.111:8102,SERVER,1.20170603:baron_0.1";
//		
//		String encode64 = UtilTool.getEncode64Info(receivedInfo);
//		String decode64 = UtilTool.getDecode64Info(encode64);
//		System.err.println(decode64);
//		byte[] buf = new byte[1024];
//		DatagramPacket packet = new DatagramPacket(buf, buf.length);
//		int Command = analyzeInfo(encode64, packet);
//		System.out.println("Command : " + Command);
//		ArrayList<String> filledInfo = fillInfo(Command, encode64, packet);
//		for (int i = 0; i < filledInfo.size(); i++) {
//			System.err.println(filledInfo.get(i));
//		}
//	}
	
	/**
	 * generate feedback info[info-body,self-ipInfo[local(nat)],des1-ipInfo,(des2-ipInfo,)data,server-client-flag,software-version]
	 * @param Command
	 * @param receivedEncodedInfo
	 * @param packet
	 * @return	generated info
	 */
	public static ArrayList<String> fillInfo(int Command, String receivedEncodedInfo, DatagramPacket packet, String localIPInfo){
		ArrayList<String> filledInfoList = new ArrayList<>();
		StringBuffer sBuffer = new StringBuffer();
		if (receivedEncodedInfo !=null && packet != null && localIPInfo != null) {
			String softwareInfo = ConfigMessage.CLIENT_VERISION + ConfigMessage.DATA_SPLIT_FLAG + ConfigMessage.CLIENT_SUBVER;	//软件版本信息:1.20170603:baron_0.1
			String decode64Info = UtilTool.getDecode64Info(receivedEncodedInfo);	//decode info with Encode64
			String[] decodeInfo = decode64Info.split(ConfigMessage.MESSAGE_SPLIT_FLAG);
			if (decodeInfo.length >= 4) {
				String desIPInfo, connNATIPInfo = null;
				String localNatIPInfo = null;
				String desIP = UtilTool.getIP(packet.getAddress());	//destination NAT-IP
				int desPort = packet.getPort();						//destination NAT-Port
				switch (Command) {	//analyze info
//				case 0:	//build connection with server[Client_Conn_Server,self_local,server_nat,CLIENT,1.20170603:baron_0.1]
//					
//					break;
				case 1:	//request random number of nodes from server[Get_Random_Nodes,self_local,server_nat,number,1.20170603:baron_0.1]
					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//get server NATAddress from packet
//					desIPInfo = decodeInfo[1];										//get server NATAddress from info
//					localNatIPInfo = decodeInfo[2];									//get self NATAddress from info
					int connNum = clientData.getConnNum(1, 0);
					sBuffer.append(ConfigMessage.GET_RANDOM_NODES).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(connNum).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(softwareInfo);
					filledInfoList.add(sBuffer.toString());
					break;
				case 2:	//ask server for help to build connecting with node
					ArrayList<UserInfoBean> clientList = clientData.getAllClientList();
					localNatIPInfo = decodeInfo[2];	//get self NATAddress from info
					for (int i = 0; i < clientList.size(); i++) {
						sBuffer.setLength(0);
						connNATIPInfo = clientList.get(i).getNATAddress();				//get connecting node NATAddress from info
						String connLocalIPInfo = clientList.get(i).getHostAddress();	//get connecting node LocalAddress from info
						//judge public_network_ip
						if (connNATIPInfo != null && connLocalIPInfo != null && localNatIPInfo != null) {	//[Client_Conn_NAT,self_local,connecting_nat,CLIENT,1.20170603:baron_0.1]
							String localNATIP = localNatIPInfo.split(ConfigMessage.DATA_SPLIT_FLAG)[0];
							int localNATPort = Integer.parseInt(localNatIPInfo.split(ConfigMessage.DATA_SPLIT_FLAG)[1]);
							String connNATIP = connNATIPInfo.split(ConfigMessage.DATA_SPLIT_FLAG)[0];
							int connNATPort = Integer.parseInt(connNATIPInfo.split(ConfigMessage.DATA_SPLIT_FLAG)[1]);
							if (localNATIP.equals(connNATIP) && localNATPort != connNATPort) {	//same nat
								sBuffer.append(ConfigMessage.CLIENT_CONN_NAT).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
								.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(connNATIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
								.append(connLocalIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
								.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
								.append(softwareInfo);
								filledInfoList.add(sBuffer.toString());
							}
							else {
								if (connNATIPInfo.equals(connLocalIPInfo)) {	//public_network_ip-->send connection request to node
									sBuffer.append(ConfigMessage.CLIENT_CONN_NAT).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//									.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append(connNATIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append(softwareInfo);
									filledInfoList.add(sBuffer.toString());
								}
								else {	//[Client_Conn_Client,self_local,server_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
									desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//get server NATAddress from packet
//									desIPInfo = decodeInfo[1];										//get server NATAddress from info
									sBuffer.append(ConfigMessage.CLIENT_CONN_CLIENT_FLAG).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//									.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append(connNATIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
									.append(softwareInfo);
									filledInfoList.add(sBuffer.toString());
								}
							}
						}
					}
					break;
				case 3:	//build connection with node[Client_Conn_NAT,self_local,connecting_nat,CLIENT,1.20170603:baron_0.1]
//					localNatIPInfo = decodeInfo[2];	//get self NATAddress from info
					desIPInfo = decodeInfo[3];		//get destination NATAddress from info
					sBuffer.append(ConfigMessage.CLIENT_CONN_NAT).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(softwareInfo);
					filledInfoList.add(sBuffer.toString());
					break;
				case 4:	//reply node request[Client_Conn_NAT_OK,self_local,connecting_nat,CLIENT,1.20170603:baron_0.1]
					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//get destination NATAddress from packet
//					desIPInfo = decodeInfo[1];										//get destination NATAddress from info
//					localNatIPInfo = decodeInfo[2];									//get self NATAddress from info
					sBuffer.append(ConfigMessage.CLIENT_CONN_NAT_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(softwareInfo);
					filledInfoList.add(sBuffer.toString());
					break;
				case 5:	//confirm connection[Connect_Request_OK,self_local,connecting_nat,CLIENT,1.20170603:baron_0.1]
					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//get destination NATAddress from packet
//					desIPInfo = decodeInfo[1];										//get destination NATAddress from info
//					localNatIPInfo = decodeInfo[2];									//get self NATAddress from info
					sBuffer.append(ConfigMessage.CONNECT_REQUEST_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(softwareInfo);
					filledInfoList.add(sBuffer.toString());
					break;
				case 6:	//without reply
					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//get destination NATAddress from packet
//					desIPInfo = decodeInfo[1];										//get destination NATAddress from info
//					sBuffer.append(ConfigMessage.CONNECT_REQUEST_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append("NONE").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(softwareInfo);
//					filledInfoList.add(sBuffer.toString());
					logger.info("build connection with(" + desIPInfo + ")Successfully!");
					break;
				case 7:	//reply ok to server[Client_Request_Client_OK,self_local,server_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
						//build connection to node[Client_Conn_NAT,self_local,connecting_nat,CLIENT,1.20170603:baron_0.1]
//					localNatIPInfo = decodeInfo[2];									//get self NATAddress from info
//					desIPInfo = decodeInfo[1];										//get server localAddress from info
					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//get server NATAddress from packet
					connNATIPInfo = decodeInfo[3];									//get connecting NATAddress from info
					//[Client_Request_Client_OK,self_local,server_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
					sBuffer.append(ConfigMessage.CLIENT_REQUEST_CLIENT_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(connNATIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(softwareInfo);
					filledInfoList.add(sBuffer.toString());
					//[Client_Conn_NAT,self_local,connecting_nat,CLIENT,1.20170603:baron_0.1]
					sBuffer.setLength(0);
					sBuffer.append(ConfigMessage.CLIENT_CONN_NAT).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(connNATIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(softwareInfo);
					filledInfoList.add(sBuffer.toString());
					
					break;
				case 8:	//build server connection directly[Generic_Conn_Server_Client_OK,self_local,server_nat,CLIENT,1.20170603:baron_0.1]
					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//get server NATAddress from packet
//					desIPInfo = decodeInfo[1];										//get server localAddress from info
//					localNatIPInfo = decodeInfo[2];									//get self NATAddress from info
					sBuffer.append(ConfigMessage.GENERIC_CONN_SERVER_CLIENT_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
					.append(softwareInfo);
					filledInfoList.add(sBuffer.toString());
					
					break;
				case 0:	//reject server/client any requests[Client_Reject_Server_Conn/Server_Reject_Server_Conn,本机IP:port,对方IP:port,CLIENT,1.20170603:baron_0.1]
					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//get destination NATAddress from packet
//					desIPInfo = decodeInfo[1];										//get destination NATAddress from info
//					localNatIPInfo = decodeInfo[2];									//get self NATAddress from info
					if (decode64Info.contains("SERVER")) {	//[Client_Reject_Server_Conn,self_local,destination_nat,CLIENT,1.20170603:baron_0.1]
						sBuffer.append(ConfigMessage.CLIENT_REJECT_SERVER_CONN).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
						.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
						.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
						.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
						.append(softwareInfo);
						filledInfoList.add(sBuffer.toString());
					}
					else {	//[Client_Reject_Client_Conn,self_local,destination_nat,CLIENT,1.20170603:baron_0.1]
						sBuffer.append(ConfigMessage.CLIENT_REJECT_CLIENT_CONN).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
						.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
						.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
						.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
						.append(softwareInfo);
						filledInfoList.add(sBuffer.toString());
					}
					break;
				default:
					break;
				}
			}
		}
		return filledInfoList;
	}
	/**
	 * analyze info
	 * @param receivedEncodedInfo
	 * @param packet
	 * @return	command(Integer)
	 */
	public static int analyzeInfo(String receivedEncodedInfo, DatagramPacket packet){
		int command = 0;
		if (receivedEncodedInfo != null && packet != null) {
			String decode64Info = UtilTool.getDecode64Info(receivedEncodedInfo);
			
			if (decode64Info.contains("SERVER")) {
				logger.info("receive from server:" + decode64Info);
			}
			else {
				logger.info("receive from client:" + decode64Info);
			}
			
			String[] decodeInfo = decode64Info.split(ConfigMessage.MESSAGE_SPLIT_FLAG);
			int len = decodeInfo.length;
			if (len >= 3) {
				String infoFlag = decodeInfo[0];
				if (ConfigMessage.CLIENT_CONN_SERVER_FLAG_OK.equals(infoFlag)) {
					
					command = 1;
				}
				else if (ConfigMessage.GET_RANDOM_NODES_OK.equals(infoFlag)) {	//receive random nodes info from server
					//parser nodes info, save into Map
					cMap = UtilTool.extractUserInfoBean(receivedEncodedInfo);
//					logger.info("Map Size:" + cMap.size());
					
					//loop for adding
					ArrayList<UserInfoBean> clientList = UtilTool.getAllClientList(cMap);
					int size = clientList.size();
					if (size > 0) {
						for (int i = 0; i < size; i++) {
							UserInfoBean userInfoBean = clientList.get(i);
							String key = userInfoBean.getUniqueAddress();
							clientData.addClient(key, userInfoBean);
						}
					}
					
//					if (clientData.getClientCount(false) > 0) {
//						//loop for adding
//						ArrayList<UserInfoBean> clientList = UtilTool.getAllClientList(cMap);
//						int size = clientList.size();
//						if (size > 0) {
//							for (int i = 0; i < size; i++) {
//								UserInfoBean userInfoBean = clientList.get(i);
//								String key = userInfoBean.getUniqueAddress();
//								clientData.addClient(key, userInfoBean);
//							}
//						}
//					}
//					else {
//						clientData.setcMap(cMap);
//						logger.info(clientData.getAllClient());
//					}
					
					command = 2;
				}
				else if (ConfigMessage.CLIENT_CONN_CLIENT_FLAG_OK.equals(infoFlag)) {	//receive node's confirmation from server
					
					command = 3;
				}
				else if (ConfigMessage.CLIENT_CONN_NAT.equals(infoFlag)) {	//receive node's connection request
					
					command = 4;
				}
				else if (ConfigMessage.CLIENT_CONN_NAT_OK.equals(infoFlag)) {
					String natIP = UtilTool.getIP(packet.getAddress());
					String localIP = decodeInfo[1].split(ConfigMessage.DATA_SPLIT_FLAG)[0];
					String key = localIP + ConfigMessage.DATA_SPLIT_FLAG + natIP;
//					logger.info("key:" + key);
					if (clientData.containsKey(key)) {
						UserInfoBean userInfoBean = clientData.getUserInfoBean(key);
						userInfoBean.setConnFlag(true);	//set connection flag = true
						logger.info("Connect with(" + userInfoBean.getUserInfo() + ")Successfully!");
					}
					else {
						int natPort = packet.getPort();
						int localPort = Integer.parseInt(decodeInfo[1].split(ConfigMessage.DATA_SPLIT_FLAG)[1]);
						Long updateTimeStamp = UtilTool.getUTCTimeStamp();
						UserInfoBean userInfoBean = new UserInfoBean(UtilTool.getInetAddress(localIP), localPort, UtilTool.getInetAddress(natIP), natPort, updateTimeStamp, true);
						clientData.addClient(key, userInfoBean);
						logger.info("clientInfo:" + userInfoBean.getUserInfo());
					}
					
					command = 5;
				}
				else if (ConfigMessage.CONNECT_REQUEST_OK.equals(infoFlag)) {
					String natIP = UtilTool.getIP(packet.getAddress());
					String localIP = decodeInfo[1].split(ConfigMessage.DATA_SPLIT_FLAG)[0];
					String key = localIP + ConfigMessage.DATA_SPLIT_FLAG + natIP;
//					logger.info("key:" + key);
					if (clientData.containsKey(key)) {
						UserInfoBean userInfoBean = clientData.getUserInfoBean(key);
						userInfoBean.setConnFlag(true);	//set connection flag = true
						logger.info("Connect with(" + userInfoBean.getUserInfo() + ")Successfully!");
					}
					else {
						int natPort = packet.getPort();
						int localPort = Integer.parseInt(decodeInfo[1].split(ConfigMessage.DATA_SPLIT_FLAG)[1]);
						Long updateTimeStamp = UtilTool.getUTCTimeStamp();
						UserInfoBean userInfoBean = new UserInfoBean(UtilTool.getInetAddress(localIP), localPort, UtilTool.getInetAddress(natIP), natPort, updateTimeStamp, true);
						clientData.addClient(key, userInfoBean);
						logger.info("clientInfo:" + userInfoBean.getUserInfo());
					}
					
					command = 6;
				}
				else if (ConfigMessage.CLIENT_REQUEST_CLIENT.equals(infoFlag)) {
					
					command = 7;
				}
				else if (ConfigMessage.GENERIC_CONN_SERVER_CLIENT.equals(infoFlag)) {
					//save server info
		            InetAddress serverNATIP = packet.getAddress();	//get server NATIP from packet
		            int serverNATPort = packet.getPort();			//get server NATPort from packet
		            String[] ipDetails = decodeInfo[1].split(ConfigMessage.DATA_SPLIT_FLAG);	//get server localAddress from info
		            InetAddress serverLocalIP = UtilTool.getInetAddress(ipDetails[0]);
		            int serverLocalPort = Integer.parseInt(ipDetails[1]);
//					String serverNATAddress = UtilTool.getIP(serverNATIP) + ConfigMessage.DATA_SPLIT_FLAG + serverNATPort;
		            String key = ipDetails[0] + ConfigMessage.DATA_SPLIT_FLAG + UtilTool.getIP(serverNATIP);
					Long updateTimeStamp = UtilTool.getUTCTimeStamp();
		            UserInfoBean userInfoBean = new UserInfoBean(serverLocalIP, serverLocalPort, serverNATIP, serverNATPort, updateTimeStamp, true);
		            clientData.addClient(key, userInfoBean);
		            logger.info("serverInfo:" + userInfoBean.getUserInfo());
		            
					command = 8;
				}
				else if (ConfigMessage.SERVER_REJECT_CLIENT_CONN.equals(infoFlag) || ConfigMessage.CLIENT_REJECT_CLIENT_CONN.equals(infoFlag)) {	//receive rejection from server/client
//					String localIP = decodeInfo[1].split(ConfigMessage.DATA_SPLIT_FLAG)[0];
//					String natIP = UtilTool.getIP(packet.getAddress());
//					if (localIP != null && natIP != null) {
//						String key = localIP + ConfigMessage.DATA_SPLIT_FLAG + natIP;
//						clientData.removeInvalidClient(key);
//						command = -1;
//					}
					logger.error("Server or Client reject connection request!");
					command = -1;
				}
			}
		}
		else {
			logger.error("Received Info is NULL");
		}
		return command;
	}
	
//	/**
//	 * 构造发送的消息内容[消息头,己方IP信息[local或nat],目标1IP信息,(目标2IP信息,)指令,软件版本信息]
//	 * @param Command
//	 * @param receivedEncodedInfo
//	 * @param packet
//	 * @return	构造的消息内容
//	 */
//	public static ArrayList<String> fillInfo(int Command, String receivedEncodedInfo, DatagramPacket packet){
//		ArrayList<String> filledInfoList = new ArrayList<>();
//		StringBuffer sBuffer = new StringBuffer();
////		String localIP = UtilTool.getLocalAddress();
//		String softwareInfo = ConfigMessage.CLIENT_VERISION + ConfigMessage.DATA_SPLIT_FLAG + ConfigMessage.CLIENT_SUBVER;	//1.20170603:baron_0.1
////		if (receivedEncodedInfo !=null && localIP != null && packet != null) {
//		if (receivedEncodedInfo !=null && packet != null) {
//			String decode64Info = UtilTool.getDecode64Info(receivedEncodedInfo);
//			String[] decodeInfo = decode64Info.split(ConfigMessage.MESSAGE_SPLIT_FLAG);
//			if (decodeInfo.length >= 4) {
////				String localIPInfo = localIP + ConfigMessage.DATA_SPLIT_FLAG + ConfigMessage.SYSTEM_PORT;
//				String desIPInfo = null;
//				String localNatIPInfo = null;
////				logger.info("Server InetAddress: " + packet.getAddress());
//				String desIP = UtilTool.getIP(packet.getAddress());
//				int desPort = packet.getPort();
//				switch (Command) {
////				case 0:	//请求与服务器建立连接[Client_Conn_Server,self_local,server_nat,CLIENT,1.20170603:baron_0.1]
////					
////					break;
//				case 1:	//请求从服务器处获取随机数量的节点[Get_Random_Nodes,self_nat,server_nat,number,1.20170603:baron_0.1]
//					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//从packet中解析出服务器地址
////					desIPInfo = decodeInfo[1];		//从消息中解析出服务器地址
//					localNatIPInfo = decodeInfo[2];	//从消息中解析出本机NAT地址
//					int connNum = clientData.getConnNum(1, 0);
//					sBuffer.append(ConfigMessage.GET_RANDOM_NODES).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(connNum).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(softwareInfo);
//					filledInfoList.add(sBuffer.toString());
//					break;
//				case 2:	//告知服务器与某在线节点()建立连接请求
//					ArrayList<UserInfoBean> clientList = clientData.getAllClientList();
//					localNatIPInfo = decodeInfo[2];	//从消息中解析出本机NAT地址
//					for (int i = 0; i < clientList.size(); i++) {
//						sBuffer.setLength(0);
//						desIPInfo = clientList.get(i).getNATAddress();	//从消息中解析出待连接客户端的NAT地址
//						String clientLocalIPInfo = clientList.get(i).getHostAddress();	//从消息中解析出待连接客户端的Local地址
//						//判断是否为公网IP
//						if (desIPInfo != null && clientLocalIPInfo != null) {	//[Client_Conn_NAT,self_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
//							if (desIPInfo.equals(clientLocalIPInfo)) {	//公网IP-->直接发送NAT连接信息
//								sBuffer.append(ConfigMessage.CLIENT_CONN_NAT).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////								.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(softwareInfo);
//								filledInfoList.add(sBuffer.toString());
//							}
//							else {	//[Client_Conn_Client,self_nat,server,connecting_nat,CLIENT,1.20170603:baron_0.1]
//								String serverIPInfo = decodeInfo[1];	//从消息中解析出服务器地址
//								sBuffer.append(ConfigMessage.CLIENT_CONN_CLIENT_FLAG).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////								.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(serverIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//								.append(softwareInfo);
//								filledInfoList.add(sBuffer.toString());
//							}
//						}
//					}
//					break;
//				case 3:	//向客户端发送建立请求连接信息[Client_Conn_NAT,self_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
//					/**不通过服务器中转，直接建立连接请求的情况需要另行处理，以下只处理服务器中转的连接请求*/
//					localNatIPInfo = decodeInfo[2];	//从消息中解析出本机NAT地址
//					desIPInfo = decodeInfo[3];		//从消息中解析出对方NAT地址
//					sBuffer.append(ConfigMessage.CLIENT_CONN_NAT).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(softwareInfo);
//					filledInfoList.add(sBuffer.toString());
//					break;
//				case 4:	//回复连接请求的客户端[Client_Conn_NAT_OK,self_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
////					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//从packet中解析出地址
//					localNatIPInfo = decodeInfo[2];	//从消息中解析出本机NAT地址
//					desIPInfo = decodeInfo[1];		//从消息中解析出对方NAT地址
//					sBuffer.append(ConfigMessage.CLIENT_CONN_NAT_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(softwareInfo);
//					filledInfoList.add(sBuffer.toString());
//					break;
//				case 5:	//握手确认连接成功[Connect_Request_OK,self_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
////					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//从packet中解析出地址
//					localNatIPInfo = decodeInfo[2];	//从消息中解析出本机NAT地址
//					desIPInfo = decodeInfo[1];		//从消息中解析出对方NAT地址
//					sBuffer.append(ConfigMessage.CONNECT_REQUEST_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(softwareInfo);
//					filledInfoList.add(sBuffer.toString());
//					break;
//				case 6:	//握手确认连接成功，无需发送回复信息
//					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//从packet中解析出地址
////					desIPInfo = decodeInfo[1];	//从消息中解析出目的地址
////					sBuffer.append(ConfigMessage.CONNECT_REQUEST_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////					.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////					.append("NONE").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////					.append(softwareInfo);
////					filledInfoList.add(sBuffer.toString());
//					logger.info("Connect with(" + desIPInfo + ")Successfully!");
//					break;
//				case 7:	//向服务器回复同意连接[Client_Request_Client_OK,self_nat,server,connecting_nat,CLIENT,1.20170603:baron_0.1]
//						//向请求连接的客户端，发送NAT请求[Client_Conn_NAT,self_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
//					localNatIPInfo = decodeInfo[2];	//从消息中解析出本机NAT地址
//					desIPInfo = decodeInfo[1];		//从消息中解析出服务器地址
//					String shadowIPInfo = decodeInfo[3];	//从消息中解析出请求连接的客户端NAT地址
//					//[Client_Request_Client_OK,self_nat,server,connecting_nat,CLIENT,1.20170603:baron_0.1]
//					sBuffer.append(ConfigMessage.CLIENT_REQUEST_CLIENT_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(shadowIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(softwareInfo);
//					filledInfoList.add(sBuffer.toString());
//					//[Client_Conn_NAT,self_nat,connecting_nat,CLIENT,1.20170603:baron_0.1]
//					sBuffer.setLength(0);
//					sBuffer.append(ConfigMessage.CLIENT_CONN_NAT).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(shadowIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(softwareInfo);
//					filledInfoList.add(sBuffer.toString());
//					
//					break;
//				case 8:	//同意与服务器直接建立连接[Generic_Conn_Server_Client_OK,self_nat,server,CLIENT,1.20170603:baron_0.1]
////					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//从packet中解析出服务器地址
//					desIPInfo = decodeInfo[1];		//从消息中解析出服务器地址
//					localNatIPInfo = decodeInfo[2];	//从消息中解析出本机NAT地址
//					sBuffer.append(ConfigMessage.GENERIC_CONN_SERVER_CLIENT_OK).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//					.append(softwareInfo);
//					filledInfoList.add(sBuffer.toString());
//					
//					break;
//				case 0:	//向请求连接的服务器或客户端返回拒绝连接信息[Client_Reject_Server_Conn/Server_Reject_Server_Conn,本机IP:port,对方IP:port,CLIENT,1.20170603:baron_0.1]
////					desIPInfo = desIP + ConfigMessage.DATA_SPLIT_FLAG + desPort;	//从packet中解析出地址
//					desIPInfo = decodeInfo[1];		//从消息中解析出目的地址
//					localNatIPInfo = decodeInfo[2];	//从消息中解析出本机NAT地址
//					if (decode64Info.contains("SERVER")) {
//						sBuffer.append(ConfigMessage.CLIENT_REJECT_SERVER_CONN).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////						.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append(softwareInfo);
//						filledInfoList.add(sBuffer.toString());
//					}
//					else {
//						sBuffer.append(ConfigMessage.CLIENT_REJECT_CLIENT_CONN).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
////						.append(localIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append(localNatIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append(desIPInfo).append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append("CLIENT").append(ConfigMessage.MESSAGE_SPLIT_FLAG)
//						.append(softwareInfo);
//						filledInfoList.add(sBuffer.toString());
//					}
//					break;
//				default:
//					break;
//				}
//			}
//			
//		}
//		return filledInfoList;
//	}
	
}
