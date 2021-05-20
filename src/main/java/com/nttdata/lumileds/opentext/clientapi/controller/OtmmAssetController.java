package com.nttdata.lumileds.opentext.clientapi.controller;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.nttdata.lumileds.opentext.clientapi.domain.Asset;
import com.nttdata.lumileds.opentext.clientapi.domain.AssetCreateRequest;
import com.nttdata.lumileds.opentext.clientapi.domain.AssetCreateResponse;
import com.nttdata.lumileds.opentext.clientapi.domain.SessionResponse;
import com.nttdata.lumileds.opentext.clientapi.service.OtmmAssetService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/assets")
@Slf4j
public class OtmmAssetController {

	@Autowired
	private OtmmAssetService otmmAssetService;

	@ApiOperation(value = "REST service to import asset into a project in OTMM",
			notes = "REST service to import asset into a project in OTMM",
			response = AssetCreateResponse.class)
	@RequestMapping(value = "/import", method = RequestMethod.POST, consumes = MediaType
	.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	
	@ResponseBody
	public AssetCreateResponse importAsset(@RequestBody @Valid AssetCreateRequest
			assetCreateRequest) {
		
		log.info("Asset create request received {}", assetCreateRequest);
		
		Asset otmmAsset = otmmAssetService.importAsset(assetCreateRequest);
        AssetCreateResponse assetCreateResponse = new AssetCreateResponse();
        BeanUtils.copyProperties(assetCreateRequest, assetCreateResponse);
        assetCreateResponse.setAssetId(otmmAsset.getAssetId());
        log.info("Asset create response {}", assetCreateResponse);
        return assetCreateResponse;

	}
	
//	@RequestMapping("/assetid/{id}")
//	public Asset getAsset(String assetId) {
//		
//		Asset otmmAsset = otmmAssetService.getAsset(assetId);
//	
//		return null;
//	}
	
	@RequestMapping("/lock")
	public void lockUnlockAsset() {
		otmmAssetService.lockUnlockAsset();
	}
	
	@RequestMapping("/session")
	public HttpHeaders getSession() {
		
		return otmmAssetService.getSession();
	
	}
	
}