package com.ferry.user.domain.user;

import com.ferry.user.domain.common.exception.InvalidUsernameException;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

public record Username(String value){
	public static final int MIN_LENGTH = 5;
	public static final int MAX_LENGTH = 50;

	public Username{
		if(value == null || value.isBlank()){
			throw new InvalidUsernameException("Username must not be blank");
		}
		if(value.length() < MIN_LENGTH){
			throw new InvalidUsernameException("Username must be at least " + MIN_LENGTH + " characters long");
		}
		if(value.length() > MAX_LENGTH){
			throw new InvalidUsernameException("Username must be at most " + MAX_LENGTH + " characters long");
		}
	}
}
