package org.a6e3iana.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    private final String message;

    public MessageDTO(String message) {
        this.message = message;
    }
}
