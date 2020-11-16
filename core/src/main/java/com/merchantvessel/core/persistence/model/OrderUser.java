package com.merchantvessel.core.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EOrderType;

@Entity
@Table(name = "order_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderUser extends Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "USER_NAME")
	private String userName;

	public OrderUser() {
		super();
	}

	public OrderUser(@NotNull EOrderType orderType, @NotNull EBusinessType businessType, @NotNull ObjUser user) {
		super(orderType, businessType, user);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
