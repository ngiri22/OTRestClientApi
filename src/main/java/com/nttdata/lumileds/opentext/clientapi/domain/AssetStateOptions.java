package com.nttdata.lumileds.opentext.clientapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetStateOptions {
	
	@JsonProperty("apply_to_all_versions")
	private boolean applyToAllVersions;

}
