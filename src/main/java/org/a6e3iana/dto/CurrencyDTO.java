package org.a6e3iana.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CurrencyDTO {
    private int id;
    private String name;
    private String code;
    private String sign;

    public CurrencyDTO (int id, String code, String fullName, String sign){
        this.id = id;
        this.code = code;
        this.name = fullName;
        this.sign = sign;
    }
}
