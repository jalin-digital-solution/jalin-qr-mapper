package co.id.jalin.qrmapper.alto.configuration;

import co.id.jalin.qrmapper.alto.dto.AltoCustomerDto;
import co.id.jalin.qrmapper.alto.dto.AltoMerchantDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditResponseDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentStatusRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentStatusResponseDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentCheckRequestDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentCreditRequestDto;
import co.id.jalin.qrmapper.exception.PaymentNotFoundException;
import co.id.jalin.qrmapper.repository.TransactionLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static co.id.jalin.qrmapper.util.StringUtil.generateLocalRandomRrn;
import static co.id.jalin.qrmapper.util.StringUtil.generateLocalRandomStan;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class AltoMapperConfiguration {

    @Value("${api.path.alto.dana.context}")
    private String contextPathAlto;
    @Value("${api.path.alto.qr.payment}")
    private String paymentCreditPathAlto;

    private final ObjectMapper objectMapper;
    private final TransactionLogRepository transactionLogRepository;

    @Bean(name = "altoModelMapper")
    public ModelMapper altoModelMapper(){
        var altoModelMapper = new ModelMapper();

        // PAY CREDIT REQUEST ALT-JLN
        altoModelMapper
                .createTypeMap(AltoPaymentCreditRequestDto.class, PaymentCreditRequestDto.class)
                .setConverter(mappingContext -> {
                    var source = mappingContext.getSource();
                    var merchant = Optional.ofNullable(source.getMerchant()).orElse(new AltoMerchantDto());
                    var customer = Optional.ofNullable(source.getCustomer()).orElse(new AltoCustomerDto());
                    var dateTime = OffsetDateTime.parse(source.getDateTime());
                    return PaymentCreditRequestDto.builder()
                            .pan(merchant.getPan())
                            .processingCode(source.getTransactionCode())
                            .transactionAmount(source.getAmount())
                            .transmissionDateTime(dateTime.withOffsetSameInstant(ZoneOffset.UTC).format(ESB_DATETIME_FORMAT))
                            .systemTraceAuditNumber(generateLocalRandomStan())
                            .localTransactionDateTime(dateTime.format(ESB_DATETIME_FORMAT))
                            .settlementDate(dateTime.plusDays(SETTLEMENT_DAY_OFFSET).format(ESB_DATE_FORMAT))
                            .captureDate(dateTime.format(ESB_DATE_FORMAT))
                            .merchantType(merchant.getMcc())
                            .posEntryMode(DEFAULT_POS_ENTRY_MODE)
                            .feeType(FEE_TYPE_CREDIT)
                            .feeAmount(Optional.ofNullable(source.getFee()).orElse(BigDecimal.ZERO))
                            .acquirerId(source.getAcquirerNns())
                            .issuerId(source.getIssuerNns())
                            .forwardingId(DEFAULT_FORWARDING_ID)
                            .rrn(source.getReferenceNumber())
                            .approvalCode(source.getAuthorizationId())
                            .terminalId(source.getTerminalLabel())
                            .merchantId(merchant.getId())
                            .merchantName(merchant.getName())
                            .merchantCity(merchant.getCity())
                            .merchantCountry(merchant.getCountryCode())
                            .productIndicator(PI_Q001)
                            .customerData(customer.getName())
                            .merchantCriteria(merchant.getCriteria())
                            .currencyCode(CURRENCY_CODE.get(source.getCurrencyCode()))
                            .postalCode(merchant.getPostalCode())
                            .customerPan(customer.getPan())
                            .additionalField(source.getAdditionalData())
                            .build();
                });

        // PAY CREDIT DEFAULT RESPONSE
        altoModelMapper
                .createTypeMap(AltoPaymentCreditRequestDto.class, AltoPaymentCreditResponseDto.class)
                .setConverter(mappingContext -> {
                    var source = mappingContext.getSource();
                    return AltoPaymentCreditResponseDto.builder()
                            .responseCode(ALT_RESP_CODE_DO_NOT_HONOR)
                            .responseText(ALT_RESP_MESSAGE_DO_NOT_HONOR)
                            .referenceNumber(source.getReferenceNumber())
                            .networkReferenceNumber(generateLocalRandomRrn())
                            .invoiceNo(DEFAULT_INVOICE_NUMBER)
                            .currencyCode(source.getCurrencyCode())
                            .amount(source.getAmount())
                            .fee(source.getFee())
                            .build();
                });

        // PAY CHECK REQUEST ALT-JLN
        altoModelMapper
                .createTypeMap(AltoPaymentStatusRequestDto.class, PaymentCheckRequestDto.class)
                .setConverter(mappingContext -> {
                    try {
                        var source = mappingContext.getSource();
                        var transactionLog = transactionLogRepository.findFirstByLeg1RrnAndApiService(source.getReferenceNumber(),contextPathAlto+paymentCreditPathAlto).orElseThrow();
                        var esbPaymentCredit = objectMapper.readValue(transactionLog.getLeg2(), PaymentCreditRequestDto.class);
                        var dateTime = OffsetDateTime.parse(source.getDateTime());
                        return PaymentCheckRequestDto.builder()
                                .pan(esbPaymentCredit.getPan())
                                .processingCode(esbPaymentCredit.getProcessingCode().replace(PROC_CODE_26,PROC_CODE_36))
                                .transactionAmount(esbPaymentCredit.getTransactionAmount())
                                .transmissionDateTime(dateTime.withOffsetSameInstant(ZoneOffset.UTC).format(ESB_DATETIME_FORMAT))
                                .systemTraceAuditNumber(generateLocalRandomStan())
                                .localTransactionDateTime(esbPaymentCredit.getLocalTransactionDateTime())
                                .settlementDate(esbPaymentCredit.getSettlementDate())
                                .captureDate(esbPaymentCredit.getCaptureDate())
                                .merchantType(esbPaymentCredit.getMerchantType())
                                .posEntryMode(esbPaymentCredit.getPosEntryMode())
                                .feeType(esbPaymentCredit.getFeeType())
                                .feeAmount(esbPaymentCredit.getFeeAmount())
                                .acquirerId(esbPaymentCredit.getAcquirerId())
                                .issuerId(esbPaymentCredit.getIssuerId())
                                .forwardingId(esbPaymentCredit.getForwardingId())
                                .rrn(source.getReferenceNumber())
                                .approvalCode(esbPaymentCredit.getApprovalCode())
                                .terminalId(esbPaymentCredit.getTerminalId())
                                .merchantId(esbPaymentCredit.getMerchantId())
                                .merchantName(esbPaymentCredit.getMerchantName())
                                .merchantCity(esbPaymentCredit.getMerchantCity())
                                .merchantCountry(esbPaymentCredit.getMerchantCountry())
                                .productIndicator(esbPaymentCredit.getProductIndicator())
                                .customerData(esbPaymentCredit.getCustomerData())
                                .merchantCriteria(esbPaymentCredit.getMerchantCriteria())
                                .currencyCode(esbPaymentCredit.getCurrencyCode())
                                .postalCode(esbPaymentCredit.getPostalCode())
                                .customerPan(esbPaymentCredit.getCustomerPan())
                                .additionalField(esbPaymentCredit.getAdditionalField())
                                .build();
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
                        return null;
                    }
                });

        // PAY STATUS DEFAULT RESPONSE
        altoModelMapper
                .createTypeMap(AltoPaymentStatusRequestDto.class, AltoPaymentStatusResponseDto.class)
                .setConverter(mappingContext -> {
                    var source = mappingContext.getSource();
                    return AltoPaymentStatusResponseDto.builder()
                            .responseCode(ALT_RESP_CODE_DO_NOT_HONOR)
                            .responseText(ALT_RESP_MESSAGE_DO_NOT_HONOR)
                            .referenceNumber(source.getReferenceNumber())
                            .networkReferenceNumber(generateLocalRandomRrn())
                            .invoiceNo(DEFAULT_INVOICE_NUMBER)
                            .build();
                });

        return altoModelMapper;
    }
}
