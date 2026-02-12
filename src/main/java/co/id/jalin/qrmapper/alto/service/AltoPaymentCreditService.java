package co.id.jalin.qrmapper.alto.service;

import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditResponseDto;
import co.id.jalin.qrmapper.cache.RcMappingManager;
import co.id.jalin.qrmapper.client.EsbRestClient;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.dto.transaction.PaymentCreditRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AltoPaymentCreditService {

    @Value("${api.path.esb.qr.payment}")
    private String apiPathPaymentCredit;
    @Value("${api.path.alto.qr.payment}")
    private String apiPathPaymentCreditAlto;

    private final RequestContext requestContext;
    private final ModelMapper altoModelMapper;
    private final EsbRestClient esbRestClient;
    private final RcMappingManager rcMappingManager;
    private final AltoValidationService altoValidationService;

    public AltoPaymentCreditResponseDto paymentCredit(Map<String, String> headers, AltoPaymentCreditRequestDto requestDto){
        requestContext.getTransactionLog().setAmount(requestDto.getAmount());
        requestContext.getTransactionLog().setLeg1Rrn(requestDto.getReferenceNumber());
        requestContext.getTransactionLog().setSwitchingRrn(requestDto.getNetworkReferenceNumber());
        requestContext.getTransactionLog().setAdditionalInfo(requestDto.getAdditionalData());

        altoValidationService.validateHeaders(headers,apiPathPaymentCreditAlto);

        var responseDto = altoModelMapper.map(requestDto, AltoPaymentCreditResponseDto.class);
        var esbRequestDto = altoModelMapper.map(requestDto, PaymentCreditRequestDto.class);
        var esbResponseDto = esbRestClient.sendPayment(esbRequestDto,apiPathPaymentCredit);
        var rcMapping = rcMappingManager.getRcMappingIMapJalinToDana().get(esbResponseDto.getResponseCode());
        if (Objects.nonNull(rcMapping)) {
            responseDto.setResponseCode(rcMapping.getOutputRc());
            responseDto.setResponseText(rcMapping.getOutputRcMessage());
            Optional.ofNullable(esbResponseDto.getResponseCode()).ifPresent(responseDto::setInvoiceNo);
        }
        requestContext.getTransactionLog().setLeg4Rc(responseDto.getResponseCode());
        requestContext.getTransactionLog().setInvoiceNumber(responseDto.getInvoiceNo());
        return responseDto;
    }
}
