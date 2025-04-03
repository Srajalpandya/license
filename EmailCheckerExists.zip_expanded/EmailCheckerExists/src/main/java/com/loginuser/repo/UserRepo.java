package com.loginuser.repo;

import java.lang.foreign.Linker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loginuser.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer>{


	Users findByUsername(String username);
	
	Optional<Users> findByusername(String username);

	//Users findbyUsername(String username);

}
