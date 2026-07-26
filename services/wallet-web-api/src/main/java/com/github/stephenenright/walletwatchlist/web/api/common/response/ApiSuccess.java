/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiSuccess {
	private final String message;
}
