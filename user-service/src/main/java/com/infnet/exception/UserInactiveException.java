package com.infnet.exception;

public class UserInactiveException extends RuntimeException{
    public UserInactiveException() {
        super("User is Inactive");
    }

}
