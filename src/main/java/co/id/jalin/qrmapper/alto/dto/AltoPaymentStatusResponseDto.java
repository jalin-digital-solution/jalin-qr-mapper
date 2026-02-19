package co.id.jalin.qrmapper.alto.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AltoPaymentStatusResponseDto {

    private String responseCode;
    private String responseText;

    private String transactionResponseCode;
    private String transactionStatus;

    private String referenceNumber;
    private String networkReferenceNumber;
    private String invoiceNo;
}
