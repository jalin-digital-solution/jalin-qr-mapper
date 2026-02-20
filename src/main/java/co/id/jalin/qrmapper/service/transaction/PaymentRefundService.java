package co.id.jalin.qrmapper.service.transaction;

import co.id.jalin.qrmapper.alto.client.AltoRestClient;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentRefundRequestDto;
import co.id.jalin.qrmapper.cache.RcMappingManager;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.dto.transaction.PaymentRefundRequestDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentRefundResponseDto;
import co.id.jalin.qrmapper.exception.HttpHeaderException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class PaymentRefundService {

    @Value("${api.path.alto.qr.refund}")
    private String apiPathPaymentRefundAlto;

    private final RequestContext requestContext;
    private final ModelMapper esbModelMapper;
    private final AltoRestClient altoRestClient;
    private final RcMappingManager rcMappingManager;

    public PaymentRefundResponseDto paymentRefund(Map<String, String> headers, PaymentRefundRequestDto requestDto){
        requestContext.getTransactionLog().setAmount(requestDto.getTransactionAmount());
        requestContext.getTransactionLog().setLeg1Rrn(requestDto.getRrn());
        requestContext.getTransactionLog().setAdditionalInfo(requestDto.getAdditionalField());

        if (headers.get(MAC) == null) {
            throw new HttpHeaderException("Signature in mac is not exist");
        }

        var responseDto = PaymentRefundResponseDto.builder().responseCode(RESP_CODE_DO_NOT_HONOR).authorizationCode(DEFAULT_APPROVAL_CODE).build();
        var altoRequestDto = esbModelMapper.map(requestDto, AltoPaymentRefundRequestDto.class);
        var altoResponseDto = altoRestClient.sendPayment(altoRequestDto,apiPathPaymentRefundAlto);
        var rcMapping = rcMappingManager.getRcMappingIMapDanaToJalin().get(altoResponseDto.getResponseCode());
        if (Objects.nonNull(rcMapping)) {
            responseDto.setResponseCode(rcMapping.getOutputRc());
            Optional
                    .ofNullable(altoResponseDto.getResponseCode())
                    .ifPresent(
                            s -> responseDto.setAuthorizationCode(altoResponseDto.getAuthorizationId())
                    );
        }
        requestContext.getTransactionLog().setLeg4Rc(responseDto.getResponseCode());
        requestContext.getTransactionLog().setInvoiceNumber(responseDto.getAuthorizationCode());
        return responseDto;
    }
}
