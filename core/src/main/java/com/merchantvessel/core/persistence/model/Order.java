package com.merchantvessel.core.persistence.model;

import java.io.Serializable;
import java.time.LocalDateTime;
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

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EDataKind;
import com.merchantvessel.core.business.enumeration.EPrcStatus;

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
	@Column(name = "DATA_KIND")
	private EDataKind dataKind;

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

	@NotNull
	@Column(name = "VAL_DATE")
	private LocalDateTime valueDate;

	@ManyToOne(targetEntity = Obj.class)
	@JoinColumn(name = "OBJ_ID")
	private Obj obj;

	@NotNull
	@ManyToOne(targetEntity = ObjUser.class)
	@JoinColumn(name = "USER_ID")
	private ObjUser objUser;

	@Size(max = 150)
	@Column(name = "OBJ_NAME")
	private String objName;

	@Column(name = "OBJ_CLOSE_DATE")
	private Date objCloseDate;

	public Order() {
	}

	public Order(@NotNull EBusinessType businessType, @NotNull ObjUser user) {
		super();
		this.dataKind = businessType.getDataKind();
		this.prcStatus = this.dataKind == EDataKind.MASTER_DATA ? EPrcStatus.OBJ_BASE_INIT : null;
		this.advText = this.dataKind.getName() + " Order of Type '" + businessType.getName() + "' by user '"
				+ user.getName() + "'";

		this.businessType = businessType;
		this.objUser = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EDataKind getDataKind() {
		return dataKind;
	}

	public void setDataKind(EDataKind orderType) {
		this.dataKind = orderType;
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

	public ObjUser getObjUser() {
		return objUser;
	}

	public void setObjUser(ObjUser objUser) {
		this.objUser = objUser;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		if (this.dataKind == EDataKind.MASTER_DATA) {
			this.objName = objName;
		}

	}

	public Date getObjCloseDate() {
		return objCloseDate;
	}

	public void setObjCloseDate(Date objCloseDate) {
		if (this.dataKind == EDataKind.MASTER_DATA) {
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
		this.obj = obj;
	}

	public @NotNull LocalDateTime getValueDate() {
		return valueDate;
	}

	public void setValueDate(LocalDateTime valueDate) {
		this.valueDate = valueDate;
	}

}
