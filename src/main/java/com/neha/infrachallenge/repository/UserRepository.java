package com.neha.infrachallenge.repository;

import com.neha.infrachallenge.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}