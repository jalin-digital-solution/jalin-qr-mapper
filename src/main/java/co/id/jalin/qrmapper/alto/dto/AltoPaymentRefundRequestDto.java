package co.id.jalin.qrmapper.alto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AltoPaymentRefundRequestDto {

    private String dateTime;
    private String referenceNumber;
    private String invoiceNo;
    private String currencyCode;
    private BigDecimal amountRefund;

    private String issuerNns;
    private String acquirerNns;
    private String transactionCode;

    private AltoMerchantDto merchant;
    private AltoCustomerDto customer;
}
