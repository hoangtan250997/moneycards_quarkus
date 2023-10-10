package com.axonactive.agileskills.base.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
@RegisterForReflection(targets = AgileSkillsExceptionMapper.class)
@Getter
public class AgileSkillsException extends Exception {

    private final transient ResponseBody responseBody;

    public AgileSkillsException(String keyMessage, String message) {
        this.responseBody = new ResponseBody(Response.Status.NOT_FOUND, keyMessage, message);
    }
}
