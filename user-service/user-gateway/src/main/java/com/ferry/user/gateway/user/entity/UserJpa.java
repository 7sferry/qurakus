package com.ferry.user.gateway.user.entity;

import com.ferry.user.domain.user.BirthDate;
import com.ferry.user.domain.user.User;
import com.ferry.user.domain.user.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "uk_users_username", columnNames = "username"))
public class UserJpa{

	@Id
	@Column(nullable = false, length = 50)
	private String id;
	@Column(nullable = false, length = 50)
	private String username;
	@Column(nullable = false)
	private LocalDate birthDate;
	@Column(nullable = false)
	private boolean active;
	@Version
	private Integer version;
	@Column(nullable = false)
	private boolean deleted;
	@Column(nullable = false, length = 50, updatable = false)
	private String createdBy;
	@Column(nullable = false, updatable = false)
	private Instant createdAt;
	@Column(nullable = false, length = 50)
	private String updatedBy;
	@Column(nullable = false)
	private Instant updatedAt;

	public static UserJpa construct(String id, User user){
		UserJpa entity = new UserJpa();
		entity.setId(id);
		entity.setUsername(user.usernameValue());
		entity.setBirthDate(user.birthDateValue());
		entity.setActive(user.active());
		entity.setVersion(user.version());
		entity.setDeleted(user.deleted());
		entity.setCreatedBy(user.createdBy());
		entity.setCreatedAt(user.createdAt());
		entity.setUpdatedBy(user.updatedBy());
		entity.setUpdatedAt(user.updatedAt());
		return entity;
	}

	public static User construct(UserJpa saved){
		return new User(saved.getId(), new Username(saved.getUsername()), new BirthDate(saved.getBirthDate()),
				saved.isActive(), saved.getVersion(), saved.isDeleted(), saved.getCreatedAt(), saved.getCreatedBy(),
				saved.getUpdatedAt(), saved.getUpdatedBy());
	}

}
