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
public class AssetStateOptionsParam {
	
	@JsonProperty("asset_state_options")
	private AssetStateOptions assetStateOptions;

}
