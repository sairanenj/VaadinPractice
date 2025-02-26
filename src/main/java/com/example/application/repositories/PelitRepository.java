package com.example.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.application.data.Pelit;

public interface PelitRepository extends JpaRepository<Pelit, Long>, JpaSpecificationExecutor<Pelit> {

}
