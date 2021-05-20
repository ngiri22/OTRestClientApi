package com.nttdata.lumileds.opentext.clientapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.lumileds.opentext.clientapi.domain.SessionResponse;
import com.nttdata.lumileds.opentext.clientapi.service.SessionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping ("v1/session")
@Slf4j
public class SessionController {

	@Autowired
	private SessionService sessionService ;
	
	@RequestMapping("/getSession")
	public HttpHeaders getSession() {
		
		return sessionService.getSession();
	
	}
	
}
