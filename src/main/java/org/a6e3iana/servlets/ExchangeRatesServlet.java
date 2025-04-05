package org.a6e3iana.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.a6e3iana.Config;
import org.a6e3iana.dto.CurrencyDTO;
import org.a6e3iana.filters.Attributes;
import org.a6e3iana.services.ExchangeRateService;
import org.a6e3iana.dto.ExchangeRateDTO;
import org.a6e3iana.utils.ResponseCode;
import org.a6e3iana.utils.ParameterUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {

    public static final String FIELD_BASE_CODE = "baseCurrencyCode";
    public static final String FIELD_TARGET_CODE = "targetCurrencyCode";
    public static final String FIELD_RATE = "rate";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateService service = new ExchangeRateService();
        List<ExchangeRateDTO> responseDto = service.getAll();
        resp.setStatus(ResponseCode.GET_SUCCESS);
        req.setAttribute(Attributes.DTO, responseDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCode = req.getParameter(FIELD_BASE_CODE);
        String targetCode = req.getParameter(FIELD_TARGET_CODE);
        String rateString = req.getParameter(FIELD_RATE);

        ParameterUtils.validateCurrencyCode(baseCode, Config.CURRENCY_CODE_LENGTH);
        ParameterUtils.validateCurrencyCode(targetCode, Config.CURRENCY_CODE_LENGTH);
        ParameterUtils.validateParameter(FIELD_RATE, rateString, Config.RATE_LENGTH);

        double rate = ParameterUtils.parseDouble(rateString);
        ParameterUtils.validateDouble(rate, Config.RATE_MIN, Config.RATE_MAX);

        CurrencyDTO base = new CurrencyDTO(baseCode);
        CurrencyDTO target = new CurrencyDTO(targetCode);
        ExchangeRateDTO exchangeRateRequestDto = new ExchangeRateDTO(base, target, rate);
        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateDTO exchangeRateResponseDto = service.save(exchangeRateRequestDto);

        resp.setStatus(ResponseCode.POST_SUCCESS);
        req.setAttribute(Attributes.DTO, exchangeRateResponseDto);
    }
}
