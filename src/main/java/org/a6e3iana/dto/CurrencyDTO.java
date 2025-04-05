package org.a6e3iana.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {
    private int id;
    private String name;
    private String code;
    private String sign;

    public CurrencyDTO (String code, String fullName, String sign){
        this.code = code;
        this.name = fullName;
        this.sign = sign;
    }

    public CurrencyDTO (String code){
        this.code = code;
    }
}
