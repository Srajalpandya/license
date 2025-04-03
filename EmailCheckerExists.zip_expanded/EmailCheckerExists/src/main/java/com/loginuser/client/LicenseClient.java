package com.loginuser.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="LICENSE-SERVICE", url="${license.service.url}")
public interface LicenseClient{

	  @GetMapping("/validate/{licenseKey}")
	    public ResponseEntity<Boolean> validateLicense(@PathVariable String licenseKey);
	  
	  
}
