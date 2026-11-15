package com.ferry.user.webservice.user.detail;

import com.ferry.user.core.user.detail.UserDetailRequest;
import com.ferry.user.core.user.detail.UserDetailUseCase;
import com.ferry.user.webservice.config.ProblemDetail;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestResponse;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Path("/public/user/detail")
@Tag(name = "User")
@RequiredArgsConstructor
public class UserDetailController{
	private final UserDetailUseCase userDetailUseCase;

	@GET
	@Transactional
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get a user by username", description = "Returns an active user.")
	@APIResponse(responseCode = "200", description = "User found",
			content = @Content(mediaType = MediaType.APPLICATION_JSON,
					schema = @Schema(implementation = UserDetailRestResponse.class)))
	@APIResponse(responseCode = "400", description = "Blank, too short or too long username",
			content = @Content(mediaType = ProblemDetail.MEDIA_TYPE,
					schema = @Schema(implementation = ProblemDetail.class)))
	@APIResponse(responseCode = "404", description = "No active user with that username",
			content = @Content(mediaType = ProblemDetail.MEDIA_TYPE,
					schema = @Schema(implementation = ProblemDetail.class)))
	@APIResponse(responseCode = "500", description = "Unexpected error",
			content = @Content(mediaType = ProblemDetail.MEDIA_TYPE,
					schema = @Schema(implementation = ProblemDetail.class)))
	public RestResponse<UserDetailRestResponse> getDetail(
			@Parameter(description = "Username, 5 to 50 characters", required = true, example = "budi_santoso")
			@QueryParam("username") String username){
		WebUserDetailPresenter presenter = new WebUserDetailPresenter();
		userDetailUseCase.execute(new UserDetailRequest(username), presenter);
		return presenter.getRestResponse();
	}

}
