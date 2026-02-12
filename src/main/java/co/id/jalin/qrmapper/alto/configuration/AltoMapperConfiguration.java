package co.id.jalin.qrmapper.alto.configuration;

import co.id.jalin.qrmapper.alto.dto.AltoCustomerDto;
import co.id.jalin.qrmapper.alto.dto.AltoMerchantDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditRequestDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentCreditResponseDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentCreditRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static co.id.jalin.qrmapper.util.StringUtil.generateLocalRandomRrn;
import static co.id.jalin.qrmapper.util.StringUtil.generateLocalRandomStan;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Configuration
public class AltoMapperConfiguration {

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
                            .networkReferenceNumber(source.getNetworkReferenceNumber())
                            .invoiceNo(DEFAULT_INVOICE_NUMBER)
                            .currencyCode(source.getCurrencyCode())
                            .amount(source.getAmount())
                            .fee(source.getFee())
                            .build();
                });

        return altoModelMapper;
    }
}
