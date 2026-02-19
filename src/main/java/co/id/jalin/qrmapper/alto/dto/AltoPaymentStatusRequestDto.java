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
public class AltoPaymentStatusRequestDto {

    private String dateTime;
    private String referenceNumber;
    private String issuerNns;
    private String acquirerNns;
    private String transactionDateTime;
}
