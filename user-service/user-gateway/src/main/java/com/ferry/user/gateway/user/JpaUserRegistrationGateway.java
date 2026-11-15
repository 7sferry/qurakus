package com.ferry.user.gateway.user;

import com.ferry.user.core.user.registration.UserRegistrationGateway;
import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;
import com.ferry.user.gateway.user.entity.UserJpa;
import com.ferry.user.gateway.user.repository.UserJpaRepository;
import com.ferry.utils.generator.IdGenerator;
import lombok.RequiredArgsConstructor;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@RequiredArgsConstructor
public class JpaUserRegistrationGateway implements UserRegistrationGateway{
	private final UserJpaRepository userJpaRepository;
	private final IdGenerator idGenerator;

	@Override
	public User save(User register){
		String id = idGenerator.generateId();
		UserJpa entity = UserJpa.construct(id, register.ownedBy(id));
		userJpaRepository.persistAndFlush(entity);
		return UserJpa.construct(entity);
	}

	@Override
	public boolean existsByUsername(Username username){
		return userJpaRepository.existsByUsername(username.value());
	}

}
