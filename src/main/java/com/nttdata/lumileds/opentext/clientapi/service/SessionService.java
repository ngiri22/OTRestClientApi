package com.nttdata.lumileds.opentext.clientapi.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import com.nttdata.lumileds.opentext.clientapi.domain.Asset;
import com.nttdata.lumileds.opentext.clientapi.domain.AssetCreateRequest;
import com.nttdata.lumileds.opentext.clientapi.domain.Session;
import com.nttdata.lumileds.opentext.clientapi.domain.SessionResponse;
import com.nttdata.lumileds.opentext.clientapi.repository.OpenTextRepository;
//import com.tgt.mkt.customdam.domain.OtmmJob;
//import com.tgt.mkt.customdam.domain.otmmasset.CollectionSummary;
//import com.tgt.mkt.customdam.domain.otmmasset.OtmmAsset;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SessionService {

	@Autowired
	private OpenTextRepository openTextRepository;

//	public SessionResponse getSession() {
//		SessionResponse sessionResponse = null;
//		try {
//			sessionResponse = openTextRepository.createSession();
//
//		} finally {
//			if (sessionResponse != null) {
//				openTextRepository.deleteSession();
//			}
//		}
//		return sessionResponse;
//	}

	
	//Return the headers containing Cookie and Session ID.
	public HttpHeaders getSession() {
		
		HttpHeaders httpHeaders = null ;
		
		try {
			
			httpHeaders = openTextRepository.createSession();
		}
		finally {
			
			if (httpHeaders != null) {
				
				openTextRepository.deleteSession();
			}
			
		}
		
		return httpHeaders ;
		
	}
	
}
