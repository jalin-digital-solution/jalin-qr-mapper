package co.id.jalin.qrmapper.client;

import co.id.jalin.qrmapper.aspect.annotation.LogExecutionTime;
import co.id.jalin.qrmapper.cache.CredentialDataManager;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.dto.transaction.PaymentCreditResponseDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentRequestDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentResponseDto;
import co.id.jalin.qrmapper.entity.CredentialData;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.ConnectException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static co.id.jalin.qrmapper.util.StringUtil.buildCredDataByCredIdKey;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class EsbRestClient {

    @Value("${esb.base.url}")
    private String esbBaseUrl;

    private final RequestContext requestContext;
    private final WebClient webClientEsb;
    private final RestTemplate restTemplateEsb;
    private final ObjectMapper objectMapper;
    private final SignatureService signatureService;
    private final CredentialDataManager credentialDataManager;

    @LogExecutionTime
//    @Observed(name = "webClient.processing", contextualName = "all-outgoing-request-process")
    public PaymentResponseDto sendPayment(
            PaymentRequestDto requestDto,
            String apiPathPayment
    ) {
        try {
            var credentialData = getCredentialData(requestDto,apiPathPayment);
            var uri = uriBuilder(credentialData,apiPathPayment);
            var mapHeaders = headerBuilder(requestDto,credentialData);

            var requestDtoStr = objectMapper.writeValueAsString(requestDto);
            var headerStr = objectMapper.writeValueAsString(mapHeaders);
            logRequest(uri,headerStr,requestDtoStr);
            requestContext.getTransactionLog().setLeg2Rrn(requestDto.getRrn());
            requestContext.getTransactionLog().setLeg2(requestDtoStr);
//            var responseEntityStr = dummyResponse();
            var responseEntityStr = sendWebClient(uri,mapHeaders,requestDto);
            assert responseEntityStr != null;
//            var responseEntityStr = sendRestTemplate(uri,mapHeaders,requestDto);
            logResponse(objectMapper.writeValueAsString(responseEntityStr.getHeaders().toSingleValueMap()),responseEntityStr.getBody());
            requestContext.getTransactionLog().setLeg3(responseEntityStr.getBody());

            var responseDto = parseResponse(responseEntityStr);
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

    private CredentialData getCredentialData(PaymentRequestDto requestDto, String apiPathPayment){
        return credentialDataManager
                .getCredDataByCredId(buildCredDataByCredIdKey(requestDto.getIssuerId(),apiPathPayment))
                .orElseThrow(() -> new WebClientGeneralException("Credential data destination is not set with identifier "+ requestDto.getIssuerId()));
    }

    private String uriBuilder(CredentialData credentialData, String apiPathPayment){
        return UriComponentsBuilder.fromUriString(esbBaseUrl + apiPathPayment)
                .queryParam(VAR_USER, credentialData.getUsername())
                .queryParam(VAR_PASS, credentialData.getPassword())
                .toUriString();
    }

    private Map<String,String> headerBuilder(PaymentRequestDto requestDto, CredentialData credentialData) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        var mapHeaders = new HashMap<String,String>();
        mapHeaders.put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        mapHeaders.put(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        mapHeaders.put(MAC,signatureService.generateSignatureMacInstance(objectMapper.writeValueAsString(requestDto),credentialData.getSecretKey(),HMAC_SHA256));
        return mapHeaders;
    }

    private void logRequest(String uri, String header, String requestDto){
        log.info("WebClient Target POST {}", uri);
        log.info("WebClient RequestHeader {}", header);
        log.info("WebClient RequestBody {}", requestDto);
    }

    private void logResponse(String header, String responseDto){
        log.info("WebClient ResponseHeader {}", header);
        log.info("WebClient ResponseBody {}", responseDto);
    }

    private ResponseEntity<String> sendWebClient(String uri, Map<String,String> mapHeaders, PaymentRequestDto requestDto){
        return webClientEsb.post().uri(uri)
                .headers(httpHeaders -> httpHeaders.setAll(mapHeaders))
                .bodyValue(requestDto)
                .exchangeToMono(clientResponse -> {
                    log.info("WebClient Original Response Status Code {}", clientResponse.statusCode().value());
                    return clientResponse.toEntity(String.class);
                })
                .block();
    }

        private ResponseEntity<String> sendRestTemplate(String uri, Map<String, String> mapHeaders, PaymentRequestDto requestDto) {
            HttpHeaders headers = new HttpHeaders();
            headers.setAll(mapHeaders);
            HttpEntity<PaymentRequestDto> requestEntity = new HttpEntity<>(requestDto, headers);
            ResponseEntity<String> response = restTemplateEsb.exchange(uri,HttpMethod.POST,requestEntity,String.class);
            log.info("RestTemplate Response Status Code {}", response.getStatusCode().value());
            return response;
        }

    private PaymentResponseDto parseResponse(ResponseEntity<String> responseEntityStr) throws JsonProcessingException, NoSuchFieldException {
        var responseDto = objectMapper.readValue(responseEntityStr.getBody(),PaymentCreditResponseDto.class);
        if (Objects.isNull(responseDto.getResponseCode())) {
            throw new NoSuchFieldException("Field response code is null");
        }
        return responseDto;
    }

    private ResponseEntity<String> dummyResponse() throws JsonProcessingException {
        return ResponseEntity.ok(
                objectMapper.writeValueAsString(
                        PaymentResponseDto.builder()
                                .responseCode(RESP_CODE_SUCCESS)
                                .invoiceNumber("12345678901234567890")
                                .build()
                )
        );
    }
}
