INSERT INTO Currencies(Code, FullName, Sign)
VALUES
    ('USD', 'United States dollar', '$'),
    ('RUB', 'Russian ruble', '₽'),
    ('TRY', 'Turkish lira', '₺'),
    ('EUR', 'Euro', '€'),
    ('GBP', 'Sterling', '£'),
    ('CNY', 'Renminbi', '¥');

INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate)
VALUES
    (1, 3, 37.95),
    (1, 4, 0.90),
    (2, 3, 0.45),
    (2, 5, 0.009),
    (4, 2, 93.38),
    (5, 4, 1.19),
    (6, 3, 5.20),
    (5, 6, 9.59);