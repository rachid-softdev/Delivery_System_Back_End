package fr.univrouen.deliverysystem.driver;

import java.time.Instant;

public class DriverFilter {

    private Boolean isAvailable;
    private Instant createdAtAfter;
    private Instant createdAtBefore;

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Instant getCreatedAtAfter() {
        return createdAtAfter;
    }

    public void setCreatedAtAfter(Instant createdAtAfter) {
        this.createdAtAfter = createdAtAfter;
    }

    public Instant getCreatedAtBefore() {
        return createdAtBefore;
    }

    public void setCreatedAtBefore(Instant createdAtBefore) {
        this.createdAtBefore = createdAtBefore;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean isAvailable;
        private Instant createdAtAfter;
        private Instant createdAtBefore;

        public Builder isAvailable(Boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public Builder createdAtAfter(Instant createdAtAfter) {
            this.createdAtAfter = createdAtAfter;
            return this;
        }

        public Builder createdAtBefore(Instant createdAtBefore) {
            this.createdAtBefore = createdAtBefore;
            return this;
        }

        public DriverFilter build() {
            final DriverFilter filter = new DriverFilter();
            filter.setIsAvailable(isAvailable);
            filter.setCreatedAtAfter(createdAtAfter);
            filter.setCreatedAtBefore(createdAtBefore);
            return filter;
        }
    }
}
