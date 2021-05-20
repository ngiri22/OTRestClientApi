package com.nttdata.lumileds.opentext.clientapi.domain;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AssetCreateRequest {

    @NotNull(message = "file_path cannot be null")
    private String filePath;

    @NotNull(message = "user_id cannot be null")
    private String userId;

    @NotNull(message = "folder_id cannot be null")
    private String folderId;

    @NotNull(message = "file_name cannot be null")
	public String fileName;

}
