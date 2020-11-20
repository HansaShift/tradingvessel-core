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

@Entity
@Table(name = "obj_hist")
@Inheritance(strategy = InheritanceType.JOINED)
public class ObjHist implements Serializable {

	private static final long serialVersionUID = -3458221490393509305L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_ID")
	private Order orderId;

	@NotNull
	@ManyToOne(targetEntity = Obj.class)
	@JoinColumn(name = "OBJ_ID")
	private Obj objId;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "BUSINESS_TYPE")
	private EBusinessType businessType;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OBJ_HIST_TS_INS", updatable = false)
	private Date objHistTsIns;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OBJ_TS_INS", updatable = false)
	private Date objTsIns;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OBJ_TS_LAST_MDF")
	private Date objTsLastMdf;

	@NotNull
	@Size(max = 150)
	@Column(name = "NAME")
	private String name;

	@Column(name = "CLOSE_DATE")
	private Date closeDate;

	@NotNull
	@Column(name = "VALID_FROM")
	private LocalDateTime validFrom;

	@NotNull
	@Column(name = "VALID_TO")
	private LocalDateTime validTo;

	@NotNull
	@Column(name = "VALID")
	private boolean valid;

	public ObjHist() {
	}

	public ObjHist(@NotNull Obj obj, @NotNull Order order, @NotNull LocalDateTime validFrom,
			@NotNull LocalDateTime validTo, @NotNull boolean valid) {
		super();
		this.orderId = order;
		this.objId = obj;
		this.businessType = obj.getBusinessType();
		this.objTsIns = obj.getTimestampCreate();
		this.objTsLastMdf = obj.getTimestampModified();
		this.name = obj.getName();
		this.closeDate = obj.getCloseDate();
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.valid = valid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrderId() {
		return orderId;
	}

	public void setOrderId(Order orderId) {
		this.orderId = orderId;
	}

	public Obj getObjId() {
		return objId;
	}

	public void setObjId(Obj objId) {
		this.objId = objId;
	}

	public EBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(EBusinessType businessType) {
		this.businessType = businessType;
	}

	public Date getObjHistTsIns() {
		return objHistTsIns;
	}

	public void setObjHistTsIns(Date objHistTsIns) {
		this.objHistTsIns = objHistTsIns;
	}

	public Date getObjTsIns() {
		return objTsIns;
	}

	public void setObjTsIns(Date objTsIns) {
		this.objTsIns = objTsIns;
	}

	public Date getObjTsLastMdf() {
		return objTsLastMdf;
	}

	public void setObjTsLastMdf(Date objTsLastMdf) {
		this.objTsLastMdf = objTsLastMdf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public LocalDateTime getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(LocalDateTime validFrom) {
		this.validFrom = validFrom;
	}

	public LocalDateTime getValidTo() {
		return validTo;
	}

	public void setValidTo(LocalDateTime validTo) {
		this.validTo = validTo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

}
