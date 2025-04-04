package org.a6e3iana.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.a6e3iana.Config;
import org.a6e3iana.filters.Attributes;
import org.a6e3iana.services.CurrencyService;
import org.a6e3iana.dto.CurrencyDTO;
import org.a6e3iana.utils.ResponseCode;
import org.a6e3iana.utils.ParameterUtils;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getPathInfo().replaceFirst("/", "");
        ParameterUtils.validateCurrencyCode(code, Config.CURRENCY_CODE_LENGTH);

        CurrencyService service = new CurrencyService();
        CurrencyDTO currencyResponseDto = service.getByCode(code);

        resp.setStatus(ResponseCode.GET_SUCCESS);
        req.setAttribute(Attributes.DTO, currencyResponseDto);
    }

}

