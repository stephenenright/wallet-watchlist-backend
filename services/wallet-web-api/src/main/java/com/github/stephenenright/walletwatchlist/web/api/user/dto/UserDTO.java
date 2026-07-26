/* (C) 2026 */
package com.github.stephenenright.walletwatchlist.web.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User account")
public class UserDTO {

	@Schema(description = "Unique identifier", example = "00000000-0000-0000-0000-000000000001")
	private UUID id;

	@Schema(description = "User's first name", example = "John")
	private String firstName;

	@Schema(description = "User's last name", example = "Doe")
	private String lastName;

	@Schema(description = "User's email address", example = "john.doe@example.com")
	private String email;

	@Schema(description = "Date the user was created")
	private Instant dateCreated;

	@Schema(description = "Date the user was last updated")
	private Instant dateUpdated;
}
