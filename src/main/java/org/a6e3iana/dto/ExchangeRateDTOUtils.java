package org.a6e3iana.dto;

import org.a6e3iana.model.ExchangeRate;

public class ExchangeRateDTOUtils {
    public static ExchangeRateDTO convertModelToDto (ExchangeRate exRate){
        CurrencyDTO baseDTO = CurrencyDTOUtils.convertModelToDto(exRate.getBaseCurrency());
        CurrencyDTO targetDTO = CurrencyDTOUtils.convertModelToDto(exRate.getTargetCurrency());
        int id = exRate.getId();
        double rate = exRate.getRate();
        return new ExchangeRateDTO(id, baseDTO, targetDTO, rate);
    }
}
