package fr.univrouen.deliverysystem.driver;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

/**
 * Inspired by :
 * https://binarymindset.com/filter-your-data-the-right-way-using-jpa-specifications/
 */
public class DriverFilterSpecification {

    public static Specification<DriverEntity> filterBy(DriverFilter filter) {
        return Specification
                .where(hasCreatedAtAfter(filter.getCreatedAtAfter()))
                .and(hasCreatedAtBefore(filter.getCreatedAtBefore()))
                .and(hasIsAvailable(filter.getIsAvailable()));
    }

    private static Specification<DriverEntity> hasCreatedAtAfter(Instant createdAtAfter) {
        return (root, query, cb) -> createdAtAfter == null ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("createdAt"), createdAtAfter);
    }

    private static Specification<DriverEntity> hasCreatedAtBefore(Instant createdAtBefore) {
        return (root, query, cb) -> createdAtBefore == null ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("createdAt"), createdAtBefore);
    }

    private static Specification<DriverEntity> hasCreatedAtFrom(Instant createdAtFrom) {
        return (root, query, cb) -> createdAtFrom == null ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("createdAt"), createdAtFrom);
    }

    private static Specification<DriverEntity> hasCreatedAtTo(Instant createdAtTo) {
        return (root, query, cb) -> createdAtTo == null ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("createdAt"), createdAtTo);
    }

    private static Specification<DriverEntity> hasIsAvailable(Boolean isAvailable) {
        return (root, query, cb) -> isAvailable == null ? cb.conjunction()
                : cb.equal(root.get("isAvailable"), isAvailable);
    }
}
