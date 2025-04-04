package org.a6e3iana.dao;

import jakarta.servlet.ServletException;
import org.a6e3iana.model.Currency;

import java.util.List;
import java.util.Optional;

public interface CurrencyDAO {
    List<Currency> getAll() throws ServletException;
    Optional<Currency> findByCode(String code) throws ServletException;
    Optional<Currency> findById(int id) throws ServletException;
    Currency save(Currency currency) throws ServletException;
    Optional<Currency> update(Currency currency) throws ServletException;
}
