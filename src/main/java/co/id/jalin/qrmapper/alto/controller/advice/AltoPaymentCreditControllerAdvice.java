package co.id.jalin.qrmapper.alto.controller.advice;

import co.id.jalin.qrmapper.alto.controller.AltoPaymentCreditController;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditResponseDto;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.exception.GeneralException;
import co.id.jalin.qrmapper.exception.HttpHeaderException;
import co.id.jalin.qrmapper.exception.WebClientConnectTimeoutException;
import co.id.jalin.qrmapper.exception.WebClientGeneralException;
import co.id.jalin.qrmapper.exception.WebClientResponseTimeoutException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.math.BigDecimal;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@RestControllerAdvice(assignableTypes = AltoPaymentCreditController.class)
@RequiredArgsConstructor
public class AltoPaymentCreditControllerAdvice {

    private final ObjectMapper objectMapper;
    private final ModelMapper altoModelMapper;
    private final RequestContext requestContext;

    @ExceptionHandler({GeneralException.class, WebClientGeneralException.class})
    public ResponseEntity<AltoPaymentCreditResponseDto> handlePaymentCredit(
            Exception exception,
            HttpServletRequest servletRequest
    ) {
        try {
            var requestBodyStr = requestContext.getRequestBody();
            var requestBody = objectMapper.readValue(requestBodyStr, AltoPaymentCreditRequestDto.class);
            var responseBody = altoModelMapper.map(requestBody, AltoPaymentCreditResponseDto.class);

            if (exception instanceof HttpHeaderException) {
                responseBody.setResponseCode(ALT_RESP_CODE_FORMAT_ERROR);
                responseBody.setResponseText(ALT_RESP_MESSAGE_FORMAT_ERROR);
            }
            if (exception instanceof WebClientResponseTimeoutException) {
                responseBody.setResponseCode(ALT_RESP_CODE_TIMEOUT);
                responseBody.setResponseText(ALT_RESP_MESSAGE_TIMEOUT);
            }
            if (exception instanceof WebClientConnectTimeoutException) {
                responseBody.setResponseCode(ALT_RESP_CODE_SYSTEM_MALFUNCTION);
                responseBody.setResponseText(ALT_RESP_MESSAGE_SYSTEM_MALFUNCTION);
            }

            log.error("Error final log {}", servletRequest.getServletPath(), exception);
            return ResponseEntity.badRequest()
                    .body(responseBody);
        } catch (IOException e) {
            log.error("Error handle advice", exception);
            return ResponseEntity.internalServerError()
                    .body(buildErrorResponseBody());
        }
    }

    private AltoPaymentCreditResponseDto buildErrorResponseBody(){
        return AltoPaymentCreditResponseDto.builder()
                .responseCode(ALT_RESP_CODE_DO_NOT_HONOR)
                .responseText(ALT_RESP_MESSAGE_DO_NOT_HONOR)
                .referenceNumber(DEFAULT_RRN)
                .networkReferenceNumber(DEFAULT_RRN)
                .invoiceNo(DEFAULT_INVOICE_NUMBER)
                .currencyCode(ALT_CC_IDR)
                .amount(BigDecimal.ZERO)
                .fee(BigDecimal.ZERO)
                .build();
    }
}
