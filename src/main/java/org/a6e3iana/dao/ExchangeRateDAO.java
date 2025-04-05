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

public interface ExchangeRateDAO extends CrudDAO<ExchangeRate, Integer> {

     List<CrossExchanger> getCrossExchangeRates (int baseCurrencyId, int targetCurrencyId) throws ServletException;

     Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) throws ServletException;

}
