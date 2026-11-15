package com.ferry.user.webservice.user.detail;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Schema(name = "UserDetailResponse")
public record UserDetailRestResponse(
	@Schema(description = "ULID", example = "01KA4ZQ3V7T2M9XK8D5R6B1N0C") String id,
	@Schema(example = "budi_santoso") String username,
	@Schema(enumeration = {"Active", "Inactive"}, example = "Active") String status,
	@Schema(description = "Birth date as EEEE, dd MMMM yyyy", example = "Friday, 09 May 1997") String birthDate,
	@Schema(description = "Registration time, epoch milliseconds", example = "1794715200000") long joinedAt){
}
