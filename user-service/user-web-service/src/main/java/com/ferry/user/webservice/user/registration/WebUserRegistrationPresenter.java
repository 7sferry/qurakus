package com.ferry.user.webservice.user.registration;

import com.ferry.user.core.user.registration.UserRegistrationPresenter;
import com.ferry.user.core.user.registration.UserRegistrationResponse;
import com.ferry.user.domain.user.User;
import lombok.Getter;
import org.jboss.resteasy.reactive.RestResponse;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Getter
public class WebUserRegistrationPresenter implements UserRegistrationPresenter{
	private RestResponse<UserRegistrationRestResponse> restResponse;

	@Override
	public void present(UserRegistrationResponse response){
		User user = response.user();
		String status = user.active() ? "Active" : "Inactive";
		restResponse = RestResponse.status(RestResponse.Status.CREATED, new UserRegistrationRestResponse(user.id(),
				user.usernameValue(), status, user.createdAt().toEpochMilli()));
	}

}
