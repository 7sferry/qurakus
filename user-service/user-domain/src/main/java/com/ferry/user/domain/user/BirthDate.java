package com.ferry.user.domain.user;

import com.ferry.user.domain.common.exception.InvalidBirthDateException;

import java.time.LocalDate;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

public record BirthDate(LocalDate value){
	public static final int MIN_AGE = 18;

	public BirthDate{
		if(value == null){
			throw new InvalidBirthDateException("Birth date must not be blank");
		}
		if(LocalDate.now().minusYears(MIN_AGE).isBefore(value)){
			throw new InvalidBirthDateException("Birth date must be at least " + MIN_AGE + " years old");
		}
	}
}
