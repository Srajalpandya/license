package com.loginuser.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loginuser.client.EmailClient;
import com.loginuser.client.LicenseClient;
import com.loginuser.model.JwtResponse;
import com.loginuser.model.UserCredit;
import com.loginuser.model.Users;
import com.loginuser.repo.UserCreditRepository;
import com.loginuser.service.UserService;

@RestController
@RequestMapping("/auth")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@Autowired
	private LicenseClient client;
	@Autowired
	private EmailClient emailClient;
	@Autowired
	private UserCreditRepository userCreditRepository;

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Users user) {
		return service.register(user);
	}
	
	@PostMapping("/login")
	public JwtResponse Login(@RequestBody Users user) {
		
		return service.verify(user);
	}
	@GetMapping("/license-Check/{email}/{licenseKey}")
	public boolean license(@PathVariable String email, @PathVariable String licenseKey) {
	    Boolean emailCheck = false;

	    // Validate the license using client
	    ResponseEntity<Boolean> licenseCheckResponse = client.validateLicense(licenseKey);

	    if (licenseCheckResponse != null && licenseCheckResponse.getStatusCode().is2xxSuccessful()) {
	        Boolean licenseCheck = licenseCheckResponse.getBody();

	        if (licenseCheck != null && licenseCheck) {
	            Optional<UserCredit> existingUser = userCreditRepository.findByEmail(email);

	            if (existingUser.isPresent()) {
	                UserCredit userCredit = existingUser.get();
	                // Increment credit by 1
	                int currentCredit = userCredit.getCredit();
	                if (currentCredit >= 100) {
	                	System.out.println("current credit "+currentCredit);
	                    return false; // If credit exceeds 100, return false
	                }
	                userCredit.setCredit(currentCredit + 1); // Increment credit
	                userCreditRepository.save(userCredit); // Save updated credit
	            } else {
	                // Add new email if not exists with initial credit of 1
	                UserCredit newUserCredit = new UserCredit();
	                newUserCredit.setEmail(email);
	                newUserCredit.setCredit(1); // Initial credit
	                userCreditRepository.save(newUserCredit);
	            }
	            // Verify the email using emailClient after handling credit
	            emailCheck = emailClient.verifyEmail(email);
	        }
	    }

	    return emailCheck;
	}
	@GetMapping("/email-Check/{email}")
	public  boolean EmaiExists(@PathVariable String email) {
		
		Boolean EmailCheck=false;
		 EmailCheck=emailClient.verifyEmail(email);
		
		return EmailCheck;
		
	}
}
