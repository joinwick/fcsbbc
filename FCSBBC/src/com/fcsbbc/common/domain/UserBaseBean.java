/**
 * 
 */
package com.fcsbbc.common.domain;

/**
 * @author luo.changshu
 *
 */
public class UserBaseBean {
	private String user_id;
	private String user_name;
	private String user_password;
	private String user_telephone;
	private int user_role;
	private String user_ca_path;
	private int is_active;
	private String user_system_id;
	private String generate_time;
	
	public UserBaseBean() {
		
	}
	
	/**
	 * @param user_id
	 * @param user_role
	 * @param user_ca_path
	 * @param is_active
	 * @param user_system_id
	 */
	public UserBaseBean(String user_id, int user_role, String user_ca_path, int is_active, String user_system_id) {
		super();
		this.user_id = user_id;
		this.user_role = user_role;
		this.user_ca_path = user_ca_path;
		this.is_active = is_active;
		this.user_system_id = user_system_id;
	}
	
	/**
	 * @param user_id
	 * @param user_telephone
	 * @param user_role
	 * @param user_ca_path
	 * @param is_active
	 * @param user_system_id
	 */
	public UserBaseBean(String user_id, String user_telephone, int user_role, String user_ca_path, int is_active,
			String user_system_id) {
		super();
		this.user_id = user_id;
		this.user_telephone = user_telephone;
		this.user_role = user_role;
		this.user_ca_path = user_ca_path;
		this.is_active = is_active;
		this.user_system_id = user_system_id;
	}

	/**
	 * @param user_id
	 * @param user_name
	 * @param user_password
	 * @param user_telephone
	 * @param user_role
	 * @param user_ca_path
	 * @param is_active
	 * @param user_system_id
	 * @param generate_time
	 */
	public UserBaseBean(String user_id, String user_name, String user_password, String user_telephone, int user_role,
			String user_ca_path, int is_active, String user_system_id, String generate_time) {
		super();
		this.user_id = user_id;
		this.user_name = user_name;
		this.user_password = user_password;
		this.user_telephone = user_telephone;
		this.user_role = user_role;
		this.user_ca_path = user_ca_path;
		this.is_active = is_active;
		this.user_system_id = user_system_id;
		this.generate_time = generate_time;
	}

	public String getUser_system_id() {
		return user_system_id;
	}
	public void setUser_system_id(String user_system_id) {
		this.user_system_id = user_system_id;
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	
	public String getUser_telephone() {
		return user_telephone;
	}
	public void setUser_telephone(String user_telephone) {
		this.user_telephone = user_telephone;
	}
	
	public int getUser_role() {
		return user_role;
	}
	public void setUser_role(int user_role) {
		this.user_role = user_role;
	}
	
	public String getUser_ca_path() {
		return user_ca_path;
	}
	public void setUser_ca_path(String user_ca_path) {
		this.user_ca_path = user_ca_path;
	}
	
	public int getIs_active() {
		return is_active;
	}
	public void setIs_active(int is_active) {
		this.is_active = is_active;
	}
	
	public String getGenerate_time() {
		return generate_time;
	}
	public void setGenerate_time(String generate_time) {
		this.generate_time = generate_time;
	}
}
