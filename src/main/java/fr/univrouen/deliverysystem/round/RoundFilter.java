package fr.univrouen.deliverysystem.round;

import java.time.Instant;

public class RoundFilter {
	private Instant searchDate;

	public Instant getSearchDate() {
		return searchDate;
	}

	public void setSearchDate(Instant searchDate) {
		this.searchDate = searchDate;
	}

	public static RoundFilterBuilder builder() {
		return new RoundFilterBuilder();
	}

	public static class RoundFilterBuilder {
		private Instant searchDate;

		public RoundFilterBuilder searchDate(Instant searchDate) {
			this.searchDate = searchDate;
			return this;
		}

		public RoundFilter build() {
			final RoundFilter filter = new RoundFilter();
			filter.setSearchDate(searchDate);
			return filter;
		}
	}
}
