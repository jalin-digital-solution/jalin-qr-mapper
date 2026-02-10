package co.id.jalin.qrmapper.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.EMPTY_STRING;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.UNDERSCORE;

public class StringUtil {

    private StringUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isBlank(String v) {
        return v == null || v.isBlank();
    }

    public static String generateLocalRandomStan(){
        return String.format("%06d", ThreadLocalRandom.current().nextInt(1_000_000));
    }

    public static String generateLocalRandomRrn(){
        return String.format("%012d", ThreadLocalRandom.current().nextLong(1_000_000_000_000L));
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

    public static String handlePostMethod(HttpServletRequest requestWrapper) throws IOException {
        if (HttpMethod.POST.name().equals(requestWrapper.getMethod()) && Objects.nonNull(requestWrapper.getContentType())) {
            return requestWrapper.getReader().lines().collect(Collectors.joining(EMPTY_STRING));
        }
        return EMPTY_STRING;
    }
}
