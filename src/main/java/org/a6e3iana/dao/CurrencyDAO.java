package org.a6e3iana.dao;

import jakarta.servlet.ServletException;
import org.a6e3iana.model.Currency;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<Currency, Integer> {

    Optional<Currency> findByCode(String code) throws ServletException;

}
