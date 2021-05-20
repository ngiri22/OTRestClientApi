package com.nttdata.lumileds.opentext.clientapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkAssetResultRepresentation {

	private BulkAssetResult bulkAssetResult ;
}
