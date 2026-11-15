package com.ferry.user.core.user.registration;

import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

public interface UserRegistrationGateway{
	User save(User register);

	boolean existsByUsername(Username username);
}
