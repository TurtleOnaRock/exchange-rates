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

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_SIGN = "sign";

    public static final int NAME_LENGTH = 50;
    public static final int SIGN_LENGTH = 4;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyService service = new CurrencyService();
        resp.setStatus(ResponseCode.GET_SUCCESS);
        req.setAttribute(Attributes.DTO, service.getAll());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter(FIELD_NAME);
        String code = req.getParameter(FIELD_CODE);
        String sign = req.getParameter(FIELD_SIGN);
        ParameterUtils.validateCurrencyCode(code, Config.CURRENCY_CODE_LENGTH);
        ParameterUtils.validateParameter(FIELD_NAME, name, NAME_LENGTH);
        ParameterUtils.validateParameter(FIELD_SIGN, sign, SIGN_LENGTH);
        CurrencyDTO currencyRequestDto = makeCurrencyDto(name, code, sign);
        CurrencyService service = new CurrencyService();
        CurrencyDTO currencyResponseDto = service.save(currencyRequestDto);
        resp.setStatus(ResponseCode.GET_SUCCESS);
        req.setAttribute(Attributes.DTO, currencyResponseDto);
    }

    private CurrencyDTO makeCurrencyDto(String name, String code, String sign){
        CurrencyDTO currencyDto = new CurrencyDTO();
        currencyDto.setName(name);
        currencyDto.setCode(code);
        currencyDto.setSign(sign);
        currencyDto.setId(-1);
        return currencyDto;
    }
}
