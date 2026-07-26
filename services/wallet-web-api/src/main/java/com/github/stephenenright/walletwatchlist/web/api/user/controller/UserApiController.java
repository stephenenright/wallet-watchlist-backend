/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.controller;

import com.github.stephenenright.walletwatchlist.web.api.common.controller.BaseApiController;
import com.github.stephenenright.walletwatchlist.web.api.common.dto.PagedRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.common.response.ApiResponse;
import com.github.stephenenright.walletwatchlist.web.api.common.response.GetResponse;
import com.github.stephenenright.walletwatchlist.web.api.user.dto.UserDTO;
import com.github.stephenenright.walletwatchlist.web.api.user.dto.request.CreateUserRequestDTO;
import com.github.stephenenright.walletwatchlist.web.api.user.mapper.UserMapper;
import com.github.stephenenright.walletwatchlist.web.api.user.service.UserGetService;
import com.github.stephenenright.walletwatchlist.web.api.user.service.UserUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserApiController extends BaseApiController<UserDTO> {

	private final UserGetService userGetService;
	private final UserUpdateService userUpdateService;
	private final UserMapper userMapper;

	@GetMapping
	@Operation(summary = "List all users", description = "Returns a paginated list of all users ordered by last name, first name")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved users")})
	public ResponseEntity<ApiResponse<UserDTO>> list(@ParameterObject PagedRequestDTO pagedRequest) {
		return apiListPaged(() -> {
			var page = userGetService.findAll(pagedRequest.toPageable()).map(userMapper::toDto);
			return GetResponse.createSuccessResponse(page);
		});
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get user by ID", description = "Returns a user by their unique identifier")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")})
	public ResponseEntity<ApiResponse<UserDTO>> getById(
			@Parameter(description = "User ID", example = "00000000-0000-0000-0000-000000000001") @PathVariable UUID id) {
		return apiGet(() -> GetResponse
				.createSuccessOrNotFound(userGetService.findById(id).map(userMapper::toDto).orElse(null)));
	}

	@PostMapping
	@Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
	@ApiResponses({
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully"),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or validation failed")})
	public ResponseEntity<ApiResponse<UserDTO>> create(
			@Parameter(description = "User creation request") @Valid @RequestBody CreateUserRequestDTO request) {
		return apiCreate(() -> {
			var createResponse = userUpdateService.create(request);
			if (createResponse.isCreated() && createResponse.getResult().isPresent()) {
				return com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse
						.createSuccessResponse(userMapper.toDto(createResponse.getResult().get()));
			}
			if (createResponse.hasValidationErrors() && createResponse.getValidationErrors().isPresent()) {
				return com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse
						.createValidationFailedResponse(createResponse.getValidationErrors().get());
			}
			return com.github.stephenenright.walletwatchlist.web.api.common.response.CreateResponse
					.createFailedResponse(createResponse.getErrorMessage().orElse("Creation failed"));
		});
	}
}
