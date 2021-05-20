package com.nttdata.lumileds.opentext.clientapi.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.lumileds.opentext.clientapi.domain.Asset;
import com.nttdata.lumileds.opentext.clientapi.domain.BulkAssetResultRepresentation;
import com.nttdata.lumileds.opentext.clientapi.domain.Session;
import com.nttdata.lumileds.opentext.clientapi.domain.SessionResponse;
import com.nttdata.lumileds.opentext.clientapi.util.OTMMConstants;

//import com.tgt.mkt.customdam.config.OtmmApiConfig;
//import com.tgt.mkt.customdam.domain.OtmmJob;
//import com.tgt.mkt.customdam.domain.otmmasset.OtmmAsset;
//import com.tgt.mkt.customdam.domain.otmmfolder.MediaManagerRequestObject;
//import com.tgt.mkt.customdam.domain.otmmfolder.SessionResponse;
//import com.tgt.mkt.customdam.exception.OTWrapperException;
//import com.tgt.mkt.customdam.exception.OtmmAccessException;
//import com.tgt.mkt.customdam.exception.OtmmErrors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import static com.tgt.mkt.customdam.config.OtmmConstants.API_RESPONSE_HEADER_COOKIE;
//import static com.tgt.mkt.customdam.config.OtmmConstants.ASSETS_URL;
//import static com.tgt.mkt.customdam.config.OtmmConstants.COOKIE;
//import static com.tgt.mkt.customdam.config.OtmmConstants.FOLDER_API_URL;
//import static com.tgt.mkt.customdam.config.OtmmConstants.HTTP_HEADER_CONNECTION_KEY;
//import static com.tgt.mkt.customdam.config.OtmmConstants.HTTP_HEADER_CONNECTION_VALUE;
//import static com.tgt.mkt.customdam.config.OtmmConstants.HTTP_KEEP_ALIVE;
//import static com.tgt.mkt.customdam.config.OtmmConstants.IMPORT_TEMPLATE_ID;
//import static com.tgt.mkt.customdam.config.OtmmConstants.JOB_ASSETS_URL;
//import static com.tgt.mkt.customdam.config.OtmmConstants.NO_CONTENT;
//import static com.tgt.mkt.customdam.config.OtmmConstants.OTMM_PASSWORD_HEADER;
//import static com.tgt.mkt.customdam.config.OtmmConstants.OTMM_USER_HEADER;
//import static com.tgt.mkt.customdam.config.OtmmConstants.PARENT_FOLDER_ID;
//import static com.tgt.mkt.customdam.config.OtmmConstants.RETRY_COUNT;
//import static com.tgt.mkt.customdam.config.OtmmConstants.SESSION_API_URL;
//import static com.tgt.mkt.customdam.config.OtmmConstants.TRUE_VALUE;
//import static com.tgt.mkt.customdam.config.OtmmConstants.X_REQUESTED_BY;
//import static com.tgt.mkt.customdam.config.OtmmConstants.LOCK_UNLOCK_STATE;

@Slf4j
@Component
public class OpenTextRepository {

	@Value("${application.username}")
	private String appUsername;
	
	@Value("${application.password}")
	private String appPassword;

	@Value("${application.baseURL}")
	private String appBaseURL;
	
	@Value("${application.sessionsURL}")
	private String appSessionsURL;
	
    RestTemplate otmmRestTemplate;
    
    @Getter
    private Map<String, String> sessionCookieMap = new HashMap<>();
    
    private HttpHeaders sessionHeaders = new HttpHeaders();

    private int projectCreationAttempt = 1;
    
   public OpenTextRepository(RestTemplateBuilder builder) { //, OtmmApiConfig otmmApiConfig) {
//        this.otmmConfig = otmmApiConfig;
//        otmmRestTemplate = builder.rootUri(otmmApiConfig.getOtmmBaseUrl())
//                .setConnectTimeout(otmmApiConfig.getConnectionTimeout())
//                .setReadTimeout(otmmApiConfig.getReadTimeout())
//                .build();
//        
        otmmRestTemplate = builder.rootUri
        		(appBaseURL)
        		.setConnectTimeout(10000)
        		.setReadTimeout(10000)
        		.build();
    }

    
    //Delete the asset ID that is passed.    
    public BulkAssetResultRepresentation deleteAsset (String assetId) {
    	
    	BulkAssetResultRepresentation bulkAssetResultRepresentation = null ;
    	
    	
    	return bulkAssetResultRepresentation ;
    	
    }

