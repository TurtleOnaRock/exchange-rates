package org.a6e3iana.exceptions;

import jakarta.servlet.ServletException;

public class FailedConnectionToDataBaseException extends ServletException{
    public FailedConnectionToDataBaseException(String message) {
        super(message);
    }
}
