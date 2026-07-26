/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pagination request parameters")
public class PagedRequestDTO {

	private static final int DEFAULT_PAGE = 1;
	private static final int DEFAULT_SIZE = 20;
	private static final int MAX_SIZE = 100;

	@Min(value = 1, message = "Page number must be at least 1")
	@Schema(description = "Page number (1-indexed)", example = "1", defaultValue = "1")
	private Integer page;

	@Min(value = 1, message = "Page size must be at least 1")
	@Max(value = 100, message = "Page size must be at most 100")
	@Schema(description = "Number of items per page", example = "20", defaultValue = "20", maximum = "100")
	private Integer size;

	public Pageable toPageable() {
		return toPageable(null);
	}

	public Pageable toPageable(Sort defaultSort) {
		int pageNumber = page != null && page >= 1 ? page - 1 : DEFAULT_PAGE - 1;
		int pageSize = size != null ? Math.min(size, MAX_SIZE) : DEFAULT_SIZE;

		return defaultSort != null
				? PageRequest.of(pageNumber, pageSize, defaultSort)
				: PageRequest.of(pageNumber, pageSize);
	}
}
