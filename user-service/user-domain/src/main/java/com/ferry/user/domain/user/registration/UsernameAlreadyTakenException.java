package com.ferry.user.domain.user.registration;

import com.ferry.user.domain.user.Username;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

public class UsernameAlreadyTakenException extends RuntimeException{
	public UsernameAlreadyTakenException(Username username){
		super("Username already taken: " + username.value());
	}
}
