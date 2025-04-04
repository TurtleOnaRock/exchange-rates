package org.a6e3iana.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.a6e3iana.dto.MessageDTO;
import org.a6e3iana.exceptions.ExceptionHandler;

import java.io.IOException;

public class ExceptionHandlerFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (ServletException e){
            res.setStatus(ExceptionHandler.getStatusCode(e));
            req.setAttribute(Attributes.DTO, new MessageDTO(e.getMessage()));
        }
    }
}

