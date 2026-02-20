package co.id.jalin.qrmapper.alto.client;

import co.id.jalin.qrmapper.alto.dto.AltoPaymentRefundRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentRefundResponseDto;
import co.id.jalin.qrmapper.cache.CredentialDataManager;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.exception.WebClientConnectTimeoutException;
import co.id.jalin.qrmapper.exception.WebClientGeneralException;
import co.id.jalin.qrmapper.exception.WebClientResponseTimeoutException;
import co.id.jalin.qrmapper.service.SignatureService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.ConnectException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static co.id.jalin.qrmapper.util.StringUtil.buildCredDataByCredIdKey;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class AltoRestClient {

    @Value("${alto.dana.base.url}")
    private String altoDanaBaseUrl;
    @Value("${credential.identifier.alto.dana}")
    private String altoDanaCredId;

    private final RequestContext requestContext;
    private final WebClient webClientAlto;
    private final ObjectMapper objectMapper;
    private final SignatureService signatureService;
    private final CredentialDataManager credentialDataManager;

    // Need to adjust DTO
    public AltoPaymentRefundResponseDto sendPayment(
            AltoPaymentRefundRequestDto requestDto,
            String apiPathPayment
    ) {
        try {
            var credentialData = credentialDataManager
                    .getCredDataByCredId(buildCredDataByCredIdKey(altoDanaCredId,VAL_TOKEN))
                    .orElseThrow(() -> new WebClientGeneralException("Credential data destination is not set with identifier "+ altoDanaCredId));
            var uri = UriComponentsBuilder.fromUriString(altoDanaBaseUrl + apiPathPayment + ".htm").toUriString();

            var requestDtoStr = objectMapper.writeValueAsString(requestDto);

            List<String> signatureComponent = new ArrayList<>();
            signatureComponent.add(HttpMethod.POST.name());
            signatureComponent.add(apiPathPayment);
            signatureComponent.add(credentialData.getApiKey());
            signatureComponent.add(signatureService.generateSignatureDigestInstance(requestDtoStr,SHA_256));
            signatureComponent.add(requestDto.getDateTime());
            var signComponentStr = String.join(COLON_SEPARATOR,signatureComponent);
            var signature = signatureService.generateSignatureMacInstance(signComponentStr, credentialData.getSecretKey(), HMAC_SHA256);

            var mapHeaders = new HashMap<String,String>();
            mapHeaders.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            mapHeaders.put(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
            mapHeaders.put(X_CLIENT_KEY,credentialData.getApiKey());
            mapHeaders.put(X_TIMESTAMP, requestDto.getDateTime());
            mapHeaders.put(X_SIGNATURE, signature);

            log.info("WebClient Target POST {}", uri);
            log.info("WebClient RequestHeader {}", objectMapper.writeValueAsString(mapHeaders));
            log.info("WebClient RequestBody {}", requestDtoStr);
            requestContext.getTransactionLog().setLeg2Rrn(requestDto.getReferenceNumber());
            requestContext.getTransactionLog().setLeg2(requestDtoStr);
            var responseEntityStr = webClientAlto.post().uri(uri)
                .headers(httpHeaders -> httpHeaders.setAll(mapHeaders))
                .bodyValue(requestDtoStr)
                .exchangeToMono(clientResponse -> {
                    log.info("WebClient Original Response Status Code {}", clientResponse.statusCode().value());
                    return clientResponse.toEntity(String.class);
                })
                .block();
            assert responseEntityStr != null;
            log.info("WebClient ResponseHeader {}", objectMapper.writeValueAsString(responseEntityStr.getHeaders().toSingleValueMap()));
            log.info("WebClient ResponseBody {}", responseEntityStr.getBody());
            requestContext.getTransactionLog().setLeg3(responseEntityStr.getBody());

            var responseDto = objectMapper.readValue(responseEntityStr.getBody(),AltoPaymentRefundResponseDto.class);
            if (Objects.isNull(responseDto.getResponseCode())) {
                throw new NoSuchFieldException("Field response code is null");
            }
            requestContext.getTransactionLog().setLeg3Rc(responseDto.getResponseCode());
            return responseDto;
        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchFieldException e) {
            throw new WebClientGeneralException(e.getMessage());
        } catch (WebClientException e) {
            if (e.getCause() instanceof TimeoutException) {
                throw new WebClientResponseTimeoutException(e.getMessage());
            }
            if (e.getCause() instanceof ConnectException) {
                throw new WebClientConnectTimeoutException(e.getMessage());
            }
            throw new WebClientGeneralException(e.getMessage());
        }
    }
}