    @Retryable(value = {Exception.class}, backoff = @Backoff(delay = 3000))
    public HttpHeaders createSession() {

        Map<String, String> cookieMap = new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        
        SessionResponse sessionResponse = null;

        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<String, String>();

//        bodyMap.add(OTMM_USER_HEADER, otmmConfig.getOtmmApiUsername());
//        bodyMap.add(OTMM_PASSWORD_HEADER, otmmConfig.getOtmmApiPassword());
//        headers.add(HTTP_HEADER_CONNECTION_KEY, HTTP_HEADER_CONNECTION_VALUE);
//        sessionUrl = otmmConfig.getOtmmBaseUrl().concat(SESSION_API_URL);
//        
        
        bodyMap.add(OTMMConstants.username, appUsername);
        bodyMap.add(OTMMConstants.password, appPassword);
        headers.add(OTMMConstants.connection, OTMMConstants.keepAlive);
        
        int sessionId = -1;

        try {

            HttpEntity<MultiValueMap<String, String>> request = new
                    HttpEntity<MultiValueMap<String, String>>
                    (bodyMap, headers);

            ResponseEntity<String> model = otmmRestTemplate.exchange(
            		OTMMConstants.sessions, HttpMethod.POST,
                    request, String.class);

            try {
            	
            	sessionResponse = new ObjectMapper().readValue(model.getBody(),
                        SessionResponse.class);
                if (null != sessionResponse) {
                    sessionId = sessionResponse.getSessionResource().getSession().getId();
                    
                    //return sessionResponse;
                }

            } catch (IOException ex) {
                log.error("Exception while opening connection to OTMM: {}", ex);
                //throw new OtmmAccessException(OtmmErrors.OTMM_SERVICE_ERROR.getMessage());
            }

            if (model.getStatusCode().value() == HttpStatus.OK.value()) {
                List<String> cookieList = model.getHeaders().get(OTMMConstants.setCookie);
                cookieMap = cookieList.stream()
                        .map(a -> a.split("="))
                        .filter(val -> val[1].length() > 0)
                        .collect(Collectors.toMap(x -> x[0], x -> x[1]));

            }
        } catch (RestClientException | ClassCastException | UnsupportedOperationException ex) {
        	
        	log.error("Exception while creating session {} ", ex );
        	
        	//ex.printStackTrace();
        	
        	
//            log.error(OtmmErrors.OTMM_SERVICE_ERROR + OtmmErrors.OTMM_SERVICE_ERROR.getMessage(),
//                    ex);
//            throw new OtmmAccessException(OtmmErrors.OTMM_SERVICE_ERROR.getMessage());
        }
        setSessionCookieMap(cookieMap);
        setSessionHeaders(headers, sessionId);
        return getSessionHeaders();
       // return sessionResponse ;
    }


    public void setSessionCookieMap(Map<String, String> cookieMap) {
        this.sessionCookieMap = cookieMap;
    }

    public HttpHeaders getSessionHeaders() {
        return this.sessionHeaders;
    }

    public void setSessionHeaders(HttpHeaders headers, int sessionId) {
        
    	Map<String, String> cookieMap = getSessionCookieMap();

        cookieMap.forEach((cookieName, cookieValue) -> {
            headers.add("Cookie", cookieName + "=" + cookieValue);
        });

        if (sessionId != -1) {
            headers.add(OTMMConstants.xRequestedBy, String.valueOf(sessionId));
        }
        this.sessionHeaders = headers;
    }

