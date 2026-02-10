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
public class AltoPaymentCreditRequestDto {

    private String dateTime;
    private String referenceNumber;
    private String authorizationId;
    private String currencyCode;
    private BigDecimal amount;
    private BigDecimal fee;

    private String issuerNns;
    private String acquirerNns;
    private String nationalMid;
    private String additionalData;
    private String networkReferenceNumber;
    private String terminalLabel;
    private String transactionCode;

    private AltoMerchantDto merchant;
    private AltoCustomerDto customer;
}
