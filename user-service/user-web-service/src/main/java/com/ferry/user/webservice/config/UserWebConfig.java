package com.ferry.user.webservice.config;

import com.ferry.user.core.user.detail.DefaultUserDetailUseCase;
import com.ferry.user.core.user.detail.UserDetailGateway;
import com.ferry.user.core.user.detail.UserDetailUseCase;
import com.ferry.user.core.user.registration.DefaultUserRegistrationUseCase;
import com.ferry.user.core.user.registration.UserRegistrationGateway;
import com.ferry.user.core.user.registration.UserRegistrationUseCase;
import com.ferry.user.gateway.user.JpaUserDetailGateway;
import com.ferry.user.gateway.user.JpaUserRegistrationGateway;
import com.ferry.user.gateway.user.repository.UserJpaRepository;
import com.ferry.utils.generator.IdGenerator;
import com.ferry.utils.generator.UlidGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@ApplicationScoped
public class UserWebConfig{

	@Produces
	@ApplicationScoped
	IdGenerator idGenerator(){
		return new UlidGenerator();
	}

	@Produces
	@ApplicationScoped
	UserRegistrationGateway userRegistrationGateway(UserJpaRepository userJpaRepository, IdGenerator idGenerator){
		return new JpaUserRegistrationGateway(userJpaRepository, idGenerator);
	}

	@Produces
	@ApplicationScoped
	UserRegistrationUseCase userRegistrationUseCase(UserRegistrationGateway userRegistrationGateway){
		return new DefaultUserRegistrationUseCase(userRegistrationGateway);
	}

	@Produces
	@ApplicationScoped
	UserDetailGateway userDetailGateway(UserJpaRepository userJpaRepository){
		return new JpaUserDetailGateway(userJpaRepository);
	}

	@Produces
	@ApplicationScoped
	UserDetailUseCase userDetailUseCase(UserDetailGateway userDetailGateway){
		return new DefaultUserDetailUseCase(userDetailGateway);
	}

}
