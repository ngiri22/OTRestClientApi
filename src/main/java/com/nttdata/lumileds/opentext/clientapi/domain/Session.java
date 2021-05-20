package com.nttdata.lumileds.opentext.clientapi.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "domain_name",
        "id",
        "local_session",
        "login_name",
        "message_digest",
        "role_name",
        "user_full_name",
        "user_id",
        "user_role_id",
        "validation_key"
})
public class Session {

    @JsonProperty("domain_name")
    private String domainName;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("local_session")
    private Boolean localSession;
    @JsonProperty("login_name")
    private String loginName;
    @JsonProperty("message_digest")
    private String messageDigest;
    @JsonProperty("role_name")
    private String roleName;
    @JsonProperty("user_full_name")
    private String userFullName;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("user_role_id")
    private Integer userRoleId;
    @JsonProperty("validation_key")
    private Integer validationKey;
}
