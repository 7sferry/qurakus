package com.ferry.user.webservice.user.detail;

import com.ferry.user.core.user.detail.UserDetailPresenter;
import com.ferry.user.core.user.detail.UserDetailResponse;
import com.ferry.user.domain.user.User;
import lombok.Getter;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Getter
public class WebUserDetailPresenter implements UserDetailPresenter{
	private static final DateTimeFormatter BIRTH_DATE_FORMATTER =
			DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH);

	private RestResponse<UserDetailRestResponse> restResponse;

	@Override
	public void present(UserDetailResponse response){
		User user = response.user();
		String status = user.active() ? "Active" : "Inactive";
		String birthDate = BIRTH_DATE_FORMATTER.format(user.birthDateValue());
		restResponse = RestResponse.ok(new UserDetailRestResponse(user.id(), user.usernameValue(), status,
				birthDate, user.createdAt().toEpochMilli()));
	}

}
