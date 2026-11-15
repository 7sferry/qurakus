package com.ferry.user.gateway.user;

import com.ferry.user.core.user.detail.UserDetailGateway;
import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;
import com.ferry.user.gateway.user.entity.UserJpa;
import com.ferry.user.gateway.user.repository.UserPanacheRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@RequiredArgsConstructor
public class PanacheUserDetailGateway implements UserDetailGateway{
	private final UserPanacheRepository userPanacheRepository;

	@Override
	public Optional<User> findByUsername(Username username){
		return userPanacheRepository.findByUsernameAndDeletedIsFalse(username.value())
				.map(UserJpa::construct);
	}

}
