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
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "transaction_log",
        indexes = {
                @Index(
                        name = "transaction_log_leg1_rrn_idx",
                        columnList = "leg1_rrn, api_service"
                )
        }
)
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String traceId;
    private String apiService;

    @Column(name = "leg1_rrn")
    private String leg1Rrn;
    @Column(name = "leg2_rrn")
    private String leg2Rrn;

    @Column(name = "leg3_rc")
    private String leg3Rc;
    @Column(name = "leg4_rc")
    private String leg4Rc;

    private String invoiceNumber;
    private String switchingRrn;
    private String additionalInfo;
    @Column(precision = 20, scale = 2)
    private BigDecimal amount;

    @Column(name = "leg1", columnDefinition = "TEXT")
    private String leg1;
    @Column(name = "leg2", columnDefinition = "TEXT")
    private String leg2;
    @Column(name = "leg3", columnDefinition = "TEXT")
    private String leg3;
    @Column(name = "leg4", columnDefinition = "TEXT")
    private String leg4;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
