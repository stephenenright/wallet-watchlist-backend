/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.common.controller;

import com.github.stephenenright.walletwatchlist.web.api.common.response.*;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiFunctions.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public abstract class BaseApiController<T> {

	private Class<T> modelClass;

	public BaseApiController() {
		modelClass = getModelClass();
	}

	public <RT> ResponseEntity<ApiResponse<RT>> apiListPaged(ListPagedApiCall<RT> listCall) {
		try {
			GetResponse<Page<RT>> getResponse = listCall.execute();

			if (getResponse.isOk()) {
				return ResponseEntity.ok(ApiResponseUtils.pagedResponse(getResponse.getResult().orElse(null)));
			}

			if (getResponse.getValidationErrors().isPresent()) {
				return ResponseEntity.badRequest().body(ApiResponseUtils.createValidationFailedErrorResponse(
						getResponse.getErrorMessage().orElse("Invalid request"),
						getResponse.getErrorCode().orElse(null), getResponse.getValidationErrors().orElse(null)));
			} else if (getResponse.isNotFoundError()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtils.createNotFoundErrorResponse(
						getResponse.getErrorMessage().orElse(null), getResponse.getErrorCode().orElse(null)));
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponseUtils.createErrorResponse(
							getResponse.getErrorMessage().orElse("An unexpected error occurred"),
							getResponse.getErrorCode().orElse(null), getResponse.getValidationErrors().orElse(null)));

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Getting paged list failed", e);
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponseUtils.createErrorResponse("An unexpected error occurred", null, null));
		}
	}

	public <RT> ResponseEntity<ApiResponse<List<RT>>> apiList(ListApiCall<RT> listCall) {
		try {
			GetResponse<List<RT>> getResponse = listCall.execute();

			if (getResponse.isOk()) {
				return ResponseEntity
						.ok(ApiResponseUtils.createSuccessResponse("Success", getResponse.getResult().orElse(null)));
			}

			if (getResponse.isNotAuthorized()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(ApiResponseUtils.createErrorResponse(getResponse.getErrorMessage().orElse("Forbidden"),
								getResponse.getErrorCode().orElse(null), null));
			} else if (getResponse.isNotFoundError()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtils.createNotFoundErrorResponse(
						getResponse.getErrorMessage().orElse(null), getResponse.getErrorCode().orElse(null)));
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponseUtils.createErrorResponse(
							getResponse.getErrorMessage().orElse("An unexpected error occurred"),
							getResponse.getErrorCode().orElse(null), getResponse.getValidationErrors().orElse(null)));

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Get list failed", e);
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponseUtils.createErrorResponse("An unexpected error occurred", null, null));
		}
	}

	public <RT> ResponseEntity<ApiResponse<RT>> apiGet(GetApiCall<RT> getCall) {
		try {
			GetResponse<RT> getResponse = getCall.execute();

			if (getResponse.isOk()) {
				return ResponseEntity
						.ok(ApiResponseUtils.createSuccessResponse("Success", getResponse.getResult().orElse(null)));
			}

			if (getResponse.isNotAuthorized()) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(ApiResponseUtils.createErrorResponse(getResponse.getErrorMessage().orElse("Forbidden"),
								getResponse.getErrorCode().orElse(null), null));
			} else if (getResponse.isNotFoundError()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtils.createNotFoundErrorResponse(
						getResponse.getErrorMessage().orElse(null), getResponse.getErrorCode().orElse(null)));
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponseUtils.createErrorResponse(
							getResponse.getErrorMessage().orElse("An unexpected error occurred"),
							getResponse.getErrorCode().orElse(null), getResponse.getValidationErrors().orElse(null)));

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Get failed", e);
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponseUtils.createErrorResponse("An unexpected error occurred", null, null));
		}
	}

	public <RT> ResponseEntity<ApiResponse<RT>> apiCreate(CreateApiCall<RT> createCall) {
		try {
			CreateResponse<RT> createResponse = createCall.execute();

			if (createResponse.isCreated()) {
				return ResponseEntity.status(HttpStatus.CREATED)
						.body(ApiResponseUtils.creationSuccessResponse(createResponse, modelClass));
			}

			if (createResponse.hasValidationErrors() && createResponse.getValidationErrors().isPresent()) {
				return ResponseEntity.badRequest().body(ApiResponseUtils.createValidationFailedErrorResponse(
						createResponse.getErrorMessage().orElse("Invalid request"),
						createResponse.getErrorCode().orElse(null), createResponse.getValidationErrors().orElse(null)));
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseUtils.createErrorResponse(
					createResponse.getErrorMessage().orElse("Creation failed"),
					createResponse.getErrorCode().orElse(null), createResponse.getValidationErrors().orElse(null)));

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Creation failed", e);
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponseUtils.createErrorResponse("An unexpected error occurred", null, null));
		}
	}

	public <RT> ResponseEntity<ApiResponse<RT>> apiUpdate(UpdateApiCall<RT> updateCall) {
		try {
			UpdateResponse<RT> updateResponse = updateCall.execute();

			if (updateResponse.isUpdated()) {
				return ResponseEntity.ok(ApiResponseUtils.updateSuccessResponse(updateResponse, modelClass));
			}

			if (updateResponse.isNotFound()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtils.createNotFoundErrorResponse(
						updateResponse.getErrorMessage().orElse(null), updateResponse.getErrorCode().orElse(null)));
			} else if (updateResponse.getValidationErrors().isPresent()) {
				return ResponseEntity.badRequest().body(ApiResponseUtils.createValidationFailedErrorResponse(
						updateResponse.getErrorMessage().orElse("Invalid request"),
						updateResponse.getErrorCode().orElse(null), updateResponse.getValidationErrors().orElse(null)));
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponseUtils.createErrorResponse(updateResponse.getErrorMessage().orElse("Update failed"),
							updateResponse.getErrorCode().orElse(null),
							updateResponse.getValidationErrors().orElse(null)));

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Update failed", e);
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponseUtils.createErrorResponse("An unexpected error occurred", null, null));
		}
	}

	public <RT> ResponseEntity<ApiResponse<RT>> apiDelete(DeleteApiCall<RT> deleteCall) {
		try {
			DeleteResponse<RT> deleteResponse = deleteCall.execute();

			if (deleteResponse.isDeleted()) {
				return ResponseEntity.ok(ApiResponseUtils.deleteSuccessResponse(deleteResponse, modelClass));
			} else if (deleteResponse.isNotFound()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(ApiResponseUtils.createNotFoundErrorResponse(
								deleteResponse.getErrorMessage().orElse("Item not found"),
								deleteResponse.getErrorCode().orElse(null)));
			}

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(ApiResponseUtils.createErrorResponse(deleteResponse.getErrorMessage().orElse("Delete failed"),
							deleteResponse.getErrorCode().orElse(null), null));

		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("Deletion failed", e);
			}

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(ApiResponseUtils.createErrorResponse("An unexpected error occurred", null, null));
		}
	}

	@SuppressWarnings("unchecked")
	private Class<T> getModelClass() {
		if (this.modelClass == null) {
			this.modelClass = this.getOverriddenModelClass();
			if (this.modelClass == null) {
				ParameterizedType thisType = (ParameterizedType) this.getClass().getGenericSuperclass();
				Type[] args = thisType.getActualTypeArguments();
				this.modelClass = (Class<T>) args[0];
			}
		}
		return this.modelClass;
	}

	protected Class<T> getOverriddenModelClass() {
		return null;
	}
}
