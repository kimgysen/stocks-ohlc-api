
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS  citext;

CREATE TABLE IF NOT EXISTS USER(
    user_id UUID NOT NULL DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email CITEXT UNIQUE,
    username TEXT NOT NULL,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    updated_by UUID,
    row_version NUMERIC,

    PRIMARY KEY (user_id)
);
--
CREATE TABLE IF NOT EXISTS ROLE (
    role_id INT NOT NULL,
    role TEXT NOT NULL,

    PRIMARY KEY (role_id)
);
--
CREATE TABLE IF NOT EXISTS USER_ROLE(
    user_id UUID NOT NULL,
    role_id INT NOT NULL DEFAULT 1,

    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (role_id) REFERENCES ROLE(role_id)
);
--
CREATE TABLE IF NOT EXISTS AUTHPROVIDER(
    auth_provider_id INT NOT NULL,
    auth_provider TEXT NOT NULL,

    PRIMARY KEY (auth_provider_id)
);
--
CREATE TABLE IF NOT EXISTS USER_AUTHPROVIDER(
    user_provider_id UUID NOT NULL,
    user_id UUID NOT NULL,
    auth_provider_id INT NOT NULL,
    oauth_user_id TEXT NOT NULL,

    PRIMARY KEY (user_provider_id),
    UNIQUE(user_id, auth_provider_id),
    UNIQUE(auth_provider_id, user_provider_id),
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (auth_provider_id) REFERENCES AUTHPROVIDER(auth_provider_id)
);
--
CREATE TABLE IF NOT EXISTS STOCK_TICKER(
    ticker_id SERIAL NOT NULL,
    symbol TEXT NOT NULL,
    title TEXT NOT NULL,

    UNIQUE (symbol),
);
--
CREATE TABLE IF NOT EXISTS OHLC_DAILY(
    market_date DATE NOT NULL,
    ticker_symbol VARCHAR(255) NOT NULL,
    volume numeric NULL,
    open DOUBLE PRECISION NOT NULL,
    high DOUBLE PRECISION NOT NULL,
    low DOUBLE PRECISION NOT NULL,
    close DOUBLE PRECISION NOT NULL,

    PRIMARY KEY (market_date, ticker_symbol)
);
--
