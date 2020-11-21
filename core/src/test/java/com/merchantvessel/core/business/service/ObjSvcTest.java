package com.merchantvessel.core.business.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.merchantvessel.core.business.enumeration.EDataKind;
import com.merchantvessel.core.persistence.model.Order;

@RunWith(SpringRunner.class)
@SpringBootTest
class ObjSvcTest {

	@Autowired
	private ObjSvc objSvc;

	@Test
	void testValidateObjNameWithNameExists() {
		assertEquals(objSvc.validateObjName("Object name "), true);
	}

	@Test
	void testValidateObjNameWithNameMissing() {
		assertEquals(objSvc.validateObjName(null), false);
	}

	@Test
	void testValidateObjNameWithNameEmptyString() {
		assertEquals(objSvc.validateObjName("     "), false);
	}

	/*
	 * Object close date must not be set if the order has no object
	 */
	@Test
	void testValidateObjCloseDate() {
		Order order = new Order();
		order.setDataKind(EDataKind.MASTER_DATA);
		order.setObjCloseDate(new Date());
		assertEquals(objSvc.validateObjCloseDate(order), false);
	}

}
