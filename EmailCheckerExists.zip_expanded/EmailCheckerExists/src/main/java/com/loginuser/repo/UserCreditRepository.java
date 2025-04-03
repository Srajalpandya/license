package com.loginuser.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loginuser.model.UserCredit;

public interface UserCreditRepository extends JpaRepository<UserCredit, Integer> {

	Optional<UserCredit> findByEmail(String email);

}
