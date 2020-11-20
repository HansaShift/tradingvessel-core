package com.merchantvessel.core.intf.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gui")
public class GuiController {

//	@GetMapping("/menu-items/all")
//	public ResponseEntity<?> allMenuItems() {
//
//		List<JsonFormMenuItem> menuItemList = new ArrayList<JsonFormMenuItem>();
//		EBusinessType[] entitiyTypeArray = EBusinessType.values();
//
//		for (EBusinessType entityType : entitiyTypeArray) {
//			JsonFormMenuItem entityTypeJson = new JsonFormMenuItem(entityType);
//			menuItemList.add(entityTypeJson);
//		}
//		return ResponseEntity.ok(menuItemList);
//	}
}