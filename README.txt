It is study implementation of exchange rates backend program (the third task), inspired by the Java-road-map made by https://github.com/zhukovsd.

The link to test: http://77.95.201.243:8080/exchange-rate-1.0.0/
The implementation provides a set of features and consists of:
  -table of currencies;
    -add new currency with fields (name, code, sign);
    -get all currencies;  
    -get exact currency with currency's code
  -table of exchange rates;
    -add new exchange rate with pair of currency's codes (if they exists in the currency table) and rate
    -get exact exchange rate with pair of currency's codes;
    -get all exchange rates;
    -update the rate value for existing exchange rates;
    -calculate currency exchange;
The currency exchange block returns the rate and calculate the amount of needed money for 3 situation:
    -table of exchange rates includes the direct rate for pair, returns the rate;
    -table of exchange rates doesn't include the direct rate but has the reverse rate, returns recalculated rate;
    -table of exchange rate doesn't include two mentioned cases and it tries to find cross rate for 4 cases:
      We need exchange currency A-B but table might has one of those:
        A-C, B-C;
        A-C, C-B;
        C-A, B-C;
        C-A, C-B;
If table has one of this cases, it calculates cross rate and returns it. In the case of several combinations, it returns the most profit rate for client;

Database has several first notes, that gives you an opportunity to test all cases of currency exchange without adding new pair or new currency in.

I would appreciate if you check my code and test the program.
Thank you for attention.
