/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse<RT> implements ApiResponse<RT> {
	@Builder.Default
	private final String status = "ERROR";
	private final ApiError error;
	private final RT data;
	private final Map<String, ValidationError> errors;

	public static <RT> ApiErrorResponseBuilder<RT, ?, ?> notFound() {
		return ApiErrorResponse.<RT>builder().status("NOT_FOUND");
	}
}
