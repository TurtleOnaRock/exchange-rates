package org.a6e3iana.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;

    public ExchangeRate (Currency base, Currency target, double rate){
        this.baseCurrency = base;
        this.targetCurrency = target;
        this.rate = rate;
    }
}
