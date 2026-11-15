package com.ferry.user.webservice.user.registration;

import com.ferry.user.core.user.registration.UserRegistrationRequest;
import com.ferry.user.core.user.registration.UserRegistrationUseCase;
import com.ferry.user.webservice.config.ProblemDetail;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Path("/public/user/registration")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserRegistrationController{
	private final UserRegistrationUseCase userRegistrationUseCase;

	@POST
	@Transactional
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Register a user",
			description = "Creates an active user. The username must be 5 to 50 characters and unique, "
					+ "and the user must be at least 18 years old.")
	@APIResponse(responseCode = "201", description = "User registered",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UserRegistrationRestResponse.class)))
	@APIResponse(responseCode = "400",
			description = "Missing body, blank or missing field, invalid username or birth date, or malformed JSON",
			content = @Content(mediaType = ProblemDetail.MEDIA_TYPE,
					schema = @Schema(implementation = ProblemDetail.class)))
	@APIResponse(responseCode = "409", description = "Username already taken",
			content = @Content(mediaType = ProblemDetail.MEDIA_TYPE,
					schema = @Schema(implementation = ProblemDetail.class)))
	@APIResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(mediaType = ProblemDetail.MEDIA_TYPE,
					schema = @Schema(implementation = ProblemDetail.class)))
	public RestResponse<UserRegistrationRestResponse> register(
			@RequestBody(content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UserRegistrationRequest.class),
					examples = @ExampleObject(name = "budi",
							value = "{\"username\":\"budi_santoso\",\"birthDate\":\"1997-05-09\"}")))
			UserRegistrationRequest request){
		WebUserRegistrationPresenter presenter = new WebUserRegistrationPresenter();
		userRegistrationUseCase.execute(request, presenter);
		return presenter.getRestResponse();
	}

}
