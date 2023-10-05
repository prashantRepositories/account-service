package com.acountservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.acountservice.model.User;

@Repository
public interface AccountServiceRepository extends JpaRepository<User, String>{

	User findByUserEmail(String username);

}
