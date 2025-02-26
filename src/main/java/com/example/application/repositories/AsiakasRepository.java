package com.example.application.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.application.data.Asiakas;
import com.example.application.data.Sijainti;

public interface AsiakasRepository extends JpaRepository<Asiakas, Long>, JpaSpecificationExecutor<Asiakas> {
  void deleteBySijainti(Sijainti sijainti);
  List<Asiakas> findBySijainti(Sijainti sijainti);
}
