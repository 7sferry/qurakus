package com.ferry.user.gateway.user.repository;

import com.ferry.user.gateway.user.entity.UserJpa;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/************************
 * Made by [MR Ferry™]  *
 * on November 2026     *
 ************************/

@ApplicationScoped
public class UserJpaRepository implements PanacheRepositoryBase<UserJpa, String>{

	public Optional<UserJpa> findByUsernameAndDeletedIsFalse(String username){
		return find("username = :username and deleted is false", Parameters.with("username", username))
				.firstResultOptional();
	}

	public boolean existsByUsername(String username){
		return count("username = :username", Parameters.with("username", username)) > 0;
	}

}
