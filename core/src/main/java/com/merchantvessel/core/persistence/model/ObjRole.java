package com.merchantvessel.core.persistence.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.merchantvessel.core.business.enumeration.EObjType;

@Entity
@Table(name = "OBJ_ROLE")
public class ObjRole extends Obj implements Serializable {

	private static final long serialVersionUID = 1L;

	public ObjRole(String name) {
		super(name, EObjType.OBJ_ROLE);
	}

	public ObjRole() {

	}

}
