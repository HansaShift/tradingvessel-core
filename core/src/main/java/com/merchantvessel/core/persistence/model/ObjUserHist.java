package com.merchantvessel.core.persistence.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.merchantvessel.core.business.enumeration.ERole;

@Entity
@Table(name = "OBJ_USER_HIST")
public class ObjUserHist extends ObjHist implements Serializable {
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

	public ObjUserHist() {
		super();
	}

	public ObjUserHist(@NotNull Obj obj, @NotNull Order order, @NotNull LocalDateTime validFrom,
			@NotNull LocalDateTime validTo, @NotNull boolean valid, @Size(max = 50) String username,
			@Size(max = 120) String password, Set<ERole> roleSet) {
		super(obj, order, validFrom, validTo, valid);
		this.username = username;
		this.password = password;
		this.roleSet = roleSet;
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
