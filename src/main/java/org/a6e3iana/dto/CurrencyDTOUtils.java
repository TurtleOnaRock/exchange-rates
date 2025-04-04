package org.a6e3iana.dto;

import org.a6e3iana.model.Currency;

public class CurrencyDTOUtils {
    public static CurrencyDTO convertModelToDto(Currency currency){
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setId(currency.getId());
        currencyDTO.setName(currency.getFullName());
        currencyDTO.setCode(currency.getCode());
        currencyDTO.setSign(currency.getSign());
        return currencyDTO;
    }

    public static Currency convertDtoToModel(CurrencyDTO currencyDTO){
        Currency currency = new Currency();
        currency.setId(currencyDTO.getId());
        currency.setFullName(currencyDTO.getName());
        currency.setCode(currencyDTO.getCode());
        currency.setSign(currencyDTO.getSign());
        return currency;
    }
}
