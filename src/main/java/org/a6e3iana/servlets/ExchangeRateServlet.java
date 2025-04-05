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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String codes = req.getPathInfo().replaceFirst("/", "");
        ParameterUtils.validateCurrencyCode(codes, Config.CURRENCY_CODE_LENGTH*2);

        String baseCode = codes.substring(0,3);
        String targetCode = codes.substring(3,6);

        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateDTO exchangeRateResponseDto = service.getExchangeRate(baseCode, targetCode);
        resp.setStatus(ResponseCode.GET_SUCCESS);
        req.setAttribute(Attributes.DTO, exchangeRateResponseDto);
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
        CurrencyDTO baseCurrencyDto = new CurrencyDTO(baseCode);
        CurrencyDTO targetCurrencyDto = new CurrencyDTO(targetCode);
        ExchangeRateDTO exchangeRateRequestDto = new ExchangeRateDTO(baseCurrencyDto, targetCurrencyDto, rate);

        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateDTO exchangeRateResponseDto = service.update(exchangeRateRequestDto);
        resp.setStatus(ResponseCode.PATCH_SUCCESS);
        req.setAttribute(Attributes.DTO, exchangeRateResponseDto);
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