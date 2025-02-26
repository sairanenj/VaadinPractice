package com.example.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.application.data.Sijainti;

public interface SijaintiRepository extends JpaRepository<Sijainti, Long>, JpaSpecificationExecutor<Sijainti> {

}
