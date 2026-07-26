/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

	private final String message;
	private final String errorCode;
	private final Map<String, ValidationError> validationErrors;
}
