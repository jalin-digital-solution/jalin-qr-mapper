package co.id.jalin.qrmapper.alto.service;

import co.id.jalin.qrmapper.alto.dto.AltoAuthTokenRequest;
import co.id.jalin.qrmapper.alto.dto.AltoAuthTokenResponse;
import co.id.jalin.qrmapper.cache.ApplicationParameterManager;
import co.id.jalin.qrmapper.cache.CredentialDataManager;
import co.id.jalin.qrmapper.dto.BasicAuth;
import co.id.jalin.qrmapper.service.JwtAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static co.id.jalin.qrmapper.util.StringUtil.base64UrlEncode;
import static co.id.jalin.qrmapper.util.StringUtil.buildCredDataByUserPassKey;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class AltoAuthenticationService {

    private final ApplicationParameterManager applicationParameterManager;
    private final CredentialDataManager credentialDataManager;
    private final JwtAuthenticationService jwtAuthenticationService;

    public AltoAuthTokenResponse authenticate(String authorization, AltoAuthTokenRequest request) {

        // 1. Validate grant type
        if (!VAL_GRANT_TYPE.equals(request.getGrantType())) {
            throw new IllegalArgumentException("Invalid grantType");
        }

        // 2. Decode Basic Auth
        var basic = decodeBasicAuth(authorization);

        // 3. Get credential from Hazelcast
        var credential = credentialDataManager
                .getCredDataByUserPass(buildCredDataByUserPassKey(basic.username(), basic.password(), VAL_TOKEN))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credential"));

        // 4. Expiry
        int expireTime = Integer.parseInt(
                applicationParameterManager
                        .getApplicationParameterValueByName(VAR_ALTO_EXPIRE_TOKEN_IN_SECOND)
                        .orElse(DEFAULT_TOKEN_EXPIRE)
        );

        // 5. Payload
        Map<String, Object> payload = new HashMap<>();
        payload.put(VAR_USER, base64UrlEncode(credential.getUsername()));
        payload.put(VAR_PASS, base64UrlEncode(credential.getPassword()));
        payload.put(VAR_GRANT_TYPE, request.getGrantType());

        // 6. Generate token (choose one)
        var accessToken = jwtAuthenticationService.createToken(payload, credential.getSecretKey(), expireTime);
        // OR MANUAL VERSION
        // var accessToken = jwtAuthenticationService.createTokenManual(payload, credential.getSecretKey(), expireTime);

        return AltoAuthTokenResponse.builder()
                .accessToken(accessToken)
                .tokenType(ACCESS_TOKEN_TYPE_VALUE)
                .expiresIn(String.valueOf(expireTime))
                .build();
    }

    private BasicAuth decodeBasicAuth(String authHeader) {
        if (!authHeader.startsWith(BASIC_AUTH_PREFIX)) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }

        String base64 = authHeader.substring(BASIC_AUTH_PREFIX.length());
        String decoded = new String(Base64.getDecoder().decode(base64));
        String[] parts = decoded.split(COLON_SEPARATOR);

        if (parts.length != BASIC_AUTH_PART_LENGTH) {
            throw new IllegalArgumentException("Invalid Basic Auth format");
        }

        return new BasicAuth(parts[BASIC_AUTH_USER_IDX], parts[BASIC_AUTH_PASS_IDX]);
    }
}
