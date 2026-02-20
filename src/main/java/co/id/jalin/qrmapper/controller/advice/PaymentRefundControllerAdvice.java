package co.id.jalin.qrmapper.controller.advice;

import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.controller.transaction.PaymentRefundController;
import co.id.jalin.qrmapper.dto.transaction.PaymentRefundResponseDto;
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
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Order(5)
@RestControllerAdvice(assignableTypes = PaymentRefundController.class)
@RequiredArgsConstructor
public class PaymentRefundControllerAdvice {

    private final ObjectMapper objectMapper;
    private final ModelMapper altoModelMapper;
    private final RequestContext requestContext;

    @ExceptionHandler({GeneralException.class, WebClientGeneralException.class})
    public ResponseEntity<PaymentRefundResponseDto> handlePaymentRefund(
            Exception exception,
            HttpServletRequest servletRequest
    ) {
        try {
            var responseBody = PaymentRefundResponseDto.builder().responseCode(RESP_CODE_DO_NOT_HONOR).authorizationCode(DEFAULT_APPROVAL_CODE).build();

            switch (exception) {
                case HttpHeaderException ignored:
                    responseBody.setResponseCode(RESP_CODE_FORMAT_ERROR);
                    break;
                case WebClientConnectTimeoutException ignored:
                    responseBody.setResponseCode(RESP_CODE_SYSTEM_MALFUNCTION);
                    break;
                case WebClientResponseTimeoutException ignored:
                    responseBody.setResponseCode(RESP_CODE_TIMEOUT);
                    break;
                default:
                    log.info("Unmap response code by exception!");
            }

            requestContext.getTransactionLog().setLeg4Rc(responseBody.getResponseCode());
            requestContext.getTransactionLog().setInvoiceNumber(responseBody.getAuthorizationCode());
            log.error("Error final log {}", servletRequest.getServletPath(), exception);
            return ResponseEntity.badRequest().body(responseBody);
        } catch (Exception e) {
            log.error("Error handle at advice", exception);
            return ResponseEntity.internalServerError()
                    .body(buildErrorResponseBody());
        }
    }

    private PaymentRefundResponseDto buildErrorResponseBody(){
        return PaymentRefundResponseDto.builder()
                .responseCode(RESP_CODE_SYSTEM_MALFUNCTION)
                .authorizationCode(DEFAULT_APPROVAL_CODE)
                .build();
    }
}
