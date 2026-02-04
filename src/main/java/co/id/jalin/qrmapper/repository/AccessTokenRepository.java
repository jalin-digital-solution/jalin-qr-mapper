package co.id.jalin.qrmapper.repository;

import co.id.jalin.qrmapper.entity.AccessToken;
import co.id.jalin.qrmapper.entity.AccessTokenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends JpaRepository<AccessToken, AccessTokenId> {

    Optional<AccessToken> findByCredentialIdentifier(String credentialIdentifier);
}
