package com.ferry.user.webservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.ferry.user.domain.common.exception.InvalidBirthDateException;
import com.ferry.user.domain.common.exception.InvalidUsernameException;
import com.ferry.user.domain.common.exception.NotFoundException;
import com.ferry.user.domain.user.registration.UsernameAlreadyTakenException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Slf4j
public class UserWebExceptionHandler{

	@ServerExceptionMapper({InvalidUsernameException.class, InvalidBirthDateException.class,
			ConstraintViolationException.class})
	RestResponse<ProblemDetail> handleBadRequest(RuntimeException e, UriInfo uriInfo){
		log.warn(e.getMessage(), e);
		return problem(Response.Status.BAD_REQUEST, e.getMessage(), uriInfo);
	}

	@ServerExceptionMapper({JsonProcessingException.class, MismatchedInputException.class})
	RestResponse<ProblemDetail> handleUnreadableBody(JsonProcessingException e, UriInfo uriInfo){
		log.warn(e.getMessage(), e);
		return problem(Response.Status.BAD_REQUEST,
				"Request body is malformed or carries a value outside the accepted set", uriInfo);
	}

	@ServerExceptionMapper(NotFoundException.class)
	RestResponse<ProblemDetail> handleNotFound(NotFoundException e, UriInfo uriInfo){
		log.warn(e.getMessage(), e);
		return problem(Response.Status.NOT_FOUND, e.getMessage(), uriInfo);
	}

	@ServerExceptionMapper(UsernameAlreadyTakenException.class)
	RestResponse<ProblemDetail> handleUsernameTaken(UsernameAlreadyTakenException e, UriInfo uriInfo){
		log.warn(e.getMessage(), e);
		return problem(Response.Status.CONFLICT, e.getMessage(), uriInfo);
	}

	@ServerExceptionMapper(WebApplicationException.class)
	RestResponse<ProblemDetail> handleWebApplication(WebApplicationException e, UriInfo uriInfo){
		log.warn(e.getMessage());
		Response.StatusType status = e.getResponse().getStatusInfo();
		return problem(status.getStatusCode(), status.getReasonPhrase(), e.getMessage(), uriInfo);
	}

	@ServerExceptionMapper(Throwable.class)
	RestResponse<ProblemDetail> handleError(Throwable e, UriInfo uriInfo){
		log.error(e.getMessage(), e);
		return problem(Response.Status.INTERNAL_SERVER_ERROR, "Internal Error", uriInfo);
	}

	private static RestResponse<ProblemDetail> problem(Response.Status status, String detail, UriInfo uriInfo){
		return problem(status.getStatusCode(), status.getReasonPhrase(), detail, uriInfo);
	}

	private static RestResponse<ProblemDetail> problem(int status, String title, String detail, UriInfo uriInfo){
		ProblemDetail body = ProblemDetail.forStatusAndDetail(status, title, detail, uriInfo.getRequestUri().getPath());
		return RestResponse.ResponseBuilder.<ProblemDetail>create(status)
				.entity(body)
				.type(ProblemDetail.MEDIA_TYPE)
				.build();
	}

}
