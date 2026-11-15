package com.ferry.user.core.user.detail;

import com.ferry.user.domain.common.exception.InvalidUsernameException;
import com.ferry.user.domain.common.exception.NotFoundException;
import com.ferry.user.domain.user.BirthDate;
import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.BDDSoftAssertions.thenSoftly;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@ExtendWith(MockitoExtension.class)
class DefaultUserDetailUseCaseTest{

	private static final String USER_ID = "01USERROBERTWIJAYA000000";
	private static final String USERNAME = "robert_wijaya";

	@Mock
	UserDetailGateway gateway;
	@InjectMocks
	DefaultUserDetailUseCase useCase;
	@Mock
	UserDetailPresenter presenter;
	@Captor
	ArgumentCaptor<UserDetailResponse> responseCaptor;

	@Test
	void givenBlankUsername_thenThrowsConstraintViolationException(){
		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(new UserDetailRequest(" "), presenter))
				.isInstanceOf(ConstraintViolationException.class));

		then(gateway).shouldHaveNoInteractions();
		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenTooShortUsername_thenThrowsInvalidUsernameException(){
		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(new UserDetailRequest("ani"), presenter))
				.isInstanceOf(InvalidUsernameException.class)
				.hasMessage("Username must be at least 5 characters long"));

		then(gateway).shouldHaveNoInteractions();
		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenUnknownUsername_thenThrowsNotFoundException(){
		willReturn(Optional.empty()).given(gateway).findByUsername(eq(new Username("ghost_user")));

		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(new UserDetailRequest("ghost_user"), presenter))
				.isInstanceOf(NotFoundException.class)
				.hasMessage("User Not Found"));

		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenExistingUsername_thenPresentsUser(){
		Instant createdAt = Instant.parse("2026-10-02T08:30:00Z");
		User existing = User.builder()
				.id(USER_ID)
				.username(new Username(USERNAME))
				.birthDate(new BirthDate(LocalDate.of(1985, 12, 25)))
				.active(true)
				.version(2)
				.createdAt(createdAt)
				.createdBy(USER_ID)
				.updatedAt(createdAt)
				.updatedBy(USER_ID)
				.build();
		willReturn(Optional.of(existing)).given(gateway).findByUsername(eq(new Username(USERNAME)));

		useCase.execute(new UserDetailRequest(USERNAME), presenter);

		then(presenter).should().present(responseCaptor.capture());
		UserDetailResponse response = responseCaptor.getValue();

		thenSoftly(softly -> {
			softly.then(response.user().id()).isEqualTo(USER_ID);
			softly.then(response.user().usernameValue()).isEqualTo(USERNAME);
			softly.then(response.user().birthDateValue()).isEqualTo(LocalDate.of(1985, 12, 25));
			softly.then(response.user().active()).isTrue();
		});
	}

}
