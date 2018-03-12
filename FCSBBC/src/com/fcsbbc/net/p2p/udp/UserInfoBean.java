package com.fcsbbc.net.p2p.udp;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fcsbbc.utils.ConfigMessage;
import com.fcsbbc.utils.UtilTool;

public class UserInfoBean implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private String hostName;
	private InetAddress hostIP;
	private int hostPort;
//	private String hostMac;
	private InetAddress natIP;
	private int natPort;
//	private String natMac;
//	private Date updateTime = new Date();
	private Long updateTimeStamp;
	private boolean connFlag;
	
	private static Logger logger = LoggerFactory.getLogger(UserInfoBean.class);
	public UserInfoBean(){
		
	}
	public UserInfoBean(InetAddress hostIP, int hostPort, InetAddress natIP, int natPort, Long updateTimeStamp){
		this.hostIP = hostIP;
		this.hostPort = hostPort;
		this.natIP = natIP;
		this.natPort = natPort;
//		this.updateTime = updateTime;
		this.updateTimeStamp = updateTimeStamp;
	}
	public UserInfoBean(InetAddress hostIP, int hostPort, InetAddress natIP, int natPort, Long updateTimeStamp, boolean connFlag){
		this.hostIP = hostIP;
		this.hostPort = hostPort;
		this.natIP = natIP;
		this.natPort = natPort;
		this.updateTimeStamp = updateTimeStamp;
		this.connFlag = connFlag;
	}
	public String getUserInfo(){
		String userInfo =ConfigMessage.NODE_LOCAL_INFO_FLAG + ConfigMessage.DATA_SPLIT_FLAG + 
				UtilTool.getIP(this.hostIP) + ConfigMessage.DATA_SPLIT_FLAG + this.hostPort + 
				ConfigMessage.INFO_SPLIT_FLAG +
	            ConfigMessage.NODE_NAT_INFO_FLAG + ConfigMessage.DATA_SPLIT_FLAG + 
	            UtilTool.getIP(this.natIP) + ConfigMessage.DATA_SPLIT_FLAG + this.natPort + 
	            ConfigMessage.INFO_SPLIT_FLAG +
	            ConfigMessage.NODE_TIME_INFO_FLAG + ConfigMessage.DATA_SPLIT_FLAG + updateTimeStamp + 
	            ConfigMessage.INFO_SPLIT_FLAG + 
	            ConfigMessage.NODE_CONN_INFO_FLAG + ConfigMessage.DATA_SPLIT_FLAG + this.connFlag;
//	            ConfigMessage.NODE_TIME_INFO_FLAG + ConfigMessage.DATA_SPLIT_FLAG + UtilTool.formatDate(this.updateTime);
		
		return userInfo;
	}
	
//	public String toString(){
//		String str = " 主机名：" + this.hostName +
//	            " 内网地址：" + this.hostIP + ":"+ this.hostPort +"/MAC:" + this.hostMac + 
//	            " --->公网地址：" + this.natIP + ":" + this.natPort +"/MAC:" + this.natMac + 
//	            "  更新时间：" + formatDate(this.updateTime);
//		
//		return str.toString();
//	}
	/**
	 * 格式为:NATIP:NATPort，如105.22.114.123:62000
	 * @return
	 */
	public String getNATAddress(){
//		return this.natIP.getHostAddress() + ConfigMessage.DATA_SPLIT_FLAG + this.natPort;
		return UtilTool.getIP(this.natIP) + ConfigMessage.DATA_SPLIT_FLAG + this.natPort;
	}
	/**
	 * 格式为:LocalIP:LocalPort，如172.19.104.111:8102
	 * @return
	 */
	public String getHostAddress(){
//		return this.hostIP.getHostAddress() + ConfigMessage.DATA_SPLIT_FLAG + this.hostPort;
		return UtilTool.getIP(this.hostIP) + ConfigMessage.DATA_SPLIT_FLAG + this.hostPort;
	}
	/**
	 * 格式为:LocalIP:NATIP，如172.19.104.111:105.22.114.123
	 * @return
	 */
	public String getUniqueAddress(){
//		return this.hostIP.getHostAddress() + ConfigMessage.DATA_SPLIT_FLAG + this.natIP.getHostAddress();
		return UtilTool.getIP(this.hostIP) + ConfigMessage.DATA_SPLIT_FLAG + UtilTool.getIP(this.natIP);
	}
//	public String getHostName() {
//		return hostName;
//	}
//
//	public void setHostName(String hostName) {
//		this.hostName = hostName;
//	}

	public InetAddress getHostIP() {
		return hostIP;
	}

	public void setHostIP(InetAddress hostIP){
		this.hostIP = hostIP;
	}
	public void setHostIP(String IP) {
		InetAddress inetAddress = null;
		if (IP != null) {
			try {
				inetAddress = InetAddress.getByName(IP);
			} catch (UnknownHostException e) {
//				e.printStackTrace();
				logger.error("Unknown Host-Get Host InetAddress Failed!");
			}
		}
		else {
			logger.info("Local IP is NULL, Please Check!");
		}
		this.hostIP = inetAddress;
	}
	
	public int getHostPort() {
		return hostPort;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

//	public String getHostMac() {
//		return hostMac;
//	}
//
//	public void setHostMac(String hostMac) {
//		this.hostMac = hostMac;
//	}

	public InetAddress getNatIP() {
		return natIP;
	}

	public void setNatIP(InetAddress natIP) {
		this.natIP = natIP;
	}

	public void setNatIP(String IP){
		InetAddress inetAddress = null;
		if (IP != null) {
			try {
				inetAddress = InetAddress.getByName(IP);
			} catch (UnknownHostException e) {
//				e.printStackTrace();
				logger.error("Unknown Host-Get NAT InetAddress Failed!");
			}
		}
		else {
			logger.info("NAT IP is NULL, Please Check!");
		}
		this.natIP = inetAddress;
	}
	
	public int getNatPort() {
		return natPort;
	}

	public void setNatPort(int natPort) {
		this.natPort = natPort;
	}

//	public String getNatMac() {
//		return natMac;
//	}
//
//	public void setNatMac(String natMac) {
//		this.natMac = natMac;
//	}

//	public Date getUpdateTime() {
//		return updateTime;
//	}
//
//	public void setUpdateTime(Date updateTime) {
//		this.updateTime = updateTime;
//	}
	public Long getUpdateTimeStamp() {
		return updateTimeStamp;
	}
	public void setUpdateTimeStamp(Long updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}
	public boolean isConnFlag() {
		return connFlag;
	}
	public void setConnFlag(boolean connFlag) {
		this.connFlag = connFlag;
	}
}
