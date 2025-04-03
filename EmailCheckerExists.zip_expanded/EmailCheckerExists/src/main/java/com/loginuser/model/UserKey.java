package com.loginuser.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserKey {
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Id
	private String email;
	
	private String apiKey;

}
