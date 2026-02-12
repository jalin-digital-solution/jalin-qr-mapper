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

        var credentialData = getCredentialDataFromTokenPayload(h.get(AUTHORIZATION));
        validateToken(h,credentialData.getSecretKey());
        validateSignature(h,relativeUrl,credentialData.getSecretKey());
    }

    private CredentialData getCredentialDataFromTokenPayload(String token){
        try {
            var parts = jwtAuthenticationService.splitTokenPart(token);
            Map<String, Object> payload = jwtAuthenticationService.parseTokenPayload(parts);
            var key = buildCredDataByUserPassKey(
                    base64UrlDecode((String) payload.get(VAR_USER)),
                    base64UrlDecode((String) payload.get(VAR_PASS)),
                    VAL_TOKEN
            );
            return credentialDataManager.getCredDataByUserPass(key).orElseThrow();
        } catch (Exception e) {
            throw new JwtException(e);
        }
    }

    public void validateToken(Map<String,String> h, String secretKey){
        try {
           var claims = jwtAuthenticationService.validateToken(h.get(AUTHORIZATION),secretKey);
            if (!claims.containsKey(VAR_GRANT_TYPE)) {
                throw new IllegalArgumentException("Invalid grant type not exist");
            }
        } catch (Exception e) {
            throw new JwtException(e);
        }
    }

    public void validateSignature(Map<String,String> h, String relativeUrl, String secretKey){
        try {
            List<String> signatureComponent = new ArrayList<>();
            signatureComponent.add(HttpMethod.POST.name());
            signatureComponent.add(relativeUrl);
            signatureComponent.add(h.get(X_CLIENT_KEY));
            signatureComponent.add(signatureService.generateSignatureDigestInstance(requestContext.getRequestBody(),SHA_256));
            signatureComponent.add(h.get(X_TIMESTAMP));
            var signComponentStr = String.join(COLON_SEPARATOR,signatureComponent);
            var generatedSign = signatureService.generateSignatureMacInstance(signComponentStr, secretKey, HMAC_SHA256);
            if (!Objects.equals(generatedSign,h.get(X_SIGNATURE))) {
                throw new IllegalArgumentException("Invalid signature value");
            }
        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }
}
