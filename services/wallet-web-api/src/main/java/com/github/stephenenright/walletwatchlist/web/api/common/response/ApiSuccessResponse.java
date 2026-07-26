/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiSuccessResponse<RT> implements ApiResponse<RT> {
	@Builder.Default
	private final String status = "SUCCESS";
	private final ApiSuccess success;
	private final RT data;
}
