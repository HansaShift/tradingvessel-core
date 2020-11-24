package com.merchantvessel.core.intf.dto;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.merchantvessel.core.business.enumeration.ERole;

public class SignupRequest {

	@NotBlank
	@Size(min = 3, max = 20)
	private String username;
//
//    @NotBlank
//    @Size(max = 50)
//    @Email
//    private String email;
//
	private Set<ERole> roleSet;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<ERole> getRoleSet() {
		return roleSet;
	}

	public void setRoleSet(Set<ERole> role) {
		this.roleSet = role;
	}

}
