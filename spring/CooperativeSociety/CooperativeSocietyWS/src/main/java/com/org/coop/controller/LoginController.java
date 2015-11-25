package com.org.coop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.org.coop.service.LoginService;

@RestController
@RequestMapping("/admin/data")
public class LoginController {
	@Autowired
	private LoginService loginService;
	
	@RequestMapping("/person")
	public void getPersonDetail(@RequestParam(value = "id",required = false,
	                                                    defaultValue = "0") Integer id) {
		String role = loginService.getRole("ashish", "ashish");
		System.out.println(role + "----->");
	}

}
