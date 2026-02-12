USE qr_mapper;

-- credential_data inserts
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES(UUID(), 'DANA', 'dana_dev', 'qwer1234', 'DANA_DEV_DfIaMsbOhSW58SrrKyRnBK29qhdmdzROe703JbP66bUVp6EynEdWY5O', 'eXCwqpURcrHjhEHRd5BEsN1fNN8hovWCyWl1rMgNCu8=', 'token', 'Dana as Client', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES('93600915', 'Dana', 'DanaInq', 'dana123', '', 'SoaJaLinBank123', '/qr/inqRepos', 'ESB Credentials for DANA', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES('93600915', 'Dana', 'DanaPay', 'dana123', '', 'SoaJaLinBank123', '/qr/payment', 'ESB Credentials for DANA', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES('93600915', 'Dana', 'DanaCheck', 'dana123', '', 'SoaJaLinBank123', '/qr/check', 'ESB Credentials for DANA', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES('93600915', 'Dana', 'DanaRefund', 'dana123', '', 'SoaJaLinBank123', '/qr/refund', 'ESB Credentials for DANA', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES(UUID(), 'BNI', 'bni_dev', 'qwer1234', 'BNI_DEV_LZkiUS3B6AQ3AmO3y25lohp1NJOAf9TOBMtVONz4AUVkGFeZCZUmorgb', 'mx1tesXCnGV0fS6pqETyfrNYQAmBp8vFhwgW5tPtNL4=', 'open-service', 'BNI as Client', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES('93600009', 'BNI', 'BniInq', 'bni123', '', 'SoaJaLinBank123', '/qr/inqRepos', 'ESB Credentials for BNI', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES('93600009', 'BNI', 'BniPay', 'bni123', '', 'SoaJaLinBank123', '/qr/payment', 'ESB Credentials for BNI', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES('93600009', 'BNI', 'BniCheck', 'bni123', '', 'SoaJaLinBank123', '/qr/check', 'ESB Credentials for BNI', NOW(), NOW());
INSERT INTO credential_data(credential_identifier, credential_name, username, password, api_key, secret_key, credential_target, description, created_at, updated_at) VALUES('93600009', 'BNI', 'BniRefund', 'bni123', '', 'SoaJaLinBank123', '/qr/refund', 'ESB Credentials for BNI', NOW(), NOW());

-- rc_mapping esb-dana inserts
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','00','','001','Success',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','01','','081','Authentication Error',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','02','','081','Authentication Error',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','03','','103','Invalid Merchant',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','05','','104','Do not honor',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','12','','071','Invalid Transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','13','','050','Invalid amount to payment',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','14','','040','invalid account number to payment',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','15','','041','Invalid account number to hold',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','30','','105','Format Error',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','31','','087','Bank not supported',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','40','','088','Transaction not allowed',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','51','','094','Inssuficient fund',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','57','','085','Account is suspended',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','58','','106','Transaction not permitted to terminal',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','59','','107','Suspected fraud',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','61','','108','Exceeds transaction amount limit',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','62','','109','Restricted card',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','65','','110','Exceeds transaction frequency limit',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','68','','020','Transaction timed out',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','70','','104','Error general system',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','71','','112','Invalid routing',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','72','','100','Link down',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','73','','073','Invalid host',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','75','','105','Format error',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','76','','112','Invalid routing',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','77','','081','Authentication Error',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','78','','112','Invalid routing',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','80','','112','Invalid routing',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','83','','010','Rejected By destination Bank',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','90','','101','Switching not operate or switching host unreachable',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','91','','100','Link down',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','92','','112','Invalid routing',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','94','','111','Dupplicate transmission /duplicate QR',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','96','','060','Internal server error',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-DANA','A0','','A00','QR payment fail (Mutation does not perform)',NOW(),NOW());

-- rc_mapping esb-bni inserts
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','00','','00','Approved or completed successfully',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','01','','05','Do not honor',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','02','','30','Format error / Invalid Signature',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','03','','03','Invalid merchant',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','05','','05','Do not honor',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','12','','12','Invalid transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','13','','13','Invalid amount',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','14','','14','Invalid card number (no such number)',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','15','','15','No such issuer',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','30','','30','Format error / Invalid Signature',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','31','','31','Bank not supported by switch',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','40','','40','Requested function not supported',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','51','','51','Insufficient funds',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','57','','57','Transaction not permitted to cardholder / QR is expired',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','58','','58','Transaction not permitted to terminal',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','59','','59','Suspected fraud',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','61','','61','Exceeds amount transaction limit',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','62','','62','Restricted card',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','65','','65','Exceeds frequency transaction limit',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','68','','68','Response received too late',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','71','','92','Unable to route transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','72','','89','Link down',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','73','','89','Link down',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','75','','30','Format error / Invalid Signature',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','76','','92','Unable to route transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','77','','30','Format error / Invalid Signature',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','78','','92','Unable to route transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','80','','92','Unable to route transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','83','','83','No accounts',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','90','','90','Cutoff is in process, a switch is ending business for a day and starting the next (transaction can besent again in a few minutes)',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','91','','89','Link down',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','92','','92','Unable to route transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','94','','94','Duplicate transaction / QR is duplicate',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','96','','96','System malfunction / system error',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-ESB-TO-BNI','A0','','A0','QR Payment Fail (Mutation not perform)',NOW(),NOW());

-- rc_mapping dana-esb inserts
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','001','','00','Success',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','003','','12','Invalid Transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','030','','12','Invalid Transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','040','','14','Invalid PAN Number',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','050','','13','Invalid Amount',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','060','','68','Suspend',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','070','','30','Format Error',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','071','','12','Invalid Transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','092','','12','Invalid Transaction',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','103','','03','Invalid Merchant',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','A00','','A0','QR payment fail (Mutation does not perform)',NOW(),NOW());

-- rc_mapping dana-esb additional inserts
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','001','','00','Success',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','095','','83','Requested data not found',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','087','','31','Bank not supported',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','088','','40','Transaction not allowed',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','093','','14','Bank not found',NOW(),NOW());
INSERT INTO rc_mapping(service_code, input_rc, input_rc_message, output_rc, output_rc_message, created_at, updated_at) VALUES('QR-DOM-MPM-DANA-TO-ESB','112','','92','Invalid routing',NOW(),NOW());

-- application_parameter inserts
INSERT INTO application_parameter(name, value, description, created_at, updated_at) VALUES('ALTO_EXPIRE_TOKEN_IN_SECOND', '3600', 'Expired token for dana', NOW(), NOW());
INSERT INTO application_parameter(name, value, description, created_at, updated_at) VALUES('BASE_URL_ESB', 'http://13.131.7.154:8484', 'base url for dana', NOW(), NOW());
INSERT INTO application_parameter(name, value, description, created_at, updated_at) VALUES('BASE_URL_DANA', 'https://10.195.0.110:443/jalin', 'base url for dana', NOW(), NOW());
INSERT INTO application_parameter(name, value, description, created_at, updated_at) VALUES('LAST_UPDATE_DB_RECOVERY', CAST(NOW() AS CHAR), 'Last recovery can write grant', NOW(), NOW());