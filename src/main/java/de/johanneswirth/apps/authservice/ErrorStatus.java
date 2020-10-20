package de.johanneswirth.apps.authservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.johanneswirth.apps.common.ErrorSerializer;
import de.johanneswirth.apps.common.IStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonSerialize(using= ErrorSerializer.class)
public enum ErrorStatus implements IStatus {
    USER_EXISTS("USER_EXISTS"),
    INCORRECT_INVITATION("INCORRECT_INVITATION"),
    ALREADY_FRIENDS("ALREADY_FRIENDS"),
    FRIENDREQUEST_EXISTS("FRIENDREQUEST_EXISTS");

    @NotEmpty
    private String errorMessage;

    @NotNull
    private boolean critical;

    ErrorStatus(String errorMessage, boolean critical) {
        this.errorMessage = errorMessage;
        this.critical = critical;
    }

    ErrorStatus(String errorMessage) {
        this.errorMessage = errorMessage;
        this.critical = true;
    }

    @Override
    @JsonProperty("error")
    public boolean isError() {
        return true;
    }

    @JsonProperty("critical")
    public boolean isCritical() {
        return critical;
    }

    @JsonProperty("errorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }
}