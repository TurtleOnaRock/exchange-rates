package org.a6e3iana.model;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Currency {

    private int id;
    private String code;
    private String fullName;
    private String sign;

    public Currency (String code, String fullName, String sign){
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
