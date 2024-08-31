package com.br.pessoal_sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.pessoal_sync.domain.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {}
