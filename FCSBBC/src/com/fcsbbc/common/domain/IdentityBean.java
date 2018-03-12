/**
 * 
 */
package com.fcsbbc.common.domain;

/**
 * @author luo.changshu
 *
 */
public class IdentityBean {
	public int identity_id;
	public String user_id;
	public String user_telephone;
	public int identity_code;
	public String generate_time;
	public int identity_status;
	public int identity_function;
	
	public IdentityBean() {
		
	}
	
	public IdentityBean(int identity_id, String user_id, String user_telephone, int identity_code, String generate_time,
			int identity_status) {
		super();
		this.identity_id = identity_id;
		this.user_id = user_id;
		this.user_telephone = user_telephone;
		this.identity_code = identity_code;
		this.generate_time = generate_time;
		this.identity_status = identity_status;
	}
	
	public IdentityBean(int identity_id, String user_id, String user_telephone, int identity_code, String generate_time,
			int identity_status, int identity_function) {
		super();
		this.identity_id = identity_id;
		this.user_id = user_id;
		this.user_telephone = user_telephone;
		this.identity_code = identity_code;
		this.generate_time = generate_time;
		this.identity_status = identity_status;
		this.identity_function = identity_function;
	}

	public int getIdentity_id() {
		return identity_id;
	}
	public void setIdentity_id(int identity_id) {
		this.identity_id = identity_id;
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUser_telephone() {
		return user_telephone;
	}
	public void setUser_telephone(String user_telephone) {
		this.user_telephone = user_telephone;
	}
	
	public int getIdentity_code() {
		return identity_code;
	}
	public void setIdentity_code(int identity_code) {
		this.identity_code = identity_code;
	}
	
	public String getGenerate_time() {
		return generate_time;
	}
	public void setGenerate_time(String generate_time) {
		this.generate_time = generate_time;
	}
	
	public int getIdentity_status() {
		return identity_status;
	}
	public void setIdentity_status(int identity_status) {
		this.identity_status = identity_status;
	}
	
	public int getIdentity_function() {
		return identity_function;
	}
	public void setIdentity_function(int identity_function) {
		this.identity_function = identity_function;
	}
}
