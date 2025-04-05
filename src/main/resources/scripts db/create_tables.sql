CREATE TABLE IF NOT EXISTS Currencies
(   ID          INTEGER  PRIMARY KEY AUTOINCREMENT,
    Code        VARCHAR  CHECK (length(code) == 3)  NOT NULL,
    FullName    VARCHAR                             NOT NULL,
    Sign        VARCHAR                             NOT NULL,
    CONSTRAINT currencies_uniw_code UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS ExchangeRates
(   ID                  INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId      INTEGER,
    TargetCurrencyId    INTEGER,
    Rate                DECIMAL(6),
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies (ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies (ID),
    UNIQUE (BaseCurrencyId, TargetCurrencyId));