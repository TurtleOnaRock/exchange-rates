package org.a6e3iana.dao;

import jakarta.servlet.ServletException;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T, ID> {

    T save (T t) throws ServletException;

    Optional<T> findById(ID id) throws ServletException ;

    List<T> findAll () throws ServletException;

    Optional<T> update (T t) throws ServletException;

    void delete(ID id) throws ServletException;
}
