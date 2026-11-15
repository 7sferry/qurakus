package com.ferry.user.core.tools;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.util.Set;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

public interface UserValidation{
	default void validate(){
		Set<ConstraintViolation<UserValidation>> violations = UserValidationUtils.validate(this);
		if(!violations.isEmpty()){
			throw new ConstraintViolationException(violations);
		}
	}
}
