/**
 * 
 */
package com.fcsbbc.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author luo.changshu
 *
 */
public class SQLLiteDBInitiation {

	private static Logger logger = LoggerFactory.getLogger(SQLLiteDBInitiation.class.getName());
	private Connection connection;
	private PreparedStatement preparedStatement;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String createTableSql = "CREATE TABLE IF NOT EXISTS order_info ("
				+ "order_id				varchar(128),"
				+ "order_trade_side		INT,"
				+ "order_open_side		INT,"
				+ "order_price			DECIMAL(13,5),"
				+ "order_number			DECIMAL(13,5),"
				+ "instrument_id		varchar(18),"
				+ "order_status			INT,"
				+ "order_time			datetime"
				+ ");";
		createTableSql = "CREATE TABLE IF not exists audit_info ("
				+ "audit_id 			INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "user_id 				varchar(18) NOT NULL,"
				+ "audit_number 		varchar(128) NOT NULL,"
				+ "audit_time 			datetime DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))"
				+ ");";
		createTableSql = "CREATE TABLE IF NOT EXISTS instrument_info ("
				+ "instruemnt_id 					varchar(18) NOT NULL PRIMARY KEY,"
				+ "product_id 						varchar(18) NOT NULL,"
				+ "base_price 						decimal(13,5) NOT NULL,"
				+ "speculate_buy_margin_rate 		decimal(13,9) NOT NULL,"
				+ "speculate_sell_margin_rate 		decimal(13,9) NOT NULL,"
				+ "hedge_buy_margin_rate 			decimal(13,9) NOT NULL,"
				+ "hedge_sell_margin_rate 			decimal(13,9) NOT NULL,"
				+ "rise_limit_rate 					decimal(13,9) NOT NULL,"
				+ "fall_limit_rate 					decimal(13,9) NOT NULL,"
				+ "launch_date 						date NOT NULL,"
				+ "expire_date 						date NOT NULL,"
				+ "start_delivery_date 				date NOT NULL,"
				+ "end_delivery_date 				date NOT NULL"
				+ ")";
		createTableSql = "CREATE TABLE IF NOT EXISTS product_info ("
				+ "product_id 						varchar(18) NOT NULL PRIMARY KEY,"
				+ "product_name 					varchar(50) NOT NULL,"
				+ "product_unit_value 				decimal(13,9) NOT NULL,"
				+ "product_unit 					varchar(50) NOT NULL,"
				+ "quote_unit 						varchar(50) NOT NULL,"
				+ "minimum_price_movement_value 	decimal(13,9) NOT NULL,"
				+ "minimum_price_movement 			varchar(50) NOT NULL,"
				+ "minimum_trade_margin_rate 		decimal(13,9) NOT NULL,"
				+ "minimum_delivery_unit_value 		decimal(13,9),"
				+ "minimum_delivery_unit 			varchar(50),"
				+ "delivery_mode 					varchar(50) NOT NULL DEFAULT '实物交割',"
				+ "listed_exchange 					varchar(50) NOT NULL DEFAULT 'SHFE'"
				+ ")";
		createTableSql = "CREATE TABLE IF NOT EXISTS region_info ("
				+ "region_id 			int(9) NOT NULL PRIMARY KEY,"
				+ "region_code 			varchar(50) NOT NULL,"
				+ "region_name 			varchar(50) NOT NULL,"
				+ "parent_id 			smallint(5) NOT NULL,"
				+ "region_level 		smallint(5) NOT NULL,"
				+ "region_order 		smallint(5) NOT NULL,"
				+ "region_name_en 		varchar(100) NOT NULL,"
				+ "region_shortname_en 	varchar(10) NOT NULL,"
				+ "is_active 			smallint(5) NOT NULL DEFAULT '1'"
				+ ")";
		createTableSql = "CREATE TABLE IF NOT EXISTS user_detail_info ("
				+ "user_system_id 		Integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "user_id 				varchar(18) NOT NULL,"
				+ "user_telephone 		varchar(11),"
				+ "corporation 			varchar(50),"
				+ "manager 				varchar(50),"
				+ "manager_telephone 	varchar(11)," 
				+ "user_province 		varchar(50),"
				+ "user_city 			varchar(50),"
				+ "user_county 			varchar(50),"
				+ "user_detail_address 	varchar(1024),"
				+ "user_fax 			varchar(20),"
				+ "is_active 			smallint(5) NOT NULL DEFAULT '1'"
				+ ")";
		createTableSql = "CREATE TABLE IF NOT EXISTS user_general_info ("
				+ "user_id 				varchar(18) NOT NULL PRIMARY KEY,"
				+ "user_name 			varchar(1024) NOT NULL,"
				+ "user_password 		varchar(128) NOT NULL,"
				+ "user_role 			smallint(5) NOT NULL,"
				+ "user_ca_path 		varchar(1024) NOT NULL,"
				+ "is_active 			smallint(5) NOT NULL DEFAULT '1'"
				+ ")";
		createTableSql = "CREATE TABLE user_login_history ("
				+ "login_info_id 		Integer NOT NULL PRIMARY KEY AUTOINCREMENT,"
				+ "user_id 				varchar(18) NOT NULL,"
				+ "login_ip 			varchar(50) NOT NULL,"
				+ "login_time 			timestamp NOT NULL DEFAULT (datetime(CURRENT_TIMESTAMP,'localtime'))"
				+ ")";
		
		SQLLiteDBInitiation sqlLiteDBInitiation = new SQLLiteDBInitiation();
		sqlLiteDBInitiation.createTable(createTableSql);
	}
	public SQLLiteDBInitiation() {
		connection = new SQLLiteBaseOperation().getSQLBaseOperation().getConnection();
	}
	
	public void createTable(String createTableSql) {
		try {
			preparedStatement = connection.prepareStatement(createTableSql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Data Table Create Exception in <SQLLiteDBInitiation:createTable>", e.getMessage());
			throw new RuntimeException("Exception in <SQLLiteDBInitiation:createTable>", e);
		}finally {
			new SQLLiteBaseOperation().releaseResource(preparedStatement, connection);
		}
	}
}
