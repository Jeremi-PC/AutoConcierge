package com.example.auto_concierge.exception;


import javax.naming.AuthenticationException;

public class IncorrectAuthenticationException extends AuthenticationException {
    public IncorrectAuthenticationException(String msg) {
        super(msg);
    }
}