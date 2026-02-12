-- Create database qrpayment_mapper
CREATE DATABASE IF NOT EXISTS qr_mapper;
USE qr_mapper;

-- Create tables
CREATE TABLE IF NOT EXISTS application_parameter
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    value       VARCHAR(255),
    description VARCHAR(255),
    created_at  TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE IF NOT EXISTS transaction_log
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    trace_id            VARCHAR(255),
    api_service         VARCHAR(255),
    leg1_rrn            VARCHAR(255),
    leg2_rrn            VARCHAR(255),
    leg3_rc             VARCHAR(255),
    leg4_rc             VARCHAR(255),
    invoice_number      VARCHAR(255),
    switching_rrn       VARCHAR(255),
    amount              DECIMAL(20, 2),
    leg1                TEXT,
    leg2                TEXT,
    leg3                TEXT,
    leg4                TEXT,
    additional_info     VARCHAR(255),
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP
);

CREATE TABLE IF NOT EXISTS rc_mapping
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_code      VARCHAR(255),
    input_rc          VARCHAR(255),
    output_rc         VARCHAR(255),
    input_rc_message  VARCHAR(255),
    output_rc_message VARCHAR(255),
    created_at        TIMESTAMP,
    updated_at        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS credential_data
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    credential_identifier VARCHAR(1000),
    credential_name       VARCHAR(255),
    username              VARCHAR(255),
    password              VARCHAR(1000),
    api_key               VARCHAR(1000),
    secret_key            VARCHAR(1000),
    credential_target     VARCHAR(200),
    description           VARCHAR(255),
    created_at            TIMESTAMP,
    updated_at            TIMESTAMP
);

CREATE TABLE IF NOT EXISTS access_token
(
    credential_identifier VARCHAR(100) NOT NULL,
    access_token          VARCHAR(500) NOT NULL,
    token_type            VARCHAR(100),
    token_mode            VARCHAR(100),
    expires_in            BIGINT,
    created_at            TIMESTAMP,
    updated_at            TIMESTAMP,
    PRIMARY KEY (credential_identifier, access_token)
);

CREATE TABLE IF NOT EXISTS transaction_log_detail (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    trace_id VARCHAR(255) NOT NULL,
    int_reff VARCHAR(255) NOT NULL,
    body_payload TEXT NOT NULL,
    leg_order INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX transaction_log_leg1_rrn_idx ON transaction_log (leg1_rrn, api_service);
CREATE INDEX rc_mapping_service_code_idx ON rc_mapping (service_code);
CREATE INDEX transaction_log_detail_int_reff_leg_idx ON transaction_log_detail (int_reff, leg_order);
CREATE INDEX transaction_log_detail_trace_id_idx ON transaction_log_detail (trace_id);
CREATE INDEX transaction_log_detail_created_at_idx ON transaction_log_detail (created_at);
