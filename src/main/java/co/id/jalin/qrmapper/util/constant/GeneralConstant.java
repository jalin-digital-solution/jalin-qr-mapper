package co.id.jalin.qrmapper.util.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class GeneralConstant {

    private GeneralConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String APPLICATION = "APPLICATION";
    public static final String DOUBLE_DASH = "--";
    public static final String EMPTY_STRING = "";
    public static final String SPACE_STRING = " ";
    public static final String UNDERSCORE = "_";
    public static final String DOT = ".";
    public static final String COLON_SEPARATOR = ":";
    public static final String SEMICOLON_SEPARATOR = ";";
    public static final String BASIC_AUTH_PREFIX = "Basic ";
    public static final String DEFAULT_TOKEN_EXPIRE = "3600";

    public static final String DEFAULT_POS_ENTRY_MODE = "011";
    public static final String DEFAULT_FORWARDING_ID = "360004";
    public static final String DEFAULT_STAN = "000000";
    public static final String DEFAULT_RRN = "000000000000";
    public static final String DEFAULT_APPROVAL_CODE = "000000";
    public static final String DEFAULT_INVOICE_NUMBER = "00000000000000000000";
    public static final String DEFAULT_PROC_CODE_CHECK_STATUS = "360000";
    public static final String FEE_TYPE_CREDIT = "C";
    public static final String FEE_TYPE_DEBIT = "D";
    public static final String PI_Q001 = "Q001";
    public static final String PROC_CODE_26 = "26";
    public static final String PROC_CODE_36 = "36";

    public static final String ACCESS_TOKEN_TYPE_VALUE = "Bearer";
    public static final String X_TRACE_ID = "X-TRACE-ID";
    public static final String MAC = "mac";

    public static final String HMAC_SHA256 = "HmacSHA256";
    public static final String SHA_256 = "SHA-256";

    public static final String VAR_APP_PARAM = "APP_PARAM";
    public static final String VAR_APP_PARAM_VALUE = "APP_PARAM_VALUE";
    public static final String VAR_CRED_DATA_BY_USER_PASS = "CRED_DATA_BY_USER_PASS";
    public static final String VAR_CRED_DATA_BY_CRED_ID = "CRED_DATA_BY_CRED_ID";
    public static final String VAR_RC_MAPPING_JALIN_TO_DANA = "RC_MAPPING_JALIN_TO_DANA";
    public static final String VAR_RC_MAPPING_DANA_TO_JALIN = "RC_MAPPING_DANA_TO_JALIN";
    public static final String VAR_ALTO_EXPIRE_TOKEN_IN_SECOND = "ALTO_EXPIRE_TOKEN_IN_SECOND";
    public static final String VAR_SKIP_OUTGOING_REQUEST = "SKIP_OUTGOING_REQUEST";

    public static final String VAR_ALG = "alg";
    public static final String VAR_TYP = "typ";
    public static final String VAR_EXP = "exp";

    public static final String VAR_USER = "username";
    public static final String VAR_PASS = "password";
    public static final String VAR_GRANT_TYPE = "grantType";

    public static final String AUTHORIZATION = "authorization";
    public static final String X_CLIENT_KEY  = "x-client-key";
    public static final String X_TIMESTAMP   = "x-timestamp";
    public static final String X_SIGNATURE   = "x-signature";

    public static final String VAL_TOKEN = "token";
    public static final String VAL_HS256 = "HS256";
    public static final String VAL_JWT = "JWT";
    public static final String VAL_GRANT_TYPE = "client_credentials";

    public static final String QR_DOM_MPM_ESB_TO_DANA = "QR-DOM-MPM-ESB-TO-DANA";
    public static final String QR_DOM_MPM_DANA_TO_ESB = "QR-DOM-MPM-DANA-TO-ESB";

    public static final String RESP_CODE_SUCCESS = "00";
    public static final String RESP_MESSAGE_SUCCESS = "Success";
    public static final String RESP_CODE_DO_NOT_HONOR = "05";
    public static final String RESP_MESSAGE_DO_NOT_HONOR = "Do not honor";
    public static final String RESP_CODE_FORMAT_ERROR = "30";
    public static final String RESP_MESSAGE_FORMAT_ERROR = "Format error";
    public static final String RESP_CODE_TIMEOUT = "68";
    public static final String RESP_MESSAGE_TIMEOUT = "Timeout";
    public static final String RESP_CODE_SYSTEM_MALFUNCTION = "96";
    public static final String RESP_MESSAGE_SYSTEM_MALFUNCTION = "System malfunction";

    public static final String ALT_CC_IDR = "IDR";
    public static final String ALT_RESP_CODE_SUCCESS = "001";
    public static final String ALT_RESP_MESSAGE_SUCCESS = "Success";
    public static final String ALT_RESP_CODE_PAYMENT_FAIL = "A00";
    public static final String ALT_RESP_MESSAGE_PAYMENT_FAIL = "QR payment fail";
    public static final String ALT_RESP_CODE_DO_NOT_HONOR = "000";
    public static final String ALT_RESP_MESSAGE_DO_NOT_HONOR = "Do not honor";
    public static final String ALT_RESP_CODE_TIMEOUT = "020";
    public static final String ALT_RESP_MESSAGE_TIMEOUT = "Transaction timeout";
    public static final String ALT_RESP_CODE_SYSTEM_MALFUNCTION = "060";
    public static final String ALT_RESP_MESSAGE_SYSTEM_MALFUNCTION = "System malfunction";
    public static final String ALT_RESP_CODE_FORMAT_ERROR = "070";
    public static final String ALT_RESP_MESSAGE_FORMAT_ERROR = "Invalid message format";
    public static final String ALT_RESP_CODE_INVALID_SIGN = "082";
    public static final String ALT_RESP_MESSAGE_INVALID_SIGN = "Invalid signature";
    public static final String ALT_RESP_CODE_INVALID_TOKEN = "083";
    public static final String ALT_RESP_MESSAGE_INVALID_TOKEN = "Invalid token";
    public static final String ALT_RESP_CODE_PAYMENT_NOT_FOUND = "096";
    public static final String ALT_RESP_MESSAGE_PAYMENT_NOT_FOUND = "Requested data not found (rrn)";

    public static final String ALT_RESP_MESSAGE_INVALID_HEADER = "Invalid header";
    public static final String ALT_RESP_MESSAGE_SUSPECT = "Suspect";

    public static final Integer NOT_EXIST = 0;
    public static final Integer MONEY_DECIMAL_SCALE = 2;
    public static final Integer AUTH_INDEX_OF_ACCESS_TOKEN = 1;
    public static final Integer BASIC_AUTH_PART_LENGTH = 2;
    public static final Integer BASIC_AUTH_USER_IDX = 0;
    public static final Integer BASIC_AUTH_PASS_IDX = 1;
    public static final Integer JWT_AUTHORIZATION_IDX = 7;
    public static final Integer JWT_PART_LENGTH = 3;
    public static final Integer JWT_HEADER_IDX = 0;
    public static final Integer JWT_BODY_IDX = 1;
    public static final Integer JWT_SIGN_IDX = 2;
    public static final Integer RRN_START_IDX = 0;
    public static final Integer RRN_LENGTH = 12;
    public static final Integer SETTLEMENT_DAY_OFFSET = 1;
    public static final Integer START_IDX_ALTO_TRX_PATH = 12;

    public static final DateTimeFormatter ALTO_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    public static final DateTimeFormatter ESB_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter ESB_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final ZoneId ZONE_ID_JAKARTA = ZoneId.of("Asia/Jakarta");

    public static final Map<String, String> CURRENCY_CODE_ALPHA_TO_NUM = Map.of("IDR", "360");
    public static final Map<String, String> CURRENCY_CODE_NUM_TO_ALPHA = Map.of("360", "IDR");
    public static final Map<String, String> ACCOUNT_TYPE_CODE_TO_DESC = Map.of("00", "UNSPECIFIED","10", "SAVING","30", "CREDIT","60", "E-WALLET");
}
