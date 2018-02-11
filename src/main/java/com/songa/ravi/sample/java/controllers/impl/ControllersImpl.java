package com.songa.ravi.sample.java.controllers.impl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.songa.ravi.sample.java.controllers.IControllers;

@RestController
@RequestMapping("/v1/api/")
public class ControllersImpl implements IControllers {

	@GetMapping("/ping")
	public String pingpong() {
		return "pong";
	}
}
