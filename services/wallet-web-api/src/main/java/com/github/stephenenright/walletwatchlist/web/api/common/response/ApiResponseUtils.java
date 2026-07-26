/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.response;

import com.github.stephenenright.walletwatchlist.web.api.common.validation.ValidationError;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.data.domain.Page;

public abstract class ApiResponseUtils {

	public static <RT> ApiResponse<RT> createValidationFailedErrorResponse(String message, String errorCode,
			Map<String, ValidationError> validationErrors) {
		return ApiErrorResponse
				.<RT>builder().error(ApiError.builder().message(message != null ? message : "Validation failed")
						.errorCode(errorCode).validationErrors(validationErrors).build())
				.errors(validationErrors).build();
	}

	public static <RT> ApiResponse<RT> createNotFoundErrorResponse(String message, String errorCode) {
		return ApiErrorResponse.<RT>notFound().error(
				ApiError.builder().message(message != null ? message : "Item not found").errorCode(errorCode).build())
				.build();
	}

	public static <RT> ApiResponse<RT> createErrorResponse(String message) {
		return ApiErrorResponse.<RT>builder()
				.error(ApiError.builder().message(message != null ? message : "An unexpected error occurred").build())
				.build();
	}

	public static <RT> ApiResponse<RT> createErrorResponse(String message, String errorCode,
			Map<String, ValidationError> validationErrors) {
		return ApiErrorResponse.<RT>builder()
				.error(ApiError.builder().message(message != null ? message : "An unexpected error occurred")
						.errorCode(errorCode).validationErrors(validationErrors).build())
				.build();
	}

	public static <RT> ApiResponse<RT> createSuccessResponse(String message) {
		return ApiSuccessResponse.<RT>builder().success(ApiSuccess.builder().message(message).build()).build();
	}

	public static <RT> ApiResponse<RT> createSuccessResponse(String message, RT data) {
		return ApiSuccessResponse.<RT>builder().success(ApiSuccess.builder().message(message).build()).data(data)
				.build();
	}

	public static <RT> ApiResponse<RT> creationSuccessResponse(CreateResponse<RT> createResponse, Class<?> createdCls) {
		String message = createResponse.getMessage().orElse(createdCls.getSimpleName() + " created successfully");
		return ApiSuccessResponse.<RT>builder().success(ApiSuccess.builder().message(message).build())
				.data(createResponse.getResult().orElse(null)).build();
	}

	public static <RT> ApiResponse<RT> updateSuccessResponse(UpdateResponse<RT> updateResponse, Class<?> updatedCls) {
		String message = updateResponse.getMessage().orElse(updatedCls.getSimpleName() + " updated successfully");
		return ApiSuccessResponse.<RT>builder().success(ApiSuccess.builder().message(message).build())
				.data(updateResponse.getResult().orElse(null)).build();
	}

	public static <RT> ApiResponse<RT> deleteSuccessResponse(DeleteResponse<RT> deleteResponse, Class<?> deletedCls) {
		String message = deleteResponse.getMessage().orElse(deletedCls.getSimpleName() + " deleted successfully");
		return ApiSuccessResponse.<RT>builder().success(ApiSuccess.builder().message(message).build())
				.data(deleteResponse.getResult().orElse(null)).build();
	}

	public static <RT> ApiResponse<RT> pagedResponse(Page<RT> page) {
		return ApiPagedResponse.<RT>builder()
				.data(ApiPagedResponse.PagedData.<RT>builder()
						.results(page != null ? page.getContent() : new ArrayList<>())
						.pageSize(page != null ? page.getSize() : 0).totalPages(page != null ? page.getTotalPages() : 0)
						.pageNumber(page != null ? page.getNumber() + 1 : 0)
						.totalResults(page != null ? page.getTotalElements() : 0).build())
				.build();
	}
}
