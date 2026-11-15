package com.ferry.user.core.user.detail;

import com.ferry.user.core.tools.UserValidation;
import jakarta.validation.constraints.NotBlank;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

public record UserDetailRequest(@NotBlank String username) implements UserValidation{
}
