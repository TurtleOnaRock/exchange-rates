package org.a6e3iana.exceptions;

import jakarta.servlet.ServletException;
import org.a6e3iana.utils.ResponseCode;

public class ExceptionHandler {

    public static int getStatusCode(ServletException e) throws ServletException{
        String exceptionClass = e.getClass().getSimpleName();
        return switch (exceptionClass){
            case "FailedConnectionToDataBassException" -> ResponseCode.DATA_BASE_ERROR;
            case "IncorrectParametersException" -> ResponseCode.INPUT_INCORRECT;
            case "NoteAlreadyExistException" -> ResponseCode.NOTE_ALREADY_EXISTS;
            case "NoteIsNotFoundException" -> ResponseCode.NOTE_NOT_FOUND;
            default -> ResponseCode.DATA_BASE_ERROR;
        };
    }
}
