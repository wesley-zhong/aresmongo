package com.ares.app.domain.Do;

import com.ares.framework.domain.MongoKeyDO;

public class AdminDO extends MongoKeyDO{

	private String  adminId;
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	private String  email;
	private String passwd;
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTeleno() {
		return teleno;
	}
	public void setTeleno(String teleno) {
		this.teleno = teleno;
	}
	private String teleno;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private String name;
}
