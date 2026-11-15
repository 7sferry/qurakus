package com.ferry.user.webservice.config;

import com.ferry.user.core.user.detail.DefaultUserDetailUseCase;
import com.ferry.user.core.user.detail.UserDetailGateway;
import com.ferry.user.core.user.detail.UserDetailUseCase;
import com.ferry.user.core.user.registration.DefaultUserRegistrationUseCase;
import com.ferry.user.core.user.registration.UserRegistrationGateway;
import com.ferry.user.core.user.registration.UserRegistrationUseCase;
import com.ferry.user.gateway.user.PanacheUserDetailGateway;
import com.ferry.user.gateway.user.PanacheUserRegistrationGateway;
import com.ferry.user.gateway.user.repository.UserPanacheRepository;
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
	UserRegistrationGateway userRegistrationGateway(UserPanacheRepository userPanacheRepository, IdGenerator idGenerator){
		return new PanacheUserRegistrationGateway(userPanacheRepository, idGenerator);
	}

	@Produces
	@ApplicationScoped
	UserRegistrationUseCase userRegistrationUseCase(UserRegistrationGateway userRegistrationGateway){
		return new DefaultUserRegistrationUseCase(userRegistrationGateway);
	}

	@Produces
	@ApplicationScoped
	UserDetailGateway userDetailGateway(UserPanacheRepository userPanacheRepository){
		return new PanacheUserDetailGateway(userPanacheRepository);
	}

	@Produces
	@ApplicationScoped
	UserDetailUseCase userDetailUseCase(UserDetailGateway userDetailGateway){
		return new DefaultUserDetailUseCase(userDetailGateway);
	}

}
