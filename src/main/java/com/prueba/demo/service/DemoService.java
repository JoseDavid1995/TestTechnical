package com.prueba.demo.service;
 
import com.prueba.demo.support.dto.Answer;
import com.prueba.demo.support.dto.UserDto;
 

public interface DemoService {

	Answer<?> addUser(UserDto dto) throws Exception;
	Answer<?> updateUser(String idUser, UserDto body) throws Exception;
	Answer<?> getListUser() throws Exception;
	Answer<?> login(UserDto dto) throws Exception;
}
