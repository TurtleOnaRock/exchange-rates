package org.a6e3iana.services;

import jakarta.servlet.ServletException;
import org.a6e3iana.dao.CurrencyDAO;
import org.a6e3iana.dao.CurrencyDAOImpl;
import org.a6e3iana.dao.ExchangeRateDAO;
import org.a6e3iana.dao.ExchangeRateDAOImpl;
import org.a6e3iana.dto.CurrencyDTO;
import org.a6e3iana.dto.CurrencyDTOUtils;
import org.a6e3iana.dto.ExchangeDTO;
import org.a6e3iana.exceptions.ExceptionMessages;
import org.a6e3iana.exceptions.NoteIsNotFoundException;
import org.a6e3iana.model.CrossExchanger;
import org.a6e3iana.model.Currency;
import org.a6e3iana.model.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class ExchangeService {

    public static final double ZERO = 0.0;
    public static final double TOO_MANY_FOR_RATE = 1000000;
    public static final int RATE_DECIMAL_PLACES = 6;
    public static final int AMOUNT_DECIMAL_SPACE = 2;

    public ExchangeDTO get (String baseCode, String targetCode, double amount) throws ServletException {
        CurrencyDAO currencyDAO = new CurrencyDAOImpl();
        Currency baseCurrency = currencyDAO.findByCode(baseCode).orElseThrow(() ->
                                    new NoteIsNotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND + baseCode));
        Currency targetCurrency = currencyDAO.findByCode(targetCode).orElseThrow(() ->
                                    new NoteIsNotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND + targetCode));
        CurrencyDTO baseCurrencyDTO = CurrencyDTOUtils.convertModelToDto(baseCurrency);
        CurrencyDTO targetCurrencyDTO = CurrencyDTOUtils.convertModelToDto(targetCurrency);
        double rate = getRate(baseCode,targetCode);
        if(rate == ZERO){
            double oppositeRate = getRate(targetCode, baseCode);
            if(oppositeRate != ZERO) {
                rate = 1.0 / oppositeRate;
            }
        }
        if (rate == ZERO){
            rate = getCrossRate(baseCurrencyDTO.getId(), targetCurrency.getId());
        }
        if (rate == ZERO) {
            throw new NoteIsNotFoundException(ExceptionMessages.EXCHANGE_NOT_FOUND + baseCode + targetCode);
        }
        rate = roundDouble(rate, RATE_DECIMAL_PLACES);
        double convertedAmount = amount / rate;
        convertedAmount = roundDouble(convertedAmount, AMOUNT_DECIMAL_SPACE);
        return new ExchangeDTO(baseCurrencyDTO, targetCurrencyDTO, rate, amount, convertedAmount);
    }

    private double getRate (String baseCode, String targetCode) throws ServletException {
        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
        Optional<ExchangeRate> exchangeRateOpt = dao.findByCode(baseCode + targetCode);
        if(exchangeRateOpt.isPresent()) {
            ExchangeRate exchangeRate = exchangeRateOpt.get();
            return exchangeRate.getRate();
        } else {
            return ZERO;
        }
    }

    private double getCrossRate (int baseId, int targetId) throws ServletException {
        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
        double bestRate = TOO_MANY_FOR_RATE;
        List<CrossExchanger> crossExchangers = dao.getCrossExchangeRates(baseId, targetId);
        if (crossExchangers.isEmpty()) {
            return ZERO;
        } else {
            for (CrossExchanger crossExchange : crossExchangers) {
                if (bestRate > crossExchange.getCrossRate()) {
                    bestRate = crossExchange.getCrossRate();
                }
            }
        }
        return bestRate;
    }

    private double roundDouble(double value, int scale){
        BigDecimal rounder = new BigDecimal(value);
        rounder = rounder.setScale(scale, RoundingMode.HALF_UP);
        return rounder.doubleValue();
    }
}
