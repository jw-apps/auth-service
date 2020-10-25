package de.johanneswirth.apps.authservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.johanneswirth.apps.common.Error;
import de.johanneswirth.apps.common.ErrorSerializer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonSerialize(using= ErrorSerializer.class)
public enum AuthError implements Error {
    USER_EXISTS("USER_EXISTS", false),
    INCORRECT_INVITATION("INCORRECT_INVITATION", false),
    ALREADY_FRIENDS("ALREADY_FRIENDS"),
    FRIENDREQUEST_EXISTS("FRIENDREQUEST_EXISTS");

    @NotEmpty
    private String errorMessage;

    @NotNull
    private boolean critical;

    AuthError(String errorMessage, boolean critical) {
        this.errorMessage = errorMessage;
        this.critical = critical;
    }

    AuthError(String errorMessage) {
        this.errorMessage = errorMessage;
        this.critical = true;
    }

    @JsonProperty("critical")
    public boolean isCritical() {
        return critical;
    }

    @JsonProperty("errorMessage")
    public String getError() {
        return errorMessage;
    }
}