package org.a6e3iana.services;

import jakarta.servlet.ServletException;
import org.a6e3iana.dao.CurrencyDAO;
import org.a6e3iana.dto.CurrencyDTO;
import org.a6e3iana.dao.CurrencyDAOImpl;
import org.a6e3iana.exceptions.ExceptionMessages;
import org.a6e3iana.exceptions.NoteIsNotFoundException;
import org.a6e3iana.model.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.a6e3iana.dto.CurrencyDTOUtils.convertDtoToModel;
import static org.a6e3iana.dto.CurrencyDTOUtils.convertModelToDto;

public class CurrencyService {

    public CurrencyService(){}

    public List<CurrencyDTO> getAll() throws ServletException {
        List<CurrencyDTO> currenciesDTO = new ArrayList<>();
        CurrencyDAO dao = new CurrencyDAOImpl();
        List<Currency> currencies = dao.findAll();
        if(currencies.isEmpty()){
            throw new NoteIsNotFoundException(ExceptionMessages.EMPTY_DATA_BASE);
        }
        for (Currency currency : currencies) {
            currenciesDTO.add(convertModelToDto(currency));
        }
        return currenciesDTO;
    }

    public CurrencyDTO getByCode (String code) throws ServletException{
        CurrencyDAO dao = new CurrencyDAOImpl();
        Optional<Currency> currencyOpt = dao.findByCode(code);
        Currency currency = currencyOpt.orElseThrow
                (() -> new NoteIsNotFoundException(ExceptionMessages.CURRENCY_NOT_FOUND + code));
        return convertModelToDto(currency);
    }

    public CurrencyDTO save (CurrencyDTO currencyRequestDto) throws ServletException {
        CurrencyDAO dao = new CurrencyDAOImpl();
        Currency currencyResponse = dao.save(convertDtoToModel(currencyRequestDto));
        return convertModelToDto(currencyResponse);
    }

}
