package com.ferry.user.domain.user;

import com.ferry.user.domain.common.exception.InvalidBirthDateException;
import com.ferry.user.domain.common.exception.InvalidUsernameException;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Builder(toBuilder = true)
public record User(
	String id,
	Username username,
	BirthDate birthDate,
	boolean active,
	Integer version,
	boolean deleted,
	Instant createdAt,
	String createdBy,
	Instant updatedAt,
	String updatedBy){
	public User{
		if(username == null){
			throw new InvalidUsernameException("Username must not be null");
		}
		if(birthDate == null){
			throw new InvalidBirthDateException("Birth date must not be null");
		}
	}

	public static User register(Username username, BirthDate birthDate){
		Instant now = Instant.now();
		return new User(null, username, birthDate, true, null, false, now, null, now, null);
	}

	public User ownedBy(String userId){
		return toBuilder()
				.createdBy(userId)
				.updatedBy(userId)
				.build();
	}

	public String usernameValue(){
		return username.value();
	}

	public LocalDate birthDateValue(){
		return birthDate.value();
	}

}
