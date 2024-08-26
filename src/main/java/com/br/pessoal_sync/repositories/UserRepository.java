package com.br.pessoal_sync.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.pessoal_sync.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
