package com.ferry.user.core.user.registration;

import com.ferry.user.domain.user.BirthDate;
import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;
import com.ferry.user.domain.user.registration.UsernameAlreadyTakenException;
import lombok.RequiredArgsConstructor;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@RequiredArgsConstructor
public class DefaultUserRegistrationUseCase implements UserRegistrationUseCase{
	private final UserRegistrationGateway gateway;

	@Override
	public void execute(UserRegistrationRequest request, UserRegistrationPresenter presenter){
		request.validate();
		Username username = new Username(request.username());
		BirthDate birthDate = new BirthDate(request.birthDate());
		if(gateway.existsByUsername(username)){
			throw new UsernameAlreadyTakenException(username);
		}
		User saved = gateway.save(User.register(username, birthDate));
		presenter.present(new UserRegistrationResponse(saved));
	}

}
