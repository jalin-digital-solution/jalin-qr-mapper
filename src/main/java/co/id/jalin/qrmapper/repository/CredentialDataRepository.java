package co.id.jalin.qrmapper.repository;

import co.id.jalin.qrmapper.entity.CredentialData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialDataRepository extends JpaRepository<CredentialData, Long> {

    Optional<CredentialData> findByCredentialIdentifier(String credentialIdentifier);
}
