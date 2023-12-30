package fr.univrouen.deliverysystem.driver;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Long>, JpaSpecificationExecutor<DriverEntity> {

    Optional<DriverEntity> findByPublicId(String publicId);

    Optional<DriverEntity> findByName(String name);

    List<DriverEntity> findAll();

    Page<DriverEntity> findAll(Pageable pageable);

    Page<DriverEntity> findAll(Specification<DriverEntity> specification, Pageable pageable);

    Page<DriverEntity> findByIsAvailable(boolean isAvailable, Pageable pageable);

    Page<DriverEntity> findByCreatedAtAfterAndIsAvailable(Instant createdAt, boolean isAvailable, Pageable pageable);

    Page<DriverEntity> findByCreatedAtBeforeAndIsAvailable(Instant createdAt, boolean isAvailable, Pageable pageable);

    Page<DriverEntity> findByCreatedAtBetweenAndIsAvailable(Instant createdAtStart, Instant createdAtEnd,
            boolean isAvailable, Pageable pageable);

    Long deleteByPublicId(String publicId);

}
