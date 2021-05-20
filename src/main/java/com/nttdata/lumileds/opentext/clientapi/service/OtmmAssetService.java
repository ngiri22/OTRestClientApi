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
public class OtmmAssetService {

	@Autowired
	private OpenTextRepository openTextRepository;

	public Asset importAsset(@Valid AssetCreateRequest assetCreateRequest) {

		log.info("Asset create request received for filename {} into folder {}",
				assetCreateRequest.getFileName(), assetCreateRequest.getFolderId());

		HttpHeaders otmmSession = null;
		Asset jobAssets = null;
		try {
			//otmmSession = openTextRepository.createSession();
			//            OtmmJob otmmJob = openTextRepository.createMetadataOnlyAsset(otmmSession,
			//                    assetCreateRequest.getFolderId());
			//            String jobId = otmmJob.getJobHandle().getJobId();
			//            if (!StringUtils.isEmpty(jobId)) {
			//                log.info("Job ID for metadata only asset {} : {}", assetCreateRequest.getFileName(),
			//                        jobId);
			//                jobAssets = openTextRepository.getJobAssets(otmmSession, jobId);
			//                log.info("Job Assets for job ID {} : {}", jobId, jobAssets);
			//                String assetId = null;
			//                CollectionSummary collectionSummary = jobAssets.getAssetsResource()
			//                        .getCollectionSummary();
			//                List<Asset> assetList = jobAssets.getAssetsResource().getAssetList();
			//                if (collectionSummary.getActualCountOfItems() > 0 && !CollectionUtils.isEmpty
			//                        (assetList)) {
			//                    assetId = assetList.get(0).getAssetId();
			//                }
			//                if (!StringUtils.isEmpty(assetId)) {
			//                    log.info("Attaching content to asset {}", assetId);
			//                    attachContent(assetId, assetCreateRequest);
			//                    log.info("Updating metadata for asset {}", assetId);
			//                    saveMetadata(assetId, assetCreateRequest);
			//                    log.info("Metadata update completed for {}", assetId);
			//                } else {
			//                    log.error("Asset not generated in OTMM for {} in folder {}", assetCreateRequest
			//                            .getFileName(), assetCreateRequest.getFolderId());
			//                    jobAssets = null;
			//                }
			//            } else {
			//                log.error("Asset not generated in OTMM for {} in folder {}", assetCreateRequest
			//                        .getFileName(), assetCreateRequest.getFolderId());
			//                jobAssets = null;
			//            }
		} finally {
			if (otmmSession != null) {
				openTextRepository.deleteSession();
			}
		}
		return jobAssets;
	}

	//	public Asset getAsset(String assetId) {
	//		HttpHeaders otmmSession = null;
	//        Asset jobAssets = null;
	//        try {
	//            otmmSession = openTextRepository.createSession();
	//        
	//	 } finally {
	//         if (otmmSession != null) {
	//             openTextRepository.deleteSession();
	//         }
	//     }
	//     return jobAssets;
	//	}

	public HttpHeaders getSession() {

		HttpHeaders sessionHeaders = null ;

		try {

			sessionHeaders = openTextRepository.createSession();
		}
		finally {

			if (sessionHeaders != null) {

				openTextRepository.deleteSession();
			}

		}

		return sessionHeaders ;

	}

	public void lockUnlockAsset() {

		HttpHeaders sessionHeaders = null;

		try {

			sessionHeaders = openTextRepository.createSession();

			openTextRepository.lockUnlockAsset(
					sessionHeaders, 
					"7656cf4ebffe0c676b0872c68ef0feb58c18586b",
					"lock"
					);
		}
		catch (Exception ex) {
			log.error("Exception while locking the asset: {}", ex);
		}
		finally {

			if (sessionHeaders != null) {

				openTextRepository.deleteSession();
			}

		}
	}

}
