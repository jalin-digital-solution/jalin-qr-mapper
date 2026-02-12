package co.id.jalin.qrmapper.repository;

import co.id.jalin.qrmapper.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long>, JpaSpecificationExecutor<TransactionLog> {

    Optional<TransactionLog> findFirstByLeg1RrnAndApiService(
            String leg1Rrn,
            String apiService
    );

    Optional<TransactionLog> findFirstBySwitchingRrnAndApiService(
            String switchingRrn,
            String apiService
    );
}