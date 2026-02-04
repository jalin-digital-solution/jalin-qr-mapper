package co.id.jalin.qrmapper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "transaction_log",
        schema = "qrpayment",
        indexes = {
                @Index(
                        name = "transaction_log_issuer_rrn_idx",
                        columnList = "issuer_rrn, api_service"
                )
        }
)
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String traceId;
    private String apiService;

    private String issuerRc;
    private String acquirerRc;
    private String issuerRrn;
    private String acquirerRrn;
    private String switchingRrn;
    @Column(precision = 20, scale = 2)
    private BigDecimal amount;

    private String issuerBodyRequest;
    private String issuerBodyResponse;
    private String acquirerBodyRequest;
    private String acquirerBodyResponse;

    private String additionalInfo;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
