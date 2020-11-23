package com.merchantvessel.core.business.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.merchantvessel.core.business.enumeration.EBusinessType;
import com.merchantvessel.core.persistence.model.Log;
import com.merchantvessel.core.persistence.model.ObjUser;
import com.merchantvessel.core.persistence.model.Order;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderSvcTest {

	@Autowired
	private OrderSvc orderSvc;

	@MockBean
	private LogSvc logSvc;

	@Autowired
	private ControlSvc controlSvc;

	@Test
	public void testRepoForLogEntries() {
		when(logSvc.getRecentEntries())
				.thenReturn(Stream.of(new Log("Location", "Method")).collect(Collectors.toList()));
		assertEquals(logSvc.getRecentEntries().size(), 1);
	}

	@Test
	public void testValidateObjOrder() {
		Order order = new Order(EBusinessType.OBJ_USER, new ObjUser());
		order.setObjName("Object Name");
		assertEquals(orderSvc.validateObjOrder(order), true);
	}

}
