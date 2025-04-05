package org.a6e3iana.services;

import jakarta.servlet.ServletException;
import org.a6e3iana.dao.CurrencyDAO;
import org.a6e3iana.dao.CurrencyDAOImpl;
import org.a6e3iana.dao.ExchangeRateDAO;
import org.a6e3iana.dao.ExchangeRateDAOImpl;
import org.a6e3iana.dto.ExchangeRateDTO;
import org.a6e3iana.dto.ExchangeRateDTOUtils;
import org.a6e3iana.exceptions.ExceptionMessages;
import org.a6e3iana.exceptions.NoteIsNotFoundException;
import org.a6e3iana.model.Currency;
import org.a6e3iana.model.ExchangeRate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ExchangeRateService {

    public ExchangeRateDTO getExchangeRate (String baseCode, String targetCode) throws ServletException {
        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
        Optional<ExchangeRate> exchangeRateOpt = dao.findByCodes(baseCode, targetCode);
        if(exchangeRateOpt.isEmpty()){
            throw new NoteIsNotFoundException(ExceptionMessages.EXCHANGE_NOT_FOUND + baseCode + targetCode);
        }
        return ExchangeRateDTOUtils.convertModelToDto(exchangeRateOpt.get());
    }

    public List<ExchangeRateDTO> getAll () throws ServletException{
        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
        List<ExchangeRateDTO> ratesResponseDto = new ArrayList<>();
        List<ExchangeRate> rates = dao.findAll();
        if(rates.isEmpty()){
            throw new NoteIsNotFoundException(ExceptionMessages.EMPTY_DATA_BASE);
        }
        for(ExchangeRate rate : rates ){
            ratesResponseDto.add(ExchangeRateDTOUtils.convertModelToDto(rate));
        }
        return ratesResponseDto;
    }

    public ExchangeRateDTO save (ExchangeRateDTO exchangeRateRequestDto) throws ServletException{
        ExchangeRate exchangeRateRequest = getModelFromDto(exchangeRateRequestDto);
        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
        ExchangeRate exchangeRateResponse = dao.save(exchangeRateRequest);
        return ExchangeRateDTOUtils.convertModelToDto(exchangeRateResponse);
    }

    public ExchangeRateDTO update (ExchangeRateDTO exchangeRateRequestDto) throws ServletException{
        ExchangeRate exchangeRateRequest = getModelFromDto(exchangeRateRequestDto);
        ExchangeRateDAO dao = new ExchangeRateDAOImpl();
        Optional<ExchangeRate> exchangeRateOpt = dao.update(exchangeRateRequest);
        if(exchangeRateOpt.isEmpty()){
            throw new NoteIsNotFoundException(ExceptionMessages.UPDATE_FAILED);
        }
        return  ExchangeRateDTOUtils.convertModelToDto(exchangeRateOpt.get());
    }

    private ExchangeRate getModelFromDto(ExchangeRateDTO dto) throws ServletException{
        String baseCode = dto.getBaseCurrency().getCode();
        String targetCode = dto.getTargetCurrency().getCode();
        double rate = dto.getRate();
        Currency base = getCurrencyByCode(baseCode);
        Currency target = getCurrencyByCode(targetCode);
        return new ExchangeRate(base, target, rate);
    }

    private Currency getCurrencyByCode(String code) throws ServletException{
        CurrencyDAO dao = new CurrencyDAOImpl();
        Optional<Currency> currencyOpt = dao.findByCode(code);
        return currencyOpt.orElseThrow(() -> new NoteIsNotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND + code));
    }

}
