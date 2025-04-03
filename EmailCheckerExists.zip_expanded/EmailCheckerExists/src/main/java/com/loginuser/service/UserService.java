package com.loginuser.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.loginuser.client.EmailClient;
import com.loginuser.model.JwtResponse;
import com.loginuser.model.UserKey;
import com.loginuser.model.Users;
import com.loginuser.repo.UserKeyRepo;
import com.loginuser.repo.UserRepo;

@Service
public class UserService {
	@Autowired
	private UserRepo repo;
	
	@Autowired
	private UserKeyRepo userKeyRepo;
	@Autowired
	AuthenticationManager authManager;

	
	@Autowired
	private EmailClient client;

	@Autowired
	private JwtService jwtService;
	 private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);
	 
	 public ResponseEntity<String> register(Users user) {
		    // First, check if the email is verified using your verification logic.
		    boolean emailVerified = client.verifyEmail(user.getUsername());
		    // Next, check if a user already exists with that email.
		    Optional<Users> existingUser = repo.findByusername(user.getUsername());
		    
		    UserKey userKey=new UserKey();
		    
		    if (!existingUser.isPresent() && emailVerified) {
		    	userKey.setApiKey(UUID.randomUUID().toString());
		    	userKey.setEmail(user.getUsername());
		    	userKeyRepo.save(userKey);
		        // Both conditions are met: the email is verified and is not in the database yet.
		        user.setPassword(encoder.encode(user.getPassword()));
		        repo.save(user);
		        return ResponseEntity.ok("user register success");
		    } else if (existingUser.isPresent()) {
		        // The email already exists in your system.
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user already register");
		    } else if (!emailVerified) {
		        // The provided email did not pass verification.
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("email does not exists");
		    }
		    
		    // This fallback should not be reached, but it's here as a safety net.
		    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed due to an unknown reason.");
		}


	public JwtResponse verify(Users user) {
		Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		String token="";
		if(authentication.isAuthenticated()) 
			 token=jwtService.generateToken(user.getUsername());
			return JwtResponse.builder().jwtToken(token).username(user.getUsername()).build();
		
	}
}
