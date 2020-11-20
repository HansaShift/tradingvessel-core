package com.merchantvessel.core.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EDataKind;

@Entity
@Table(name = "order_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderObjUser extends Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "OBJ_USER_NAME")
	private String username;

	@Size(max = 120)
	@Column(name = "OBJ_PASSWORD")
	private String password;

	public OrderObjUser() {
		super();
	}

	public OrderObjUser(@NotNull EDataKind dataKind, @NotNull EBusinessType businessType, @NotNull ObjUser user) {
		super(dataKind, businessType, user);
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
