package co.id.jalin.qrmapper.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@IdClass(AccessTokenId.class)
@Table(name = "access_token")
public class AccessToken {

    @Id
    @Column(name = "credential_identifier", length = 1000, nullable = false)
    private String credentialIdentifier;
    @Id
    @Column(name = "access_token", length = 1000, nullable = false)
    private String accessToken;

    private String tokenType;
    private String tokenMode;
    private Long expiresIn;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
