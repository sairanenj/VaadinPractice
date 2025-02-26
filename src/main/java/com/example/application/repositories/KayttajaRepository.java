package com.example.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.application.data.Kayttaja;

public interface KayttajaRepository extends JpaRepository<Kayttaja, Long> {
}