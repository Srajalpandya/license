package com.loginuser.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginuser.model.UserKey;

public interface UserKeyRepo extends JpaRepository<UserKey, String> {

}
