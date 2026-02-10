package co.id.jalin.qrmapper.dto.transaction;

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
public class PaymentCreditRequestDto {

    private String pan;
    private String processingCode;
    private BigDecimal transactionAmount;
    private String transmissionDateTime;
    private String systemTraceAuditNumber;
    private String localTransactionDateTime;
    private String settlementDate;
    private String captureDate;
    private String merchantType;
    private String posEntryMode;

    private String feeType;
    private BigDecimal feeAmount;

    private String acquirerId;
    private String issuerId;
    private String forwardingId;
    private String rrn;
    private String approvalCode;
    private String terminalId;

    private String merchantId;
    private String merchantName;
    private String merchantCity;
    private String merchantCountry;
    private String productIndicator;
    private String customerData;
    private String merchantCriteria;
    private String currencyCode;
    private String postalCode;

    private String additionalField;
    private String customerPan;
}
