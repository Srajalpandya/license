package com.loginuser.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="EMAIL-SERVICE", url="${email.service.url}")
public interface EmailClient {
	
	@GetMapping("/mail-cheker/{email}")
	public  boolean verifyEmail(@PathVariable String email) ;

}
