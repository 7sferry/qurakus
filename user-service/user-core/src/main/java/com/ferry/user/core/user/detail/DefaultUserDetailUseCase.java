package com.ferry.user.core.user.detail;

import com.ferry.user.domain.common.exception.NotFoundException;
import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;
import lombok.RequiredArgsConstructor;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@RequiredArgsConstructor
public class DefaultUserDetailUseCase implements UserDetailUseCase{
	private final UserDetailGateway gateway;

	@Override
	public void execute(UserDetailRequest request, UserDetailPresenter presenter){
		request.validate();
		Username username = new Username(request.username());
		User user = gateway.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("User Not Found"));
		presenter.present(new UserDetailResponse(user));
	}

}
