package com.prueba.demo.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.prueba.demo.service.DemoService;
import com.prueba.demo.support.dto.UserDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/demo")
@Api(value = "HelloWorld Resource", description = "shows hello world")
public class PruebaController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private DemoService demoService;

	@ApiOperation(value = "Register  user")
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	public ResponseEntity<Object> addUsuario(@Valid @RequestBody UserDto dto) {

		try {
			return ResponseEntity.ok(demoService.addUser(dto));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new org.springframework.http.ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
		}
	}

	@ApiOperation(value = "list User")
	@RequestMapping(value = "/getListUser", method = RequestMethod.GET)
	public ResponseEntity<Object> getListaParametro() {

		try {
			return ResponseEntity.ok(demoService.getListUser());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.ok(e);
		}
	}

	@ApiOperation(value = "Log In")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<Object> login(@RequestBody UserDto dto) {

		try {
			return ResponseEntity.ok(demoService.login(dto));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.ok(e);
		}
	}

}