    //@Retryable(value = {Exception.class}, backoff = @Backoff(delay = 3000))
    public Boolean deleteSession() {

        HttpHeaders requestheaders = new HttpHeaders();
        ResponseEntity<String> response = null;
        Boolean isSessionDeletionSucc = false;

        try {
            setSessionHeaders(requestheaders, -1);
            
            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new
                    HttpEntity<LinkedMultiValueMap<String, Object>>(null, requestheaders);
            
            response = otmmRestTemplate.exchange(appSessionsURL, HttpMethod.DELETE, requestEntity, String
                    .class);

        } catch (RestClientException | ClassCastException | UnsupportedOperationException ex) {
        	
            log.error("Exception while deleting session : ", ex);
//            log.error(OtmmErrors.OTMM_SERVICE_ERROR + OtmmErrors.OTMM_SERVICE_ERROR.getMessage(),
//                    ex);
//            throw new OtmmAccessException(OtmmErrors.OTMM_SERVICE_ERROR.getMessage());
        }


        if (response != null && response.getStatusCode() == HttpStatus.NO_CONTENT) {

            isSessionDeletionSucc = true;
            log.info("OTMM session deleted successfully");
        } else {

            isSessionDeletionSucc = false;
            log.error("OTMM session deletion failed ");
            if (response != null) {
                log.error("Error code : " + response.getStatusCodeValue());
            }
        }

        return isSessionDeletionSucc;
    }
    
