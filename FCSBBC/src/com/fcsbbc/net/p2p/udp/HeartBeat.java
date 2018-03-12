package com.fcsbbc.net.p2p.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.UtilTool;

public class HeartBeat {
	
	private  ExecutorService pool = Executors.newCachedThreadPool();
	private Logger logger = LoggerFactory.getLogger(HeartBeat.class);
	private   long  NAT_TIMOUT_MS = ConfigMessage.NAT_TIMEOUT_MS;
//	private int destinationPort;
//	private InetAddress destinationIP;
	private DatagramSocket socket;
	private UserInfoBean userInfoBean;
	
//	public HeartBeat(DatagramSocket socket, InetAddress destinationIP, int destinationPort){
//		this.socket = socket;
//		this.destinationIP = destinationIP;
//		this.destinationPort = destinationPort;
//	}
	
	public HeartBeat(DatagramSocket socket, UserInfoBean userInfoBean){
		this.socket = socket;
		this.userInfoBean = userInfoBean;
	}
	/**
	 * 维护心跳的服务器端或客户端发送信息
	 */
	public void send(){
		logger.info(" 心跳维护启动,NAT_Session超时时间{"+NAT_TIMOUT_MS+"毫秒}：  目标地址： " + userInfoBean.getHostIP().getHostAddress() + ":" + userInfoBean.getHostPort());
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						InetAddress destinationIP = userInfoBean.getHostIP();
						int destinationPort = userInfoBean.getHostPort();
						String data = ConfigMessage.NAT_MAINTAIN_HEARTBEAT_FLAG;
						String encode64 = UtilTool.getEncode64Info(data);
						byte[] sendBuf = encode64.getBytes();
						DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, destinationIP, destinationPort);
						socket.send(packet);
						Thread.sleep(NAT_TIMOUT_MS);
					} catch (InterruptedException e) {
//						e.printStackTrace();
						logger.error("Interrupted Exception!");
					} catch (IOException e) {
//						e.printStackTrace();
						logger.error("IO Exception-Send Info Failed!");
					}
				}
			}
		});
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}
	
}
