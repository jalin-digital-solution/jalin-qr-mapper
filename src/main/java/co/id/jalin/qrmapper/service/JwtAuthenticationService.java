package co.id.jalin.qrmapper.service;

import co.id.jalin.qrmapper.exception.JwtException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HexFormat;
import java.util.Map;

import static co.id.jalin.qrmapper.util.StringUtil.base64UrlDecode;
import static co.id.jalin.qrmapper.util.StringUtil.base64UrlEncode;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final ObjectMapper objectMapper;

    public String createToken(Map<String, Object> payload, String secretKey, int expiresInSeconds) {
        var now = Instant.now();
        return Jwts.builder()
                .claims(payload)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expiresInSeconds)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS256)
                .compact();
    }

    public Claims validateToken(String token, String secretKey) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Existing in JS createTokenManual
     */
    public String createTokenManual(Map<String, Object> payload, String secretKey, int expiresInSeconds) {
        try {
            Map<String, Object> header = Map.of(
                    VAR_ALG, VAL_HS256,
                    VAR_TYP, VAL_JWT
            );
            long now = Instant.now().getEpochSecond();
            payload.put(VAR_EXP, now + expiresInSeconds);

            String encodedHeader = base64UrlEncode(objectMapper.writeValueAsString(header));
            String encodedPayload = base64UrlEncode(objectMapper.writeValueAsString(payload));

            String signature = encryptWithSecretToHex(encodedHeader + DOT + encodedPayload, secretKey, HMAC_SHA256);
            return encodedHeader + DOT + encodedPayload + DOT + signature;
        } catch (Exception e) {
            throw new JwtException(e);
        }
    }

    /**
     * Existing in JS validateTokenManual
     */
    public Map<String, Object> validateTokenManual(String token, String secretKey) {
        try {
            String[] parts = splitTokenPart(token);

            String signature = encryptWithSecretToHex(parts[JWT_HEADER_IDX] + DOT + parts[JWT_BODY_IDX], secretKey, HMAC_SHA256);
            if (!signature.equals(parts[JWT_SIGN_IDX])) {
                throw new IllegalArgumentException("Invalid signature");
            }

            Map<String, Object> payload = parseTokenPayload(parts);
            long exp = ((Number) payload.get(VAR_EXP)).longValue();

            if (Instant.now().getEpochSecond() > exp) {
                throw new IllegalArgumentException("Token expired");
            }

            return payload;
        } catch (Exception e) {
            throw new JwtException(e);
        }
    }

    private String encryptWithSecretToHex(String data, String key, String algorithm) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            mac.init(new SecretKeySpec(key.getBytes(), algorithm));
            return HexFormat.of().formatHex(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new JwtException(e);
        }
    }

    public String[] splitTokenPart(String token){
        String[] parts = token.split("\\.");
        if (parts.length != JWT_PART_LENGTH) {
            throw new IllegalArgumentException("Invalid token");
        }
        return parts;
    }

    public Map<String, Object> parseTokenPayload(String[] parts) throws JsonProcessingException {
        TypeReference<Map<String,Object>> ref = new TypeReference<>(){};
        return objectMapper.readValue(base64UrlDecode(parts[JWT_BODY_IDX]),ref);
    }

    public String resolveBearerToken(String authorization) {
        if (authorization.length() > JWT_AUTHORIZATION_IDX && authorization.startsWith(ACCESS_TOKEN_TYPE_VALUE)) {
            return authorization.substring(JWT_AUTHORIZATION_IDX);
        }
        return EMPTY_STRING;
    }

}
