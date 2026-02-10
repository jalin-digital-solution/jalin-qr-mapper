package co.id.jalin.qrmapper.alto.service;

import co.id.jalin.qrmapper.cache.CredentialDataManager;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.entity.CredentialData;
import co.id.jalin.qrmapper.exception.HttpHeaderException;
import co.id.jalin.qrmapper.exception.JwtException;
import co.id.jalin.qrmapper.exception.SignatureException;
import co.id.jalin.qrmapper.service.JwtAuthenticationService;
import co.id.jalin.qrmapper.service.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static co.id.jalin.qrmapper.util.StringUtil.*;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Service
@RequiredArgsConstructor
public class AltoValidationService {

    private final CredentialDataManager credentialDataManager;
    private final JwtAuthenticationService jwtAuthenticationService;
    private final SignatureService signatureService;
    private final RequestContext requestContext;

    public void validateHeaders(Map<String, String> h, String relativeUrl) {
        if (isBlank(h.get(AUTHORIZATION))) throw new HttpHeaderException("Missing Authorization");
        if (isBlank(h.get(X_CLIENT_KEY)))  throw new HttpHeaderException("Missing X-CLIENT-KEY");
        if (isBlank(h.get(X_TIMESTAMP)))   throw new HttpHeaderException("Missing X-TIMESTAMP");
        if (isBlank(h.get(X_SIGNATURE)))   throw new HttpHeaderException("Missing X-SIGNATURE");

        var credentialData = validateToken(h);
        validateSignature(h,relativeUrl,credentialData.getSecretKey());
    }

    public CredentialData validateToken(Map<String,String> h){
        try {
            var token = h.get(AUTHORIZATION);
            var parts = jwtAuthenticationService.splitTokenPart(token);
            var payload = jwtAuthenticationService.parseTokenPayload(parts);
            var key = buildCredDataByUserPassKey(
                    base64UrlDecode((String) payload.get(VAR_USER)),
                    base64UrlDecode((String) payload.get(VAR_PASS)),
                    VAL_TOKEN
            );

            var credentialData = credentialDataManager.getCredDataByUserPass(key).orElseThrow();
            var claims = jwtAuthenticationService.validateToken(h.get(AUTHORIZATION),credentialData.getSecretKey());
            if (!claims.containsKey(VAR_GRANT_TYPE)) {
                throw new IllegalArgumentException("Invalid grant type not exist");
            }
            return credentialData;
        } catch (Exception e) {
            throw new JwtException(e);
        }
    }

    public boolean validateSignature(Map<String,String> h, String relativeUrl, String secretKey){
        try {
            List<String> signatureComponent = new ArrayList<>();
            signatureComponent.add(HttpMethod.POST.name());
            signatureComponent.add(relativeUrl);
            signatureComponent.add(h.get(X_CLIENT_KEY));
            signatureComponent.add(signatureService.generateSignatureDigestInstance(requestContext.getRequestBody(),SHA_256));
            signatureComponent.add(h.get(X_TIMESTAMP));

            var signComponentStr = String.join(COLON_SEPARATOR,signatureComponent);
            return Objects.equals(signatureService.generateSignatureMacInstance(signComponentStr,secretKey,HMAC_SHA256),h.get(X_SIGNATURE));
        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }
}
