package com.axonactive.agileskills.base.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
@RegisterForReflection(targets = ResourceNotFoundException.class)
@Getter
@ApplicationException
public class ResourceNotFoundException extends Exception {

    private final transient ResponseBody responseBody;

    public ResourceNotFoundException(String keyMessage, String message) {
        this.responseBody = new ResponseBody(Response.Status.NOT_FOUND, keyMessage, message);
    }
}
