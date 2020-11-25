package com.merchantvessel.core.persistence.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.ERole;

@Entity
@Table(name = "OBJ_USER", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class ObjUser extends Obj implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4442311010636937427L;

	@Size(max = 50)
	private String username;

	@Size(max = 120)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "ROLE_SET")
	private Set<ERole> roleSet = new HashSet<>();

	public ObjUser() {
		super();
	}

	public ObjUser(String username, String name, String password) {
		super(name, EBusinessType.OBJ_USER);
		this.username = username;
		this.password = password;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<ERole> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<ERole> roleSet) {
		this.roleSet = roleSet;
	}

}
