package co.id.jalin.qrmapper.util.constant;

public class GeneralConstant {

    private GeneralConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String APPLICATION = "APPLICATION";
    public static final String DOUBLE_DASH = "--";
    public static final String EMPTY_STRING = "";
    public static final String SPACE_STRING = " ";

    public static final String ACCESS_TOKEN_TYPE_VALUE = "Bearer";
    public static final String X_TRACE_ID = "X-TRACE-ID";

    public static final String RESP_CODE_SUCCESS = "00";
    public static final String RESP_MESSAGE_SUCCESS = "Success";
    public static final String RESP_CODE_DO_NOT_HONOR = "05";
    public static final String RESP_MESSAGE_DO_NOT_HONOR = "Do not honor";
    public static final String RESP_CODE_SYSTEM_MALFUNCTION = "96";
    public static final String RESP_MESSAGE_SYSTEM_MALFUNCTION = "System malfunction";

    public static final Integer NOT_EXIST = 0;
    public static final Integer AUTH_INDEX_OF_ACCESS_TOKEN = 1;
}
