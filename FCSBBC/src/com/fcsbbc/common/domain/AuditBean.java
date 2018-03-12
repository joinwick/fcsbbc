/**
 * 
 */
package com.fcsbbc.common.domain;

//import java.sql.Timestamp;

/**
 * @author luo.changshu
 *
 */
public class AuditBean {
	public int audit_id;
	public String user_id;
	public String audit_number;
	public int user_role;
	public String audit_time;
	
	public AuditBean(int audit_id, String user_id, String audit_number, int user_role, String audit_time) {
		super();
		this.audit_id = audit_id;
		this.user_id = user_id;
		this.audit_number = audit_number;
		this.user_role = user_role;
		this.audit_time = audit_time;
	}
	public int getUser_role() {
		return user_role;
	}
	public void setUser_role(int user_role) {
		this.user_role = user_role;
	}
	
	public int getAudit_id() {
		return audit_id;
	}
	public void setAudit_id(int audit_id) {
		this.audit_id = audit_id;
	}
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getAudit_number() {
		return audit_number;
	}
	public void setAudit_number(String audit_number) {
		this.audit_number = audit_number;
	}
	
	public String getAudit_time() {
		return audit_time;
	}
	public void setAudit_time(String audit_time) {
		this.audit_time = audit_time;
	}
}
