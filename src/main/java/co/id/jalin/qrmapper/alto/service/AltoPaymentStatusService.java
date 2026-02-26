package co.id.jalin.qrmapper.alto.service;

import co.id.jalin.qrmapper.alto.dto.AltoPaymentStatusRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentStatusResponseDto;
import co.id.jalin.qrmapper.aspect.annotation.LogExecutionTime;
import co.id.jalin.qrmapper.cache.RcMappingManager;
import co.id.jalin.qrmapper.client.EsbRestClient;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.dto.transaction.PaymentCheckRequestDto;
import co.id.jalin.qrmapper.exception.PaymentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class AltoPaymentStatusService {

    @Value("${api.path.esb.qr.check}")
    private String apiPathPaymentCheck;
    @Value("${api.path.alto.qr.status}")
    private String apiPathPaymentStatusAlto;

    private final RequestContext requestContext;
    private final ModelMapper altoModelMapper;
    private final EsbRestClient esbRestClient;
    private final RcMappingManager rcMappingManager;
    private final AltoValidationService altoValidationService;

    @LogExecutionTime
//    @Observed(name = "payment.processing", contextualName = "payment-status-process")
    public AltoPaymentStatusResponseDto paymentStatus(Map<String, String> headers, AltoPaymentStatusRequestDto requestDto){
        requestContext.getTransactionLog().setLeg1Rrn(requestDto.getReferenceNumber());

        altoValidationService.validateHeaders(headers,apiPathPaymentStatusAlto);

        var responseDto = altoModelMapper.map(requestDto, AltoPaymentStatusResponseDto.class);
        var esbRequestDto = altoModelMapper.map(requestDto, PaymentCheckRequestDto.class);
        if (Objects.isNull(esbRequestDto)) {
            throw new PaymentNotFoundException("Error when map payment check status, trx by rrn can be not found");
        }

        requestContext.getTransactionLog().setAmount(esbRequestDto.getTransactionAmount());
        requestContext.getTransactionLog().setSwitchingRrn(responseDto.getNetworkReferenceNumber());
        requestContext.getTransactionLog().setAdditionalInfo(esbRequestDto.getAdditionalField());

        var esbResponseDto = esbRestClient.sendPayment(esbRequestDto,apiPathPaymentCheck);
        var rcMapping = rcMappingManager.getRcMappingIMapJalinToDana().get(esbResponseDto.getResponseCode());
        responseDto.setResponseCode(ALT_RESP_CODE_SUCCESS);
        responseDto.setResponseText(ALT_RESP_MESSAGE_SUCCESS);
        responseDto.setTransactionResponseCode(ALT_RESP_CODE_DO_NOT_HONOR);
        responseDto.setTransactionStatus(ALT_RESP_MESSAGE_DO_NOT_HONOR);
        if (Objects.nonNull(rcMapping)) {
            responseDto.setTransactionResponseCode(rcMapping.getOutputRc());
            responseDto.setTransactionStatus(rcMapping.getOutputRcMessage());
            Optional
                    .ofNullable(esbResponseDto.getResponseCode())
                    .ifPresent(
                            s -> responseDto.setInvoiceNo(esbResponseDto.getInvoiceNumber())
                    );
        }
        if (!List.of(ALT_RESP_CODE_SUCCESS,ALT_RESP_CODE_PAYMENT_FAIL).contains(responseDto.getTransactionResponseCode())) {
            responseDto.setTransactionStatus(responseDto.getTransactionStatus() + SPACE_STRING + ALT_RESP_MESSAGE_SUSPECT);
        }
        requestContext.getTransactionLog().setLeg4Rc(responseDto.getResponseCode());
        requestContext.getTransactionLog().setInvoiceNumber(responseDto.getInvoiceNo());
        return responseDto;
    }
}
