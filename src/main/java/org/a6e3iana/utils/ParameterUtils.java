package org.a6e3iana.utils;

import org.a6e3iana.exceptions.IncorrectParametersException;

public class ParameterUtils {
    public static final String CURRENCY_CODE_FORMAT_WRONG = "Currency code should consist of %d characters. Wrong currency code is %s";
    public static final String CURRENCY_CODE_REQUIRED = "Currency code(s) required.";
    public static final String PARAMETER_REQUIRED = "Empty parameter: ";
    public static final String PARAMETER_FORMAT_WRONG = " is the max length for parameter: ";
    public static final String TOO_SMALL_VALUE = "Value might be higher then ";
    public static final String TOO_HIGH_VALUE = "Value might be less then ";
    public static final String NUMBER_FORMAT_WRONG = "Wrong number format: ";


    public static void validateCurrencyCode(String code, int codeLength) throws IncorrectParametersException {
        if (code == null || code.isEmpty()) {
            throw new IncorrectParametersException(CURRENCY_CODE_REQUIRED);
        }
        if (code.length() != codeLength) {
            throw new IncorrectParametersException(String.format(CURRENCY_CODE_FORMAT_WRONG, codeLength, code));
        }
    }

    public static void validateParameter(String name, String value, int maxLength) throws IncorrectParametersException {
        if (value == null || value.isEmpty()) {
            throw new IncorrectParametersException(PARAMETER_REQUIRED + name);
        }
        if (value.length() > maxLength) {
            throw new IncorrectParametersException(maxLength + PARAMETER_FORMAT_WRONG + name);
        }
    }

    public static double parseDouble(String number) throws IncorrectParametersException {
        double value;
        try{
            value = Double.parseDouble(number);
        } catch (NumberFormatException e){
            throw new IncorrectParametersException(NUMBER_FORMAT_WRONG);
        }
        return value;
    }

    public static void validateDouble(double value, double minValue, double maxValue) throws IncorrectParametersException{
        if(value < minValue){
            throw new IncorrectParametersException(TOO_SMALL_VALUE + minValue);
        }
        if(value > maxValue) {
            throw new IncorrectParametersException(TOO_HIGH_VALUE + maxValue);
        }
    }






}
