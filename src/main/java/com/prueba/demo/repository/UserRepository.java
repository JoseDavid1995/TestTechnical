package com.prueba.demo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.demo.core.entity.User;


 
public interface UserRepository extends JpaRepository<User, String>{

   Optional<User> findById(String id);
   List<User> findByEmail(String email);
}