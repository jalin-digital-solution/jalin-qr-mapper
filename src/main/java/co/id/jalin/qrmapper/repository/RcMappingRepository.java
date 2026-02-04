package co.id.jalin.qrmapper.repository;

import co.id.jalin.qrmapper.entity.RcMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RcMappingRepository extends JpaRepository<RcMapping, Long> {

    Optional<RcMapping> findByServiceCodeAndInputRc(
            String serviceCode,
            String inputRc
    );
}
