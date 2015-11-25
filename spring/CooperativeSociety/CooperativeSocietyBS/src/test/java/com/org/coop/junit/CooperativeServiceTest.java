package com.org.coop.junit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.org.coop.service.LoginService;
import com.org.coop.society.data.transaction.config.DataAppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={DataAppConfig.class})
public class CooperativeServiceTest {
	
	@Autowired
	private LoginService loginService;
	
	@Test
	public void fetchStaff() {
		String role = loginService.getRole("ashish", "ashish");
		System.out.println(role);
	}
}
