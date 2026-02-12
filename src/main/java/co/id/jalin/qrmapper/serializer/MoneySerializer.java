package co.id.jalin.qrmapper.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.MONEY_DECIMAL_SCALE;

@Component
public class MoneySerializer extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeNumber(value.setScale(MONEY_DECIMAL_SCALE, RoundingMode.HALF_EVEN).toString());
    }
}