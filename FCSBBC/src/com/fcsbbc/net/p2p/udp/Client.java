package com.fcsbbc.net.p2p.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.UtilTool;

public class Client {

	private static Logger logger = LoggerFactory.getLogger(Client.class);
	public static ExecutorService pool = Executors.newCachedThreadPool();
//	private static ClientData clientData = ClientData.getInstance();
	private final static int localPort = ConfigMessage.SYSTEM_PORT;
	private final static String localIP = UtilTool.getLocalAddress();
	private final static int serverPort = ConfigMessage.SYSTEM_PORT;
	private final static String serverIP = ConfigMessage.SERVER_IP;
	private final static String softwareInfo = ConfigMessage.CLIENT_VERISION + ConfigMessage.DATA_SPLIT_FLAG + ConfigMessage.CLIENT_SUBVER;	//1.20170603:baron_0.1
	private final static Byte[] locks = new Byte[0];
	private static DatagramSocket socket;	//send(receive) info
	
	public static void main(String[] args) {
		startClient();
	}
	
	static{
		logger.info("start network bootstrap!");
//		int localPort = ConfigMessage.SYSTEM_PORT;
//		String localIP = UtilTool.getLocalAddress();
//		String serverIP = ConfigMessage.SERVER_IP;
//		int serverPort = ConfigMessage.SERVER_PORT;
		if (localIP != null && localPort > 0 && serverIP != null && serverPort > 0) {
			InetAddress localAddress = UtilTool.getInetAddress(localIP);
			InetAddress serverAddress = UtilTool.getInetAddress(serverIP);
			try {
				socket = new DatagramSocket(localPort, localAddress);
//				socket.setSoTimeout(10 * 1000);
				//initial network
				String filledInfo = ConfigMessage.CLIENT_CONN_SERVER_FLAG + ConfigMessage.MESSAGE_SPLIT_FLAG + 
						localIP + ConfigMessage.DATA_SPLIT_FLAG + localPort + ConfigMessage.MESSAGE_SPLIT_FLAG + 
						serverIP + ConfigMessage.DATA_SPLIT_FLAG + serverPort + ConfigMessage.MESSAGE_SPLIT_FLAG +
						"CLIENT" + ConfigMessage.MESSAGE_SPLIT_FLAG + 
						softwareInfo;
				logger.info("node start successfully!");
				logger.info("send initial command: " + filledInfo);
				String encode64 = UtilTool.getEncode64Info(filledInfo);
				byte[] buf = encode64.getBytes(ConfigMessage.ENCODE_FORMAT_UTF8);
	            //save data into DatagramPacket 
	            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, serverAddress, serverPort);  
	            //send info through socket  
	            socket.send(sendPacket);
			} catch (Exception e) {
				logger.error("node start failed!");
				if (socket != null) {
					socket.close();
				}
			}
		}
		else {
			logger.error("network connection is unavailable!");
		}
	}
	
	
	public static void startClient(){
		
//		while (clientData.getClientCount(true) == 0 || !clientData.judgeConnResult()) {
		while (true) {
			synchronized (locks) {
				try {
					//receive info
					byte[] receiveBuf = new byte[1024];
					DatagramPacket packet = new DatagramPacket(receiveBuf, receiveBuf.length);			//save received info into buf
					socket.receive(packet);																//receive info through socket
					String receivedEncodedInfo = new String(receiveBuf, 0, packet.getLength());
					int command = ClientCommand.analyzeInfo(receivedEncodedInfo, packet);				//analyze real-info  
					String localIPInfo = localIP + ConfigMessage.DATA_SPLIT_FLAG + localPort;
					//send info
					if (command >= 0) {
//						ArrayList<String> filledInfoList = ClientCommand.fillInfo(command, receivedEncodedInfo, packet);	//generate feedback info
						ArrayList<String> filledInfoList = ClientCommand.fillInfo(command, receivedEncodedInfo, packet, localIPInfo);	//generate feedback info
						if (command != 6) {
							ArrayList<String> destinationInfo =new ArrayList<>();
							for (int i = 0; i < filledInfoList.size(); i++) {
								String encodeInfo = UtilTool.getEncode64Info(filledInfoList.get(i));		//encode info with Encode64
								
								if (filledInfoList.get(i).contains(ConfigMessage.CLIENT_CONN_NAT) || 
										filledInfoList.get(i).contains(ConfigMessage.CLIENT_CONN_NAT_OK) || 
										filledInfoList.get(i).contains(ConfigMessage.CONNECT_REQUEST_OK) || 
										filledInfoList.get(i).contains(ConfigMessage.CLIENT_REJECT_CLIENT_CONN)) {
									logger.info("command(" + command + ")send to client:" + filledInfoList.get(i));
								}
								else {
									logger.info("command(" + command + ")send to server:" + filledInfoList.get(i));
								}
								
								destinationInfo = UtilTool.getDestinationIP(encodeInfo, 2);
								byte[] sendBuf = encodeInfo.getBytes(ConfigMessage.ENCODE_FORMAT_UTF8);
								if (destinationInfo.size() >= 2) {
									InetAddress destinationAddress = UtilTool.getInetAddress(destinationInfo.get(0));
									int destinationPort = Integer.parseInt(destinationInfo.get(1));
									packet= new DatagramPacket(sendBuf, sendBuf.length, destinationAddress, destinationPort);
									socket.send(packet);	//send info through socket
								}
							}
						}
					}
				} catch (IOException e) {
					logger.error("Receive Data Failed-IO Exception!");
				}																
			}
		}
	}
	
//	public static void sendInfo(String info, DatagramSocket socket, InetAddress destinationAddress, int destinationPort){
//		if (info !=null && destinationAddress != null && socket != null) {
//			try {
//				logger.info("send info:" + info);
//				byte[] buf = UtilTool.getEncode64Bytes(info);
//				DatagramPacket packet = new DatagramPacket(buf, buf.length, destinationAddress, destinationPort);
//				socket.send(packet);
//			} catch (IOException e) {
////				e.printStackTrace();
//				logger.error("send info failed!");
//			}
//		}
//		else {
//			logger.error("send info failed,please check parameters!");
//		}
//	}
	
//	public static void receiveInfo(DatagramSocket socket){
//		if (socket != null) {
//			pool.execute(new Runnable() {
//				
//				@Override
//				public void run() {
//					String receivedInfo = null;
//					try {
//						boolean netConn = true;
//						while (netConn) {
//							byte[] buf = new byte[1024];
//							DatagramPacket packet = new DatagramPacket(buf, buf.length);
//							socket.receive(packet);
//							String encodeInfo = new String(buf, 0, packet.getLength());
//							receivedInfo = UtilTool.getDecode64Info(encodeInfo);
//				            InetAddress destinationAddress = packet.getAddress();
//				            int destinationPort = packet.getPort();
//				            String destinationIP = UtilTool.getIP(destinationAddress);
//				            int command = ClientCommand.analyzeInfo(receivedInfo);
////				            String info = ClientCommand.fillInfo(destinationIP, destinationPort, command);
////				            sendInfo(info, socket, destinationAddress, destinationPort);
//						}
//						
//					} catch (IOException e) {
////						e.printStackTrace();
//						logger.error("cannot receive info!");
//					}
//				}
//			});
//			
//		}
//		else {
//			logger.error("socket exception!");
//		}
//	}

}
