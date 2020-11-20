package com.merchantvessel.core.intf.dto;

import javax.validation.constraints.Size;

import com.merchantvessel.core.persistence.model.OrderObjUser;

public class DtoOrderObjUser extends DtoOrder {

	private String username;

	private String password;
	
	public DtoOrderObjUser(OrderObjUser orderObjUser) {
		super(orderObjUser);
		this.username = orderObjUser.getUsername();
		this.password = orderObjUser.getPassword();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
