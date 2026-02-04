package co.id.jalin.qrmapper.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataCacheDto {

    private String key;
    private Object object;

    private String responseCode;
    private String responseMessage;
}
