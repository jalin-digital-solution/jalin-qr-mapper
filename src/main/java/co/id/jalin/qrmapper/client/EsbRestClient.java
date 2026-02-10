package co.id.jalin.qrmapper.client;

import co.id.jalin.qrmapper.cache.CredentialDataManager;
import co.id.jalin.qrmapper.dto.transaction.PaymentCreditRequestDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentCreditResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.ConnectException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;

import static co.id.jalin.qrmapper.util.StringUtil.buildCredDataByCredIdKey;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class EsbRestClient {

    @Value("${esb.base.url}")
    private String esbBaseUrl;

    private final WebClient webClientEsb;
    private final ObjectMapper objectMapper;
    private final SignatureService signatureService;
    private final CredentialDataManager credentialDataManager;

    public PaymentCreditResponseDto sendPayment(
            PaymentCreditRequestDto requestDto,
            String credentialIdentifier,
            String apiPathPayment
    ) {
        try {
            var credentialData = credentialDataManager
                    .getCredDataByCredId(buildCredDataByCredIdKey(credentialIdentifier,apiPathPayment))
                    .orElseThrow(() -> new WebClientGeneralException("Credential data destination is not set with identifier "+ credentialIdentifier));
            var uri = UriComponentsBuilder.fromUriString(esbBaseUrl + apiPathPayment)
                .queryParam(VAR_USER, credentialData.getUsername())
                .queryParam(VAR_PASS, credentialData.getPassword())
                .toUriString();
            var mapHeaders = new HashMap<String,String>();
            mapHeaders.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            mapHeaders.put(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
            mapHeaders.put(MAC,signatureService.generateSignatureMacInstance(objectMapper.writeValueAsString(requestDto),credentialData.getSecretKey(),HMAC_SHA256));

            log.info("WebClient Target POST {}", uri);
            log.info("WebClient RequestHeader {}", objectMapper.writeValueAsString(mapHeaders));
            log.info("WebClient RequestBody {}", objectMapper.writeValueAsString(requestDto));
            var responseEntityStr = webClientEsb.post().uri(uri)
                .headers(httpHeaders -> httpHeaders.setAll(mapHeaders))
                .bodyValue(requestDto)
                .exchangeToMono(clientResponse -> {
                    log.info("WebClient Original Response Status Code {}", clientResponse.statusCode().value());
                    return clientResponse.toEntity(String.class);
                })
                .block();
            assert responseEntityStr != null;
            log.info("WebClient ResponseHeader {}", objectMapper.writeValueAsString(responseEntityStr.getHeaders().toSingleValueMap()));
            log.info("WebClient ResponseBody {}", responseEntityStr.getBody());

            var responseDto = objectMapper.readValue(responseEntityStr.getBody(),PaymentCreditResponseDto.class);
            if (Objects.isNull(responseDto.getResponseCode())) {
                throw new NoSuchFieldException("Field response code is null");
            }
            return responseDto;
        } catch (JsonProcessingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchFieldException e) {
            throw new WebClientGeneralException(e.getMessage());
        } catch (WebClientException e) {
            if (e.getCause() instanceof TimeoutException) {
                throw new WebClientResponseTimeoutException(e.getMessage());
            }
            if (e.getCause() instanceof ConnectException) {
                throw new WebClientResponseTimeoutException(e.getMessage());
            }
            throw new WebClientGeneralException(e.getMessage());
        }
    }
}
