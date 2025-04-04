package org.a6e3iana.dao;

import jakarta.servlet.ServletException;
import org.a6e3iana.exceptions.ExceptionMessages;
import org.a6e3iana.exceptions.FailedConnectionToDataBaseException;
import org.a6e3iana.exceptions.NoteAlreadyExistException;
import org.a6e3iana.model.CrossExchanger;
import org.a6e3iana.model.Currency;
import org.a6e3iana.model.ExchangeRate;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateDAO  {
     Optional<ExchangeRate> findByCode(String codes) throws ServletException;
     List<ExchangeRate> getAll() throws ServletException;
     ExchangeRate save(ExchangeRate exchangeRate) throws ServletException;
     Optional<ExchangeRate> update(ExchangeRate exchangeRateRequest) throws ServletException;
     List<CrossExchanger> getCrossExchangeRates (int baseCurrencyId, int targetCurrencyId) throws ServletException;
}
