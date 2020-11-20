package com.merchantvessel.core.intf.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.business.enumeration.EOrderType;
import com.merchantvessel.core.business.enumeration.EPrcStatus;
import com.merchantvessel.core.persistence.model.Obj;
import com.merchantvessel.core.persistence.model.Order;

public class DtoOrder {

	private Long id;

	private double qty;

	private EOrderType orderType;

	private EPrcStatus prcStatus;

	private String advText;

	private Long assetId;

	private String assetName;

	private Date timestampCreate;

	private Date timestampModified;

	private Long objUserId;
	private String objUserName;

	private String orderTypeName;

	private EBusinessType businessType;

	private String businessTypeName;

	private String prcStatusName;

	private LocalDateTime valueDate;

	private Obj obj;

	private String objName;

	private Date objCloseDate;

	public DtoOrder(Order order) {
		this.id = order.getId();
		this.orderType = order.getOrderType();
		this.orderTypeName = order.getOrderType().getName();
		this.businessType = order.getBusinessType();
		this.businessTypeName = order.getBusinessType().getName();
		this.prcStatus = order.getPrcStatus();
		this.prcStatusName = order.getPrcStatus().getName();
		this.advText = order.getAdvText();
		this.timestampCreate = order.getTimestampCreate();
		this.timestampModified = order.getTimestampModified();
		this.valueDate = order.getValueDate();
		this.obj = order.getObj();
		this.objUserId = order.getObjUser().getId();
		this.objUserName = order.getObjUser().getName();
		this.objName = order.getObjName();
		this.objCloseDate = order.getObjCloseDate();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getQty() {
		return qty;
	}

	public void setQty(double qty) {
		this.qty = qty;
	}

	public EOrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(EOrderType orderType) {
		this.orderType = orderType;
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

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
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

	public Long getObjUserId() {
		return objUserId;
	}

	public void setObjUserId(Long objUserId) {
		this.objUserId = objUserId;
	}

	public String getObjUserName() {
		return objUserName;
	}

	public void setObjUserName(String objUserName) {
		this.objUserName = objUserName;
	}

	public String getOrderTypeName() {
		return orderTypeName;
	}

	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	public EBusinessType getBusinessType() {
		return businessType;
	}

	public void setBusinessType(EBusinessType businessType) {
		this.businessType = businessType;
	}

	public String getBusinessTypeName() {
		return businessTypeName;
	}

	public void setBusinessTypeName(String businessTypeName) {
		this.businessTypeName = businessTypeName;
	}

	public String getPrcStatusName() {
		return prcStatusName;
	}

	public void setPrcStatusName(String prcStatusName) {
		this.prcStatusName = prcStatusName;
	}

	public LocalDateTime getValueDate() {
		return valueDate;
	}

	public void setValueDate(LocalDateTime valueDate) {
		this.valueDate = valueDate;
	}

	public Obj getObj() {
		return obj;
	}

	public void setObj(Obj obj) {
		this.obj = obj;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public Date getObjCloseDate() {
		return objCloseDate;
	}

	public void setObjCloseDate(Date objCloseDate) {
		this.objCloseDate = objCloseDate;
	}

}
