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
public class AltoPaymentRefundResponseDto {

    private String responseCode;
    private String responseText;

    private String referenceNumber;
    private String invoiceNoRefund;
    private String authorizationId;
}
