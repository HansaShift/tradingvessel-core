package com.merchantvessel.core.model.jpa;

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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.merchantvessel.core.model.enumeration.EObjType;

@Entity
@Table(name = "obj")
@Inheritance(strategy = InheritanceType.JOINED)
public class Obj implements Serializable {

	private static final long serialVersionUID = -3458221490393509305L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@NotNull
	@Size(max = 150)
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private EObjType objType;

	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_CREATE_ID")
	private Order orderCreate;

	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_LAST_MDF_ID")
	private Order orderLastMdf;

	@ManyToOne(targetEntity = Order.class)
	@JoinColumn(name = "ORDER_IN_WORK_ID")
	private Order order;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_INS", updatable = false)
	private Date timestampCreate;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TS_LAST_MODIFIED")
	private Date timestampModified;

	public Obj(@NotNull String name, @NotNull EObjType objType) {
		this.name = name;
		this.objType = objType;
	}

	public Obj() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public EObjType getObjType() {
		return objType;
	}

	public void setObjType(EObjType objType) {
		this.objType = objType;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
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

	public Order getOrderLastMdf() {
		return orderLastMdf;
	}

	public void setOrderLastMdf(Order orderLastMdf) {
		this.orderLastMdf = orderLastMdf;
	}

}
