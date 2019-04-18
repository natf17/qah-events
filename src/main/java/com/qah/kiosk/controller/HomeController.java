package com.qah.kiosk.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	
	
	@RequestMapping("/")
	public String sayHello(HttpServletRequest req) {
		System.out.println("I WAS CALLED");
		return "Welcome to the QAH Kiosk!" + req.getLocale();
	}
}
