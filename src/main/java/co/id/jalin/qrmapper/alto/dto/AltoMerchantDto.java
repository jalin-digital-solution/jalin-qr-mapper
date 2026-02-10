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
public class AltoMerchantDto {

    private String pan;
    private String id;
    private String criteria;
    private String name;
    private String city;
    private String mcc;
    private String postalCode;
    private String countryCode;
}
