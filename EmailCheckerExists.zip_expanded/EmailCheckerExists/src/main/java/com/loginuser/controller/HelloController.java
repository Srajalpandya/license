package com.loginuser.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

	@GetMapping("/")
	public String reet(HttpServletRequest request) {
		return "welcome to shubham " +request.getSession().getId();
	}
}
