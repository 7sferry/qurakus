package com.ferry.user.core.user.registration;

import com.ferry.user.core.tools.UserValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

public record UserRegistrationRequest(
	@NotBlank String username,
	@NotNull LocalDate birthDate) implements UserValidation{
}
