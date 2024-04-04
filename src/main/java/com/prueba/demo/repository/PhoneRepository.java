package com.prueba.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prueba.demo.core.entity.Phone;


 
public interface PhoneRepository extends JpaRepository<Phone, String>{

    List<Phone> findByIdUser(String idUser);
}