package org.a6e3iana.exceptions;

import jakarta.servlet.ServletException;

public class IncorrectParametersException extends ServletException {
    public IncorrectParametersException(String message) {
        super(message);
    }
}
