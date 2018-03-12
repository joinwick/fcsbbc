package com.fcsbbc.utils;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fcsbbc.net.p2p.udp.Configuration;

public class ConfigMessage {
	/**软件版本号:1.20170603*/
	public static String CLIENT_VERISION = "1.20170603";
	/**软件版本维护者:baron_0.1*/
	public static String CLIENT_SUBVER = "baron_0.1";
	/**软件版本号:1.20170616*/
	public static String SERVER_VERISION = "1.20170616";
	/**软件版本维护者:baron_0.1*/
	public static String SERVER_SUBVER = "baron_0.1";
	/**客户端请求与服务器建立连接标识(client)：Client_Conn_Server*/
	public static String CLIENT_CONN_SERVER_FLAG = "Client_Conn_Server";
	/**服务器同意与客户端建立连接(server):Client_Conn_Server_OK*/
	public static String CLIENT_CONN_SERVER_FLAG_OK ="Client_Conn_Server_OK";
	/**客户端请求获得在线的节点信息(client):Get_Random_Nodes*/
	public static String GET_RANDOM_NODES = "Get_Random_Nodes";
	/**鉴于网络初始化，服务器直接与初始节点连接并提供后续服务:Generic_Conn_Server_Client*/
	public static String GENERIC_CONN_SERVER_CLIENT = "Generic_Conn_Server_Client";
	/**鉴于网络初始化，客户端与服务器建立连接:Generic_Conn_Server_Client_OK*/
	public static String GENERIC_CONN_SERVER_CLIENT_OK = "Generic_Conn_Server_Client_OK";
	/**服务器成功返回节点信息(server):Get_Random_Nodes_OK*/
	public static String GET_RANDOM_NODES_OK = "Get_Random_Nodes_OK";
	/**客户端请求与某个节点建立连接请求(client)：Client_Conn_Client*/
	public static String CLIENT_CONN_CLIENT_FLAG = "Client_Conn_Client";
	/**服务器向某客户端发送其它客户端与其建立连接的请求信息(server):Client_Request_Client*/
	public static String CLIENT_REQUEST_CLIENT = "Client_Request_Client";
	/**客户端向服务器返回同意其它客户端建立连接的请求(client):Client_Request_Client_OK*/
	public static String CLIENT_REQUEST_CLIENT_OK = "Client_Request_Client_OK";
	/**服务器向请求建立客户端连接的客户端返回对方节点同意建立连接(server):Client_Conn_Client_OK*/
	public static String CLIENT_CONN_CLIENT_FLAG_OK = "Client_Conn_Client_OK";
	/**客户端向某节点（实际为映射后的NAT地址）直接发送连接请求(client):Client_Conn_NAT*/
	public static String CLIENT_CONN_NAT = "Client_Conn_NAT";
	/**客户端返回请求连接成功标识(client):Client_Conn_NAT_OK*/
	public static String CLIENT_CONN_NAT_OK = "Client_Conn_NAT_OK";
	/**请求连接成功(client):Connect_Request_OK*/
	public static String CONNECT_REQUEST_OK = "Connect_Request_OK";
	/**服务器拒绝客户端的连接请求(server):Server_Reject_Client_Conn*/
	public static String SERVER_REJECT_CLIENT_CONN = "Server_Reject_Client_Conn";
	/**客户端拒绝服务器的连接请求(client):Client_Reject_Server_Conn*/
	public static String CLIENT_REJECT_SERVER_CONN = "Client_Reject_Server_Conn";
	/**客户端拒绝客户端的连接请求(client):Client_Reject_Client_Conn*/
	public static String CLIENT_REJECT_CLIENT_CONN = "Client_Reject_Client_Conn";
//	/**打孔请求_成功回复：Request_Ok*/
//	public static String REQUEST_FLAG_OK = "Request_Ok";
//	/**NAT成功后_请求：Nat_Request_*/
//	public static String NAT_REQUEST_FLAG = "Nat_Request_";
//	/**NAT成功后_请求_成功回复：Nat_Request_Ok**/
//	public static String NAT_REQUEST_FLAG_OK = "Nat_Request_Ok";
	/**NAT 心跳维护：HeartBeat_*/
	public static String NAT_MAINTAIN_HEARTBEAT_FLAG = "HeartBeat_";
	/**NAT 心跳维护_响应成功：HeartBeat_Ok*/
	public static String NAT_MAINTAIN_HEARTBEAT_FLAG_OK = "HeartBeat_Ok";
	/**服务端IP**/
//	public static String SERVER_IP = Configuration.getString("server.ip");
	public static final String SERVER_IP = Configuration.getUniqueValue("server.ip");
	/**{客户端连接/服务器启用}端口*/
	public static final int SERVER_PORT = Configuration.getInt("server.port");
	/**NAT SESSION 超时时间 单位（毫秒）*/
	public static final long NAT_TIMEOUT_MS = Configuration.getInt("nat.timeout.ms");
	/**MYSQL JDBC DRIVER*/
	public static final String MYSQL_JDBC_DRIVER = Configuration.getString("mysql.jdbc.driver");
	/**MYSQL JDBC URL*/
	public static final String MYSQL_JDBC_URL = Configuration.getString("mysql.jdbc.url");
	/**MYSQL JDBC USERNAME*/
	public static final String MYSQL_JDBC_USERNAME = Configuration.getString("mysql.jdbc.username");
	/**MYSQL JDBC PASSWORD*/
	public static final String MYSQL_JDBC_PASSWORD = Configuration.getString("mysql.jdbc.password");
	/**SQLLITE JDBC_DRIVER*/
	public static final String SQLLITE_JDBC_DRIVER = Configuration.getString("sqllite.jdbc.driver");
	/**SQLLITE JDBC URL*/
	public static final String SQLLITE_JDBC_URL = Configuration.getString("sqllite.jdbc.url");
	/**默认数据库：1-SqlLite*/
	public static final int DEFAULT_DATABASE_TYPE = Integer.parseInt(Configuration.getString("default.database.type"));
	/**默认编码格式：UTF-8 */
	public static String ENCODE_FORMAT_UTF8 = "UTF-8";
	/**默认端口号：8102*/
	public static int SYSTEM_PORT = 8102;
	/**默认的消息分隔符：","*/
	public static String MESSAGE_SPLIT_FLAG = ",";
	/**默认的数据分隔符：":"*/
	public static String DATA_SPLIT_FLAG = ":";
	/**默认的信息分隔符："#"*/
	public static String INFO_SPLIT_FLAG = "#";
	/**默认的最大连接节点数：10*/
	public static int DEFAULT_MAX_CONNECT_NODE = 10;
	/**默认的最小连接节点数：5*/
	public static int DEFAULT_MIN_CONNECT_NODE = 5;
	/**主机Local IP信息默认的起始构造标志：LocalIP*/
	public static String NODE_LOCAL_INFO_FLAG = "LocalIP";
	/**主机NAT IP信息默认的起始构造标志：NATIP*/
	public static String NODE_NAT_INFO_FLAG = "NATIP";
	/**记录时间的起始构造标志：ConnTime*/
	public static String NODE_TIME_INFO_FLAG = "ConnTime";
	/**连接成功与否的起始构造标志：ConnFlag*/
	public static String NODE_CONN_INFO_FLAG = "ConnFlag";
	/**UTC时区*/
	public static final TimeZone UTC_TIME_ZONE = TimeZone.getTimeZone("UTC");
	/**当前本机所在时区*/
	public static final TimeZone LOCAL_TIME_ZONE = Calendar.getInstance(Locale.getDefault()).getTimeZone();
	/**订单买卖方向：Buy*/
	public static final String ORDER_TRADESIDE_BUY = "Buy";
	/**订单买卖方向：Sell*/
	public static final String ORDER_TRADESIDE_SELL = "Sell";
	/**订单状态：New*/
	public static final String ORDER_STATUS_NEW = "New";
	/**订单状态：Closed*/
	public static final String ORDER_STATUS_CLOSED = "Closed";
	/**订单状态：Processing*/
	public static final String ORDER_STATUS_PROCESSING = "Processing";
	/**订单状态：Cancelled*/
	public static final String ORDER_STATUS_CANCELLED = "Cancelled";
	/**订单状态：Holding*/
	public static final String ORDER_STATUS_HOLDING = "Holding";
	/**验证码有效时间：30*/
	public static final int IDENTITY_CODE_TIME_COUNT_DOWN = 30;
	/**阿里云短信模板-用户注册：SMS_125025516*/
	public static final String SHORT_MESSAGE_MODEL_USER_GEGISTER = "SMS_125025516";
	/**阿里云短信模板-密码找回：SMS_125115326*/
	public static final String SHORT_MESSAGE_MODEL_PASSWORD_RECOVERY = "SMS_125115326";
	/**验证码重发时间：45秒*/
	public static final int IDENTITY_CODE_RESENT_TIME_COUNT_DOWN = 45;
}
