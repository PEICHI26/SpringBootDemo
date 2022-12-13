package com.mongoddemo.demo.repository;

import com.mongoddemo.demo.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AppUserRepository extends MongoRepository<AppUser, String> {
	Optional<AppUser> findByEmailAddress(String email);
}
