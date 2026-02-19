package co.id.jalin.qrmapper.alto.controller.advice;

import co.id.jalin.qrmapper.alto.controller.AltoPaymentStatusController;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentStatusRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentStatusResponseDto;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.exception.GeneralException;
import co.id.jalin.qrmapper.exception.HttpHeaderException;
import co.id.jalin.qrmapper.exception.PaymentNotFoundException;
import co.id.jalin.qrmapper.exception.WebClientConnectTimeoutException;
import co.id.jalin.qrmapper.exception.WebClientGeneralException;
import co.id.jalin.qrmapper.exception.WebClientResponseTimeoutException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Order(5)
@RestControllerAdvice(assignableTypes = AltoPaymentStatusController.class)
@RequiredArgsConstructor
public class AltoPaymentStatusControllerAdvice {

    private final ObjectMapper objectMapper;
    private final ModelMapper altoModelMapper;
    private final RequestContext requestContext;

    @ExceptionHandler({GeneralException.class, WebClientGeneralException.class})
    public ResponseEntity<AltoPaymentStatusResponseDto> handlePaymentStatus(
            Exception exception,
            HttpServletRequest servletRequest
    ) {
        try {
            var requestBodyStr = requestContext.getRequestBody();
            var requestBody = objectMapper.readValue(requestBodyStr, AltoPaymentStatusRequestDto.class);
            var responseBody = altoModelMapper.map(requestBody, AltoPaymentStatusResponseDto.class);

            switch (exception) {
                case HttpHeaderException ignored:
                    responseBody.setResponseCode(ALT_RESP_CODE_FORMAT_ERROR);
                    responseBody.setResponseText(ALT_RESP_MESSAGE_FORMAT_ERROR);
                    break;
                case PaymentNotFoundException ignored:
                    responseBody.setResponseCode(ALT_RESP_CODE_PAYMENT_NOT_FOUND);
                    responseBody.setResponseText(ALT_RESP_MESSAGE_PAYMENT_NOT_FOUND);
                    break;
                case WebClientConnectTimeoutException ignored:
                    responseBody.setResponseCode(ALT_RESP_CODE_SYSTEM_MALFUNCTION);
                    responseBody.setResponseText(ALT_RESP_MESSAGE_SYSTEM_MALFUNCTION);
                    break;
                case WebClientResponseTimeoutException ignored:
                    responseBody.setResponseCode(ALT_RESP_CODE_TIMEOUT);
                    responseBody.setResponseText(ALT_RESP_MESSAGE_TIMEOUT);
                    break;
                default:
                    log.info("Unmap response code by exception!");
            }
            requestContext.getTransactionLog().setLeg4Rc(responseBody.getResponseCode());
            requestContext.getTransactionLog().setInvoiceNumber(responseBody.getInvoiceNo());
            log.error("Error final log {}", servletRequest.getServletPath(), exception);
            return ResponseEntity.badRequest().body(responseBody);
        } catch (IOException e) {
            log.error("Error handle at advice", exception);
            return ResponseEntity.internalServerError()
                    .body(buildErrorResponseBody());
        }
    }

    private AltoPaymentStatusResponseDto buildErrorResponseBody(){
        return AltoPaymentStatusResponseDto.builder()
                .responseCode(ALT_RESP_CODE_SYSTEM_MALFUNCTION)
                .responseText(ALT_RESP_MESSAGE_SYSTEM_MALFUNCTION)
                .referenceNumber(DEFAULT_RRN)
                .networkReferenceNumber(DEFAULT_RRN)
                .invoiceNo(DEFAULT_INVOICE_NUMBER)
                .build();
    }
}
