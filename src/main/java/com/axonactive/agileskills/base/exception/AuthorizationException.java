package com.axonactive.agileskills.base.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
@RegisterForReflection(targets = AuthorizationException.class)
@Getter
@ApplicationException
public class AuthorizationException extends Exception {

    private final transient ResponseBody responseBody;

    public AuthorizationException(Response.Status status, String keyMessage, String message) {
        this.responseBody = new ResponseBody(status, keyMessage, message);
    }
}
