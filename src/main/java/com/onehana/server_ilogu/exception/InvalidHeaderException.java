package com.onehana.server_ilogu.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidHeaderException extends AuthenticationException {
    public InvalidHeaderException(String msg) {
        super(msg);
    }
}
