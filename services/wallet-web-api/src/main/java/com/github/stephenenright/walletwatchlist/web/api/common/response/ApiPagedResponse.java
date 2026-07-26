/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiPagedResponse<RT> implements ApiResponse<RT> {
	@Builder.Default
	private final String status = "SUCCESS";
	private final PagedData<RT> data;

	@Builder
	@Getter
	public static class PagedData<RT> {
		private final long totalResults;
		private final int pageNumber;
		private final int pageSize;
		private final int totalPages;
		private final List<RT> results;
	}
}
