package fr.univrouen.deliverysystem.round;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoundRepository
                extends JpaRepository<RoundEntity, Long>, JpaSpecificationExecutor<RoundEntity> {

        Optional<RoundEntity> findByPublicId(String publicId);

        Optional<RoundEntity> findByName(String name);

        List<RoundEntity> findAll();

        Page<RoundEntity> findAll(Pageable pageable);

        Page<RoundEntity> findAll(Specification<RoundEntity> specification, Pageable pageable);

        @Query("SELECT r FROM RoundEntity r WHERE r.driver.id = :driver_id AND ((r.startDate >= :x_start_date AND :x_start_date <= r.endDate) OR (r.startDate >= :x_end_date AND :x_end_date <= r.endDate))")
        List<RoundEntity> findByDriverIdAndStartDateOrEndDate(
                        @Param("driver_id") Long driverId,
                        @Param("x_start_date") Instant startDate,
                        @Param("x_end_date") Instant endDate);

        Long deleteByPublicId(String publicId);

}
