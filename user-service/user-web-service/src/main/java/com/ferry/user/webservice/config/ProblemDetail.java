package com.ferry.user.webservice.config;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Schema(description = "RFC 7807 problem detail")
public record ProblemDetail(
	@Schema(example = "about:blank") String type,
	@Schema(example = "Bad Request") String title,
	@Schema(example = "400") int status,
	@Schema(example = "Username must be at least 5 characters long") String detail,
	@Schema(example = "/public/user/registration") String instance){
	public static final String MEDIA_TYPE = "application/problem+json";
	private static final String DEFAULT_TYPE = "about:blank";

	public static ProblemDetail forStatusAndDetail(int status, String title, String detail, String instance){
		return new ProblemDetail(DEFAULT_TYPE, title, status, detail, instance);
	}
}
