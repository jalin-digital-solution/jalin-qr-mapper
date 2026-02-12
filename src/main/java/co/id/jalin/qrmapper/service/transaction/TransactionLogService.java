package co.id.jalin.qrmapper.service.transaction;

import co.id.jalin.qrmapper.entity.TransactionLog;
import co.id.jalin.qrmapper.repository.TransactionLogRepository;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final Tracer tracer;
    private final TransactionLogRepository transactionLogRepository;

    public void saveTransactionLogAsync(TransactionLog transactionLog){
        var currentSpan = tracer.currentSpan();
        CompletableFuture.runAsync(() -> {
            try (Tracer.SpanInScope ignored = tracer.withSpan(currentSpan)) {
                transactionLogRepository.save(transactionLog);
            }
        });
    }
}
