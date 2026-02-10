package co.id.jalin.qrmapper.context;

import co.id.jalin.qrmapper.entity.TransactionLog;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@Component
@RequestScope
public class RequestContext {

    private String traceId;
    private String requestBody;
    private TransactionLog transactionLog;
}
