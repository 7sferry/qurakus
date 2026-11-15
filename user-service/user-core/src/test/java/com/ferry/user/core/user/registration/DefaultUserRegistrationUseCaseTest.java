package com.ferry.user.core.user.registration;

import com.ferry.user.domain.common.exception.InvalidBirthDateException;
import com.ferry.user.domain.common.exception.InvalidUsernameException;
import com.ferry.user.domain.user.BirthDate;
import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;
import com.ferry.user.domain.user.registration.UsernameAlreadyTakenException;
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

import static org.assertj.core.api.BDDSoftAssertions.thenSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.never;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@ExtendWith(MockitoExtension.class)
class DefaultUserRegistrationUseCaseTest{

	private static final String USER_ID = "01USERBUDISANTOSO0000000";
	private static final String USERNAME = "budi_santoso";

	@Mock
	UserRegistrationGateway gateway;
	@InjectMocks
	DefaultUserRegistrationUseCase useCase;
	@Mock
	UserRegistrationPresenter presenter;
	@Captor
	ArgumentCaptor<User> userCaptor;
	@Captor
	ArgumentCaptor<UserRegistrationResponse> responseCaptor;

	@Test
	void givenBlankUsername_thenThrowsConstraintViolationException(){
		UserRegistrationRequest request = new UserRegistrationRequest("  ", LocalDate.of(1995, 3, 14));

		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(request, presenter))
				.isInstanceOf(ConstraintViolationException.class));

		then(gateway).shouldHaveNoInteractions();
		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenNullBirthDate_thenThrowsConstraintViolationException(){
		UserRegistrationRequest request = new UserRegistrationRequest("angelina", null);

		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(request, presenter))
				.isInstanceOf(ConstraintViolationException.class));

		then(gateway).shouldHaveNoInteractions();
		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenTooShortUsername_thenThrowsInvalidUsernameException(){
		UserRegistrationRequest request = new UserRegistrationRequest("rob", LocalDate.of(1990, 7, 1));

		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(request, presenter))
				.isInstanceOf(InvalidUsernameException.class)
				.hasMessage("Username must be at least 5 characters long"));

		then(gateway).shouldHaveNoInteractions();
		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenTooLongUsername_thenThrowsInvalidUsernameException(){
		UserRegistrationRequest request = new UserRegistrationRequest("w".repeat(51), LocalDate.of(1988, 1, 20));

		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(request, presenter))
				.isInstanceOf(InvalidUsernameException.class)
				.hasMessage("Username must be at most 50 characters long"));

		then(gateway).shouldHaveNoInteractions();
		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenUnderageBirthDate_thenThrowsInvalidBirthDateException(){
		UserRegistrationRequest request = new UserRegistrationRequest("siti_rahma", LocalDate.now().minusYears(17));

		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(request, presenter))
				.isInstanceOf(InvalidBirthDateException.class)
				.hasMessage("Birth date must be at least 18 years old"));

		then(gateway).shouldHaveNoInteractions();
		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenTakenUsername_thenThrowsUsernameAlreadyTakenException(){
		UserRegistrationRequest request = new UserRegistrationRequest("dewi_lestari", LocalDate.of(1992, 11, 2));
		willReturn(true).given(gateway).existsByUsername(eq(new Username("dewi_lestari")));

		thenSoftly(softly -> softly.thenThrownBy(() -> useCase.execute(request, presenter))
				.isInstanceOf(UsernameAlreadyTakenException.class)
				.hasMessage("Username already taken: dewi_lestari"));

		then(gateway).should(never()).save(any(User.class));
		then(presenter).shouldHaveNoInteractions();
	}

	@Test
	void givenValidRequest_thenSavesActiveUserAndPresentsSavedUser(){
		LocalDate birthDate = LocalDate.of(1997, 5, 9);
		UserRegistrationRequest request = new UserRegistrationRequest(USERNAME, birthDate);
		Instant createdAt = Instant.parse("2026-11-15T03:00:00Z");
		User saved = User.builder()
				.id(USER_ID)
				.username(new Username(USERNAME))
				.birthDate(new BirthDate(birthDate))
				.active(true)
				.version(0)
				.createdAt(createdAt)
				.createdBy(USER_ID)
				.updatedAt(createdAt)
				.updatedBy(USER_ID)
				.build();
		willReturn(false).given(gateway).existsByUsername(eq(new Username(USERNAME)));
		willReturn(saved).given(gateway).save(any(User.class));

		useCase.execute(request, presenter);

		then(gateway).should().save(userCaptor.capture());
		then(presenter).should().present(responseCaptor.capture());
		User registered = userCaptor.getValue();
		UserRegistrationResponse response = responseCaptor.getValue();

		thenSoftly(softly -> {
			softly.then(registered.id()).isNull();
			softly.then(registered.usernameValue()).isEqualTo(USERNAME);
			softly.then(registered.birthDateValue()).isEqualTo(birthDate);
			softly.then(registered.active()).isTrue();
			softly.then(registered.deleted()).isFalse();
			softly.then(registered.createdAt()).isNotNull();
			softly.then(response.user()).isEqualTo(saved);
		});
	}

}
