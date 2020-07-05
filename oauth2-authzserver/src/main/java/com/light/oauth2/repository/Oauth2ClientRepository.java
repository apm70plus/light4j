package com.light.oauth2.repository;

import java.util.Optional;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

import com.light.oauth2.model.Oauth2Client;

/**
 * Oauth2ClientRepository
 */
public interface Oauth2ClientRepository extends CrudRepository<Oauth2Client, Long>, QuerydslPredicateExecutor<Oauth2Client> {

	Optional<Oauth2Client> findByClientId(String clientId);
}
