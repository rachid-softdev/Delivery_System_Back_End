package fr.univrouen.deliverysystem.delivery;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository
        extends JpaRepository<DeliveryEntity, Long>, JpaSpecificationExecutor<DeliveryEntity> {

    Optional<DeliveryEntity> findByPublicId(String publicId);

    List<DeliveryEntity> findAll();

    Page<DeliveryEntity> findAll(Pageable pageable);

    Page<DeliveryEntity> findAll(Specification<DeliveryEntity> specification, Pageable pageable);

    Long deleteByPublicId(String publicId);

}
