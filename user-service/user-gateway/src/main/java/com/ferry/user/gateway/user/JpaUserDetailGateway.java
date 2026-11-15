package com.ferry.user.gateway.user;

import com.ferry.user.core.user.detail.UserDetailGateway;
import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;
import com.ferry.user.gateway.user.entity.UserJpa;
import com.ferry.user.gateway.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@RequiredArgsConstructor
public class JpaUserDetailGateway implements UserDetailGateway{
	private final UserJpaRepository userJpaRepository;

	@Override
	public Optional<User> findByUsername(Username username){
		return userJpaRepository.findByUsernameAndDeletedIsFalse(username.value())
				.map(UserJpa::construct);
	}

}
