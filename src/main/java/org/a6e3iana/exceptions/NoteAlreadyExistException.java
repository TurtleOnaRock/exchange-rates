package org.a6e3iana.exceptions;

import jakarta.servlet.ServletException;

public class NoteAlreadyExistException extends ServletException {
    public NoteAlreadyExistException(String message) {
        super(message);
    }
}
