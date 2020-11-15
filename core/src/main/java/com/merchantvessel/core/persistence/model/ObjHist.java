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

	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "OBJ_ORDER_CREATE_ID")
	private Order orderCreate;

	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "OBJ_ORDER_MDF_ID")
	private Order orderMdf;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OBJ_HIST_TS_INS", updatable = false)
	private Date timestampCreateObjHist;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OBJ_TS_INS", updatable = false)
	private Date timestampCreate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OBJ_TS_LAST_MDF")
	private Date timestampModified;

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

	public ObjHist() {
	}

	public ObjHist(@NotNull Obj obj, @NotNull Order order, @NotNull LocalDateTime validFrom, @NotNull LocalDateTime validTo) {
		this.objId = obj;
		this.orderId = order;
		this.businessType = obj.getBusinessType();
		this.orderCreate = obj.getOrderCreate();
		this.orderMdf = obj.getOrderMdf();
		this.timestampCreate = obj.getTimestampCreate();
		this.timestampModified = obj.getTimestampModified();
		this.name = obj.getName();
		this.closeDate = obj.getCloseDate();
		this.validFrom = validFrom;
		this.validTo = validTo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Obj getObjId() {
		return objId;
	}

	public void setObjId(Obj objId) {
		this.objId = objId;
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

	public void setBusinessType(EBusinessType businessType) {
		this.businessType = businessType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public EBusinessType getBusinessType() {
		return businessType;
	}

	public Order getOrder() {
		return orderMdf;
	}

	public void setOrder(Order order) {
		this.orderMdf = order;
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

	public Order getOrderCreate() {
		return orderCreate;
	}

	public void setOrderCreate(Order orderCreate) {
		this.orderCreate = orderCreate;
	}

	public Order getOrderMdf() {
		return orderMdf;
	}

	public void setOrderMdf(Order orderMdf) {
		this.orderMdf = orderMdf;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Date getTimestampCreateObjHist() {
		return timestampCreateObjHist;
	}

	public void setTimestampCreateObjHist(Date timestampCreateObjHist) {
		this.timestampCreateObjHist = timestampCreateObjHist;
	}

}