    public Asset fetchAsset(String assetId, Session session) {
    	 return null;
    	
    }


//    @Retryable(value = {Exception.class}, backoff = @Backoff(delay = 3000))
//    public String createFolder(HttpHeaders headers, String parentFolderId, String
//            projectJsonRequest) throws OtmmAccessException {
//
//        String otmmFolderRestApiUrl = otmmConfig.getOtmmBaseUrl().concat(FOLDER_API_URL).concat
//                (parentFolderId);
//        System.setProperty(HTTP_KEEP_ALIVE, TRUE_VALUE);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Object> requestEntity = new HttpEntity<Object>(projectJsonRequest, headers);
//        ResponseEntity<String> projectApiResponse = null;
//
//        String createdProjectId = null;
//
//        if (otmmFolderRestApiUrl.isEmpty()) {
//            throw new OtmmAccessException("Error in creating otmmFolderRestApiUrl");
//        }
//
//        try {
//            projectApiResponse = otmmRestTemplate.exchange(otmmFolderRestApiUrl, HttpMethod.POST,
//                    requestEntity, String.class);
//            log.debug("otmmFolderRestApiUrl response:{}", projectApiResponse.getBody());
//            log.info("projectApiResponse.getStatusCode.value:{}", projectApiResponse
//                    .getStatusCode().value());
//
//        } catch (RestClientException  e) {
//            log.error("Exception while creating project {}", e);
//            throw new OtmmAccessException("Exception while creating project");
//        }
//
//        if (null != projectApiResponse) {
//            ObjectMapper mapper = new ObjectMapper();
//            try {
//                MediaManagerRequestObject responseObj = mapper.readValue(projectApiResponse
//                        .getBody(), MediaManagerRequestObject.class);
//                createdProjectId = responseObj.getFolderResource().getFolder().getAssetId();
//            } catch (IOException e) {
//                log.error("Exception while parsing OTMM response : {}", e.getMessage());
//                throw new OtmmAccessException("Exception while parsing OTMM response");
//            }
//        }
//
//        return createdProjectId;
//    }
//
//
    @Retryable(value = {Exception.class}, backoff = @Backoff(delay = 3000))
    public boolean lockUnlockAsset(HttpHeaders headers, String assetUoid,
                                    String state)
    //        throws OTWrapperException 
    {

    	String assetsStateUrl = OTMMConstants.assets + 
    			"/" + assetUoid + 
    			OTMMConstants.state ; 
    	
//        String folderLockUnlockURL = 
//        		otmmConfig.getOtmmBaseUrl().concat(FOLDER_API_URL)
//                .concat(folderUoid).concat(LOCK_UNLOCK_STATE);
//        
        
    	headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        log.info("Set asset {} state to {}", assetUoid, state);

        LinkedMultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
        
        param.add("action", state);
        
        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity =
                new HttpEntity<LinkedMultiValueMap<String, Object>>(param, headers);

        try {
            ResponseEntity<String> assetApiResponse = otmmRestTemplate.exchange
                    (assetsStateUrl, HttpMethod.PUT, requestEntity, String.class);
            if (assetApiResponse.getStatusCode() == HttpStatus.OK) {
                return true;
            } else {
                log.error("Error while setting asset: {} state to {}",
                		assetUoid, state);
                log.error("Asset lock API response : {}", assetApiResponse.getStatusCode());
            }

        } catch (RestClientException  e) {
            log.error("Exception while while setting project: {} state to :{}",
                    assetUoid, state, e);
        }

        return false;
    }
//
//    @Retryable(value = {Exception.class}, backoff = @Backoff(delay = 3000))
//    public boolean updateProjectMetadata(HttpHeaders headers, String folderUoid,
//                                         String projectUpdateJsonRequest)
//            throws OTWrapperException {
//
//        String folderUpdateURL = otmmConfig.getOtmmBaseUrl().concat(FOLDER_API_URL)
//                .concat(folderUoid);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Object> requestEntity = new HttpEntity<Object>(projectUpdateJsonRequest,
//                headers);
//        log.info("Project update json request {}", projectUpdateJsonRequest);
//
//        try {
//            ResponseEntity<String> projectApiResponse = otmmRestTemplate.exchange
//                    (folderUpdateURL, HttpMethod.PATCH, requestEntity, String.class);
//            if (projectApiResponse.getStatusCode() == HttpStatus.OK) {
//                return true;
//            } else {
//                log.error("Error while updating otmm project metadata. {}",
//                        projectApiResponse.getBody());
//            }
//
//        } catch (RestClientException  e) {
//            log.error("Exception while updating project metadata. project id : {}", folderUoid, e);
//        }
//
//        return false;
//    }
//
//    public OtmmJob createMetadataOnlyAsset(HttpHeaders headers, String folderId) throws
//            OTWrapperException {
//
//        String assetsURL = otmmConfig.getOtmmBaseUrl().concat(ASSETS_URL);
//
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
//        requestBody.add(NO_CONTENT, TRUE_VALUE);
//        requestBody.add(PARENT_FOLDER_ID, folderId);
//        requestBody.add(IMPORT_TEMPLATE_ID, otmmConfig.getImportTemplateId());
//        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);
//
//        OtmmJob job;
//
//        try {
//            job = otmmRestTemplate.postForObject(assetsURL, requestEntity, OtmmJob.class);
//        } catch (HttpClientErrorException e) {
//
//            log.error("POST otmmCreateAssetsRestApiURL Error");
//            throw new OTWrapperException("POST otmmCreateAssetsRestApiURL Error");
//
//        } catch (HttpServerErrorException hse) {
//            log.error("Exception while creating asset in OTMM : {}", hse.getMessage());
//            throw new OTWrapperException("POST otmmCreateAssetsRestApiURL Error");
//        }
//
//        return job;
//    }
//
//    public OtmmAsset getJobAssets(HttpHeaders headers, String jobId) throws OTWrapperException,
//            InterruptedException {
//        String jobAssetsURL = otmmConfig.getOtmmBaseUrl().concat(String.format(JOB_ASSETS_URL,
//                jobId));
//
//        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
//        OtmmAsset jobAssets;
//        try {
//            int attempt = 1;
//            int assetCount;
//            do {
//                ResponseEntity<OtmmAsset> responseEntity = otmmRestTemplate.exchange
//                        (jobAssetsURL, HttpMethod.GET, httpEntity, OtmmAsset.class);
//                jobAssets = responseEntity.getBody();
//                assetCount = jobAssets.getAssetsResource().getCollectionSummary()
//                        .getActualCountOfItems();
//                attempt++;
//                if (assetCount == 0) {
//                    Thread.sleep(2000L);
//                }
//
//            } while (attempt <= RETRY_COUNT && assetCount == 0);
//
//        } catch (HttpClientErrorException e) {
//            log.error("GET otmmGetAssetsRestApiURL Error");
//            throw new OTWrapperException("GET otmmGetAssetsRestApiURL Error");
//        } catch (HttpServerErrorException hse) {
//            log.error("Exception while fetching asset in OTMM : {}", hse.getMessage());
//            throw new OTWrapperException("GET otmmGetAssetsRestApiURL Error");
//        }
//
//        return jobAssets;
//    }
}

