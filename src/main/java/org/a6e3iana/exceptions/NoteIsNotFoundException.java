package org.a6e3iana.exceptions;

import jakarta.servlet.ServletException;

public class NoteIsNotFoundException extends ServletException {
    public NoteIsNotFoundException(String message) {
        super(message);
    }
}
