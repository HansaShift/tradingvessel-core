package com.merchantvessel.core.persistence.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.ERole;

@Entity
@Table(name = "order_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderObjUser extends Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "OBJ_USER_NAME")
	private String objUsername;

	@Size(max = 120)
	@Column(name = "OBJ_PASSWORD")
	private String objPassword;

	@ElementCollection
	@CollectionTable(name = "OBJ_ROLE_LIST")
	private Set<ERole> objRoleSet;

	public OrderObjUser() {
		super();
	}

	public OrderObjUser(@NotNull EBusinessType businessType, @NotNull ObjUser user) {
		super(businessType, user);
	}

	public String getUsername() {
		return objUsername;
	}

	public void setUsername(String username) {
		this.objUsername = username;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPassword() {
		return objPassword;
	}

	public void setPassword(String password) {
		this.objPassword = password;
	}

	public Set<ERole> getObjRoleSet() {
		return objRoleSet;
	}

	public void setObjRoleSet(Set<ERole> set) {
		this.objRoleSet = set;
	}

	public void addEnumRole(ERole enumRole) {
		if (this.objRoleSet == null) {
			this.objRoleSet = new HashSet<ERole>();
		}
		this.objRoleSet.add(enumRole);
	}

}
