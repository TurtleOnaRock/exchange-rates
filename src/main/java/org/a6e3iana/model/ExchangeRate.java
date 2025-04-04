package org.a6e3iana.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ExchangeRate {
    private int id;
    private Currency baseCurrency;
    private Currency targetCurrency;
    private double rate;

    public ExchangeRate (int id, Currency base, Currency target, double rate){
        this.id = id;
        this.baseCurrency = base;
        this.targetCurrency = target;
        this.rate = rate;
    }
}
