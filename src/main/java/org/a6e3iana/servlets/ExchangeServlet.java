package org.a6e3iana.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.a6e3iana.Config;
import org.a6e3iana.dto.ExchangeDTO;
import org.a6e3iana.filters.Attributes;
import org.a6e3iana.services.ExchangeService;
import org.a6e3iana.utils.ParameterUtils;
import org.a6e3iana.utils.ResponseCode;

import java.io.IOException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    public static final String FIELD_FROM = "from";
    public static final String FIELD_TO = "to";
    public static final String FIELD_AMOUNT = "amount";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter(FIELD_FROM);
        String to = req.getParameter(FIELD_TO);
        String amountStr = req.getParameter(FIELD_AMOUNT);

        ParameterUtils.validateCurrencyCode(from, Config.CURRENCY_CODE_LENGTH);
        ParameterUtils.validateCurrencyCode(to, Config.CURRENCY_CODE_LENGTH);
        ParameterUtils.validateParameter(FIELD_AMOUNT, amountStr, Config.AMOUNT_LENGTH);

        double amount = ParameterUtils.parseDouble(amountStr);
        ParameterUtils.validateDouble(amount, Config.AMOUNT_MIN, Config.AMOUNT_MAX);

        ExchangeService service = new ExchangeService();
        ExchangeDTO exchangeResponseDTO = service.get(from, to, amount);
        resp.setStatus(ResponseCode.GET_SUCCESS);
        req.setAttribute(Attributes.DTO, exchangeResponseDTO);
    }
}
