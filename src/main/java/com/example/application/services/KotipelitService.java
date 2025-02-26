package com.example.application.services;

import com.example.application.data.Asiakas;
import com.example.application.data.Kotipelit;
import com.example.application.repositories.KotipelitRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class KotipelitService {

  private final KotipelitRepository repository;

  public KotipelitService(KotipelitRepository repository) {
    this.repository = repository;
  }

  // Servicet toistaa samaa ideaa, kommentointi tehty AsiakasServiceen
  public Optional<Kotipelit> get(Long id) {
    return repository.findById(id);
  }

  public Kotipelit save(Kotipelit entity) {
    return repository.save(entity);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  public Page<Kotipelit> list(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Page<Kotipelit> list(Pageable pageable, Specification<Kotipelit> filter) {
    return repository.findAll(filter, pageable);
  }

  public int count() {
    return (int) repository.count();
  }

  @Transactional
  public void deleteByAsiakas(Asiakas asiakas) {
    repository.deleteByAsiakas(asiakas);
  }

  @Transactional
  public void deleteByAsiakasId(Long asiakasId) {
    repository.deleteByAsiakasId(asiakasId);
  }
}