package fr.univrouen.deliverysystem.round;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

public class RoundFilterSpecification {

    public static Specification<RoundEntity> filterBy(RoundFilter filter) {
        return filter == null ? Specification.where(null)
                : Specification.where(hasSearchDate(filter.getSearchDate()));
    }

    private static Specification<RoundEntity> hasSearchDate(Instant searchDate) {
        return (root, query, cb) -> searchDate == null ? cb.conjunction()
                : cb.between(cb.literal(searchDate), root.get("startDate"), root.get("endDate"));
    }

}
