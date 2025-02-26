package com.example.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.application.data.Asiakas;
import com.example.application.data.Peliapuri;

public interface PeliapuriRepository extends JpaRepository<Peliapuri, Long>, JpaSpecificationExecutor<Peliapuri> {
  void deleteByAsiakas(Asiakas asiakas);
  void deleteByAsiakasId(Long asiakasId);
}
