package com.example.application.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.application.data.Asiakas;
import com.example.application.data.Kotipelit;

public interface KotipelitRepository extends JpaRepository<Kotipelit, Long>, JpaSpecificationExecutor<Kotipelit> {
  void deleteByAsiakasId(Long asiakasId);
  void deleteByAsiakas(Asiakas asiakas);
}
