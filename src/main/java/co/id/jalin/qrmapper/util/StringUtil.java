package co.id.jalin.qrmapper.util;

import java.util.Base64;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.UNDERSCORE;

public class StringUtil {

    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String buildCredDataByUserPassKey(String username, String password, String target) {
        return username + UNDERSCORE + password + UNDERSCORE + target;
    }

    public static String buildCredDataByCredIdKey(String credentialIdentifier, String target) {
        return credentialIdentifier + UNDERSCORE + target;
    }

    public static String base64UrlEncode(String value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value.getBytes());
    }

    public static String base64UrlDecode(String value) {
        return new String(Base64.getUrlDecoder().decode(value));
    }
}
