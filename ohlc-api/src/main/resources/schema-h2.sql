-- ============================================================
--  H2 on-disk schema  –  auto-applied on startup via
--  spring.sql.init.schema-locations: classpath:schema-h2.sql
-- ============================================================

-- ── Security / User domain ──────────────────────────────────

CREATE TABLE IF NOT EXISTS "USER" (
    user_id       UUID         NOT NULL DEFAULT RANDOM_UUID(),
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255),
    username      VARCHAR(255) NOT NULL,
    is_blocked    BOOLEAN      DEFAULT FALSE,
    is_active     BOOLEAN      DEFAULT TRUE,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    updated_by    UUID,
    row_version   NUMERIC,

    PRIMARY KEY (user_id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS ROLE (
    role_id INT          NOT NULL,
    role    VARCHAR(255) NOT NULL,

    PRIMARY KEY (role_id)
);

CREATE TABLE IF NOT EXISTS USER_ROLE (
    user_id UUID NOT NULL,
    role_id INT  NOT NULL DEFAULT 1,

    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES "USER" (user_id),
    FOREIGN KEY (role_id) REFERENCES ROLE (role_id)
);

CREATE TABLE IF NOT EXISTS AUTHPROVIDER (
    auth_provider_id INT          NOT NULL,
    auth_provider    VARCHAR(255) NOT NULL,

    PRIMARY KEY (auth_provider_id)
);

CREATE TABLE IF NOT EXISTS USER_AUTHPROVIDER (
    user_provider_id UUID         NOT NULL,
    user_id          UUID         NOT NULL,
    auth_provider_id INT          NOT NULL,
    oauth_user_id    VARCHAR(255) NOT NULL,

    PRIMARY KEY (user_provider_id),
    CONSTRAINT uq_user_provider UNIQUE (user_id, auth_provider_id),
    CONSTRAINT uq_provider_user UNIQUE (auth_provider_id, user_provider_id),
    FOREIGN KEY (user_id)          REFERENCES "USER" (user_id),
    FOREIGN KEY (auth_provider_id) REFERENCES AUTHPROVIDER (auth_provider_id)
);

-- ── Ticker ──────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS STOCK_TICKER (
    ticker_id INT          NOT NULL AUTO_INCREMENT,
    symbol    VARCHAR(255) NOT NULL,
    title     VARCHAR(255) NOT NULL,

    PRIMARY KEY (ticker_id),
    CONSTRAINT uq_stock_symbol UNIQUE (symbol)
);

-- ── OHLC candles ────────────────────────────────────────────
--  Columns mirror OhlcEntity + BaseEntity:
--    market_date, ticker_symbol  → composite PK (OhlcEntityPk)
--    open / high / low / close   → DOUBLE PRECISION
--    volume                      → BIGINT  (Long in Java)
--    created_at                  → TIMESTAMP NOT NULL  (BaseEntity @CreatedDate)
--    updated_at                  → TIMESTAMP           (BaseEntity @LastModifiedDate)
--    updated_by                  → UUID                (BaseEntity @LastModifiedBy)
--    row_version                 → INT                 (BaseEntity @Version)

CREATE TABLE IF NOT EXISTS OHLC_DAILY (
    market_date   DATE         NOT NULL,
    ticker_symbol VARCHAR(255) NOT NULL,
    open          DOUBLE       NOT NULL,
    high          DOUBLE       NOT NULL,
    low           DOUBLE       NOT NULL,
    close         DOUBLE       NOT NULL,
    volume        BIGINT,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    updated_by    UUID,
    row_version   INT,

    PRIMARY KEY (market_date, ticker_symbol)
);

CREATE TABLE IF NOT EXISTS OHLC_WEEKLY (
    market_date   DATE         NOT NULL,
    ticker_symbol VARCHAR(255) NOT NULL,
    open          DOUBLE       NOT NULL,
    high          DOUBLE       NOT NULL,
    low           DOUBLE       NOT NULL,
    close         DOUBLE       NOT NULL,
    volume        BIGINT,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    updated_by    UUID,
    row_version   INT,

    PRIMARY KEY (market_date, ticker_symbol)
);

CREATE TABLE IF NOT EXISTS OHLC_MONTHLY (
    market_date   DATE         NOT NULL,
    ticker_symbol VARCHAR(255) NOT NULL,
    open          DOUBLE       NOT NULL,
    high          DOUBLE       NOT NULL,
    low           DOUBLE       NOT NULL,
    close         DOUBLE       NOT NULL,
    volume        BIGINT,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    updated_by    UUID,
    row_version   INT,

    PRIMARY KEY (market_date, ticker_symbol)
);

-- ── Moving averages ─────────────────────────────────────────
--  Columns mirror MaEntity (composite PK via MaEntityPk):
--    ticker_symbol, market_date  → composite PK
--    sma_50 / sma_100 / sma_200  → DOUBLE
--    cross_ohlc_sma*             → DOUBLE (nullable)

CREATE TABLE IF NOT EXISTS MOVING_AVERAGE_DAILY (
    ticker_symbol      VARCHAR(255) NOT NULL,
    market_date        DATE         NOT NULL,
    sma_50             DOUBLE,
    sma_100            DOUBLE,
    sma_200            DOUBLE,
    cross_ohlc_sma50   DOUBLE,
    cross_ohlc_sma100  DOUBLE,
    cross_ohlc_sma200  DOUBLE,

    PRIMARY KEY (ticker_symbol, market_date)
);

CREATE TABLE IF NOT EXISTS MOVING_AVERAGE_WEEKLY (
    ticker_symbol      VARCHAR(255) NOT NULL,
    market_date        DATE         NOT NULL,
    sma_50             DOUBLE,
    sma_100            DOUBLE,
    sma_200            DOUBLE,
    cross_ohlc_sma50   DOUBLE,
    cross_ohlc_sma100  DOUBLE,
    cross_ohlc_sma200  DOUBLE,

    PRIMARY KEY (ticker_symbol, market_date)
);
