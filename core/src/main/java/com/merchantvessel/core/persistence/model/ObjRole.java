package com.merchantvessel.core.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.ERole;

@Entity
@Table(name = "OBJ_ROLE")
public class ObjRole extends Obj implements Serializable {

	private static final long serialVersionUID = 1L;

	@Size(max = 150)
	@Column(name = "ENUM_KEY")
	private String enumKey;

	public ObjRole(ERole role) {
		super(role.getName(), EBusinessType.OBJ_ROLE);
		this.enumKey = role.toString();
	}

	public ObjRole() {

	}

	public String getEnumKey() {
		return enumKey;
	}

	public void setEnumKey(String enumKey) {
		this.enumKey = enumKey;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
