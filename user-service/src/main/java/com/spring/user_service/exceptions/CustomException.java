package com.spring.user_service.exceptions;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class CustomException {

    public static ResponseStatusException throwErr(int code, String message) {
        return new ResponseStatusException(HttpStatusCode.valueOf(code), message, null);
    }

}
