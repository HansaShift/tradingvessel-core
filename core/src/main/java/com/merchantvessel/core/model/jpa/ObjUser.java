package com.merchantvessel.core.model.jpa;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "OBJ_USER", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class ObjUser extends Obj implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4442311010636937427L;

	@NotBlank
	@Size(max = 50)
	private String username;

	@NotBlank
	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	private Set<ObjRole> roles = new HashSet<>();

	public ObjUser() {
		super();
	}

	public ObjUser(String username, String name, String password) {
		super(name);
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

	public Set<ObjRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<ObjRole> roles) {
		this.roles = roles;
	}

}
