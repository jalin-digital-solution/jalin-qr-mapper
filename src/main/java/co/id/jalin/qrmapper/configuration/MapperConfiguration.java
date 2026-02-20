package co.id.jalin.qrmapper.configuration;

import co.id.jalin.qrmapper.alto.dto.AltoCustomerDto;
import co.id.jalin.qrmapper.alto.dto.AltoMerchantDto;
import co.id.jalin.qrmapper.alto.dto.AltoPaymentRefundRequestDto;
import co.id.jalin.qrmapper.dto.transaction.PaymentRefundRequestDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;

@Configuration
public class MapperConfiguration {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean(name = "esbModelMapper")
    public ModelMapper esbModelMapper(){
        var esbModelMapper = new ModelMapper();
        esbModelMapper.getConfiguration().setAmbiguityIgnored(true);

        // PAY REFUND REQUEST JLN-ALT
        esbModelMapper
                .createTypeMap(PaymentRefundRequestDto.class, AltoPaymentRefundRequestDto.class)
                .setConverter(mappingContext -> {
                    var source = mappingContext.getSource();
                    var dateTime = LocalDateTime.parse(source.getTransmissionDateTime(), ESB_DATETIME_FORMAT);
                    var accountType = ACCOUNT_TYPE_CODE_TO_DESC.get(source.getProcessingCode().substring(2,4));
                    if (accountType == null) accountType = "UNSPECIFIED";
                    return AltoPaymentRefundRequestDto.builder()
                            .dateTime(dateTime.atZone(ZONE_ID_JAKARTA).format(ALTO_DATETIME_FORMAT))
                            .referenceNumber(source.getRrn())
                            .invoiceNo(source.getInvoiceNumber())
                            .currencyCode(CURRENCY_CODE_NUM_TO_ALPHA.get(source.getCurrencyCode()))
                            .amountRefund(source.getTransactionAmount())
                            .issuerNns(source.getIssuerId())
                            .acquirerNns(source.getAcquirerId())
                            .transactionCode(source.getProcessingCode())
                            .merchant(
                                    AltoMerchantDto.builder()
                                            .pan(source.getMerchantPan())
                                            .id(source.getMerchantId())
                                            .criteria(source.getMerchantCriteria())
                                            .name(source.getMerchantName())
                                            .city(source.getMerchantCity())
                                            .mcc(source.getMerchantType())
                                            .postalCode(source.getPostalCode())
                                            .countryCode(source.getMerchantCountry())
                                            .build()
                            )
                            .customer(
                                    AltoCustomerDto.builder()
                                            .pan(source.getPan())
                                            .name(source.getCustomerData())
                                            .accountType(accountType)
                                            .build()
                            )
                            .build();
                });

        return esbModelMapper;
    }
}
