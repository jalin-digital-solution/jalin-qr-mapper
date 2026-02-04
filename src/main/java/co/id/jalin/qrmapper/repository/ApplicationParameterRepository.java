package co.id.jalin.qrmapper.repository;

import co.id.jalin.qrmapper.entity.ApplicationParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationParameterRepository extends JpaRepository<ApplicationParameter, Long> {

    Optional<ApplicationParameter> findByName(String name);
}
