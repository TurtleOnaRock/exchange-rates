package org.a6e3iana.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
public class Currency {

    private int id;
    private String code;
    private String fullName;
    private String sign;

    public Currency (int id, String code, String fullName, String sign){
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
