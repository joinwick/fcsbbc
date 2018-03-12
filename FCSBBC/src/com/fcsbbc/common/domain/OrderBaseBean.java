/**
 * 
 */
package com.fcsbbc.common.domain;

import java.math.BigDecimal;

/**
 * @author luo.changshu
 *
 */
public class OrderBaseBean {

	private String order_id;
	private String order_trade_side;
	private String order_open_side;
	private BigDecimal order_price;
	private BigDecimal order_number;
	private String instrument_id;
	private String order_status;
	private String order_time;
	private String user_system_id;
	private BigDecimal order_fee;
	
	
	/**
	 * @param order_id
	 * @param order_trade_side
	 * @param order_price
	 * @param order_number
	 * @param instrument_id
	 * @param order_status
	 */
	public OrderBaseBean(String order_id, String order_trade_side, BigDecimal order_price, BigDecimal order_number,
			String instrument_id, String order_status) {
		super();
		this.order_id = order_id;
		this.order_trade_side = order_trade_side;
		this.order_price = order_price;
		this.order_number = order_number;
		this.instrument_id = instrument_id;
		this.order_status = order_status;
	}
	/**
	 * @param order_id
	 * @param order_trade_side
	 * @param order_price
	 * @param order_number
	 * @param instrument_id
	 * @param order_status
	 * @param order_time
	 */
	public OrderBaseBean(String order_id, String order_trade_side, BigDecimal order_price, BigDecimal order_number,
			String instrument_id, String order_status, String order_time) {
		super();
		this.order_id = order_id;
		this.order_trade_side = order_trade_side;
		this.order_price = order_price;
		this.order_number = order_number;
		this.instrument_id = instrument_id;
		this.order_status = order_status;
		this.order_time = order_time;
	}
	/**
	 * @param order_id
	 * @param order_trade_side
	 * @param order_price
	 * @param order_number
	 * @param instrument_id
	 * @param order_status
	 * @param order_time
	 * @param user_system_id
	 */
	public OrderBaseBean(String order_id, String order_trade_side, BigDecimal order_price, BigDecimal order_number,
			String instrument_id, String order_status, String order_time, String user_system_id) {
		super();
		this.order_id = order_id;
		this.order_trade_side = order_trade_side;
		this.order_price = order_price;
		this.order_number = order_number;
		this.instrument_id = instrument_id;
		this.order_status = order_status;
		this.order_time = order_time;
		this.user_system_id = user_system_id;
	}
	/**
	 * @param order_id
	 * @param order_trade_side
	 * @param order_price
	 * @param order_number
	 * @param instrument_id
	 * @param order_status
	 * @param order_time
	 * @param order_fee
	 */
	public OrderBaseBean(String order_id, String order_trade_side, BigDecimal order_price, BigDecimal order_number,
			String instrument_id, String order_status, String order_time, BigDecimal order_fee) {
		super();
		this.order_id = order_id;
		this.order_trade_side = order_trade_side;
		this.order_price = order_price;
		this.order_number = order_number;
		this.instrument_id = instrument_id;
		this.order_status = order_status;
		this.order_time = order_time;
		this.order_fee = order_fee;
	}
	/**
	 * @param order_id
	 * @param order_trade_side
	 * @param order_open_side
	 * @param order_price
	 * @param order_number
	 * @param instrument_id
	 * @param order_status
	 * @param order_time
	 * @param user_system_id
	 * @param order_fee
	 */
	public OrderBaseBean(String order_id, String order_trade_side, String order_open_side, BigDecimal order_price,
			BigDecimal order_number, String instrument_id, String order_status, String order_time, String user_system_id,
			BigDecimal order_fee) {
		super();
		this.order_id = order_id;
		this.order_trade_side = order_trade_side;
		this.order_open_side = order_open_side;
		this.order_price = order_price;
		this.order_number = order_number;
		this.instrument_id = instrument_id;
		this.order_status = order_status;
		this.order_time = order_time;
		this.user_system_id = user_system_id;
		this.order_fee = order_fee;
	}
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	
	public String getOrder_trade_side() {
		return order_trade_side;
	}
	public void setOrder_trade_side(String order_trade_side) {
		this.order_trade_side = order_trade_side;
	}
	
	public String getOrder_open_side() {
		return order_open_side;
	}
	public void setOrder_open_side(String order_open_side) {
		this.order_open_side = order_open_side;
	}
	
	public BigDecimal getOrder_price() {
		return order_price;
	}
	public void setOrder_price(BigDecimal order_price) {
		this.order_price = order_price;
	}
	
	public BigDecimal getOrder_number() {
		return order_number;
	}
	public void setOrder_number(BigDecimal order_number) {
		this.order_number = order_number;
	}
	
	public String getInstrument_id() {
		return instrument_id;
	}
	public void setInstrument_id(String instrument_id) {
		this.instrument_id = instrument_id;
	}
	
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	
	public String getOrder_time() {
		return order_time;
	}
	public void setOrder_time(String order_time) {
		this.order_time = order_time;
	}
	
	public String getUser_system_id() {
		return user_system_id;
	}
	public void setUser_system_id(String user_system_id) {
		this.user_system_id = user_system_id;
	}
	
	public BigDecimal getOrder_fee() {
		return order_fee;
	}
	public void setOrder_fee(BigDecimal order_fee) {
		this.order_fee = order_fee;
	}
}
