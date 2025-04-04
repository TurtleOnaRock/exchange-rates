package org.a6e3iana.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.a6e3iana.Config;
import org.a6e3iana.dto.CurrencyDTO;
import org.a6e3iana.dto.ExchangeRateDTO;
import org.a6e3iana.filters.Attributes;
import org.a6e3iana.services.ExchangeRateService;
import org.a6e3iana.utils.ResponseCode;
import org.a6e3iana.utils.ParameterUtils;

import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    public static final String FIELD_RATE = "rate";
    public static final String EMPTY_STRING = "";
    public static final int NO_ID = -1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codes = req.getPathInfo().replaceFirst("/", "");
        ParameterUtils.validateCurrencyCode(codes, Config.CURRENCY_CODE_LENGTH*2);
        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateDTO exRateResponseDto = service.getExchangeRate(codes);
        resp.setStatus(ResponseCode.GET_SUCCESS);
        req.setAttribute(Attributes.DTO, exRateResponseDto);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codes = req.getPathInfo().replaceFirst("/", "");
        String parameters = req.getReader().readLine();
        String rateString = this.getParameter(parameters, FIELD_RATE);
        ParameterUtils.validateParameter(FIELD_RATE, rateString, Config.RATE_LENGTH);
        ParameterUtils.validateCurrencyCode(codes, Config.CURRENCY_CODE_LENGTH*2);

        double rate = ParameterUtils.parseDouble(rateString);
        ParameterUtils.validateDouble(rate, Config.RATE_MIN, Config.RATE_MAX);

        String baseCode = codes.substring(0, 3);
        String targetCode = codes.substring(3, 6);
        CurrencyDTO baseCurrencyDto = new CurrencyDTO(NO_ID, baseCode, EMPTY_STRING, EMPTY_STRING);
        CurrencyDTO targetCurrencyDto = new CurrencyDTO(NO_ID, targetCode, EMPTY_STRING, EMPTY_STRING);
        ExchangeRateDTO exRateRequestDto = new ExchangeRateDTO(NO_ID, baseCurrencyDto, targetCurrencyDto, rate);
        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateDTO exRateResponseDto = service.update(exRateRequestDto);
        resp.setStatus(ResponseCode.PATCH_SUCCESS);
        req.setAttribute(Attributes.DTO, exRateResponseDto);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if(method.equals("PATCH")){
            this.doPatch(req, resp);
            return;
        }
        super.service(req, resp);
    }

    private String getParameter(String parameters, String parameter){
        if(parameters == null){
            return null;
        }
        if(!parameters.contains(parameter + "=")){
            return null;
        }
        if (parameters.contains("&")) {
            String[] notes  = parameters.split("&");
            for (String note : notes) {
                if (note.contains(parameter)) {
                    return note.replace(parameter + "=", "");
                }
            }
        }
        return parameters.replace(parameter + "=", "");
    }

}