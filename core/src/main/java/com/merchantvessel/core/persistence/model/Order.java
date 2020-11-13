package com.merchantvessel.core.persistence.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EPrcStatus;
import com.merchantvessel.core.business.service.LogSvc;
import com.merchantvessel.core.business.enumeration.EOrderType;

@Entity
@Table(name = "order_base")
@Inheritance(strategy = InheritanceType.JOINED)
public class Order implements Serializable {

	private static final long serialVersionUID = -3458221490393509305L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "ORDER_TYPE")
	private EOrderType orderType;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "BUSINESS_TYPE")
	private EBusinessType businessType;

	@Enumerated(EnumType.STRING)
	@Column(name = "PRC_STATUS")
	private EPrcStatus prcStatus;

	@Column(name = "ADV_TEXT")
	private String advText;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_INS", updatable = false)
	private Date timestampCreate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_LAST_MDF")
	private Date timestampModified;

	@ManyToOne(targetEntity = Obj.class)
	@JoinColumn(name = "OBJ_ID")
	private Obj obj;

	@NotNull
	@ManyToOne(targetEntity = ObjUser.class)
	@JoinColumn(name = "USER_ID")
	private ObjUser user;

	@Size(max = 150)
	@Column(name = "OBJ_NAME")
	private String objName;

	@Column(name = "OBJ_CLOSE_DATE")
	private Date objCloseDate;

	public Order() {
	}

	public Order(@NotNull EOrderType orderType, @NotNull EBusinessType businessType, @NotNull ObjUser user) {
		super();
		this.prcStatus = orderType == EOrderType.MASTER_DATA ? EPrcStatus.OBJ_BASE_INIT : null;
		this.advText = orderType.getName() + " Order of Type '" + businessType.getName() + "' by user '" + user.getName() + "'";
		this.orderType = orderType;
		this.businessType = businessType;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(EOrderType orderType) {
		this.orderType = orderType;
	}

	public EBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(EBusinessType businessType) {
		this.businessType = businessType;
	}

	public EPrcStatus getPrcStatus() {
		return prcStatus;
	}

	public void setPrcStatus(EPrcStatus prcStatus) {
		this.prcStatus = prcStatus;
	}

	public String getAdvText() {
		return advText;
	}

	public void setAdvText(String advText) {
		this.advText = advText;
	}

	public Date getTimestampCreate() {
		return timestampCreate;
	}

	public void setTimestampCreate(Date timestampCreate) {
		this.timestampCreate = timestampCreate;
	}

	public Date getTimestampModified() {
		return timestampModified;
	}

	public void setTimestampModified(Date timestampModified) {
		this.timestampModified = timestampModified;
	}

	public ObjUser getUser() {
		return user;
	}

	public void setUser(ObjUser user) {
		this.user = user;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		if (this.orderType != EOrderType.MASTER_DATA) {
			System.err.println("Only master data orders can have an object name!");
		} else {
			this.objName = objName;
		}

	}

	public Date getObjCloseDate() {
		return objCloseDate;
	}

	public void setObjCloseDate(Date objCloseDate) {
		if (this.orderType != EOrderType.MASTER_DATA) {
			System.err.println("Only master data orders can have an object close date!");
		} else {
			this.objCloseDate = objCloseDate;
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Obj getObj() {
		return obj;
	}

	public void setObj(Obj obj) {
		if (this.orderType != EOrderType.MASTER_DATA) {
			System.err.println("Only master data orders can have an object!");
		} else {
			this.obj = obj;
		}
	}

}
