/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import lombok.Getter;

@Getter
public enum ErrorType {
	BAD_REQUEST("badRequest"), UNAUTHORIZED("unauthorized"), CONFLICT("conflict"), NOT_FOUND(
			"notFound"), INTERNAL_SERVER_ERROR("internalServerError");

	private final String errorType;

	ErrorType(String errorType) {
		this.errorType = errorType;
	}
}
