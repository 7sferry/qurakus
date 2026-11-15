package com.ferry.user.webservice.user.registration;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Schema(name = "UserRegistrationResponse")
public record UserRegistrationRestResponse(
	@Schema(description = "ULID", example = "01KA4ZQ3V7T2M9XK8D5R6B1N0C") String id,
	@Schema(example = "budi_santoso") String username,
	@Schema(enumeration = {"Active", "Inactive"}, example = "Active") String status,
	@Schema(description = "Registration time, epoch milliseconds", example = "1794715200000") long joinedAt){
}
