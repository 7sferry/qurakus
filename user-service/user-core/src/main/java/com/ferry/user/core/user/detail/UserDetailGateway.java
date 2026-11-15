package com.ferry.user.core.user.detail;

import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;

import java.util.Optional;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

public interface UserDetailGateway{
	Optional<User> findByUsername(Username username);
}
