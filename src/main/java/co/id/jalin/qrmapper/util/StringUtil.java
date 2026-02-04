package co.id.jalin.qrmapper.util;

public class StringUtil {

    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String buildCredDataByUserPassKey(String username, String password, String target) {
        return username + "_" + password + "_" + target;
    }

    public static String buildCredDataByCredIdKey(String credentialIdentifier, String target) {
        return credentialIdentifier + "_" + target;
    }
}
