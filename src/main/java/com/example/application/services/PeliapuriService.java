package com.example.application.services;

import com.example.application.data.Asiakas;
import com.example.application.data.Peliapuri;
import com.example.application.repositories.PeliapuriRepository;

import jakarta.transaction.Transactional;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class PeliapuriService {

  private final PeliapuriRepository repository;

  public PeliapuriService(PeliapuriRepository repository) {
    this.repository = repository;
  }

  // Servicet toistaa samaa ideaa, kommentointi tehty AsiakasServiceen
  public Optional<Peliapuri> get(Long id) {
    return repository.findById(id);
  }

  public Peliapuri save(Peliapuri entity) {
    return repository.save(entity);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

  public Page<Peliapuri> list(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Page<Peliapuri> list(Pageable pageable, Specification<Peliapuri> filter) {
    return repository.findAll(filter, pageable);
  }

  public int count() {
    return (int) repository.count();
  }

  @Transactional
  public void deleteByAsiakasId(Long asiakasId) {
    repository.deleteByAsiakasId(asiakasId);
  }

  @Transactional
  public void deleteByAsiakas(Asiakas asiakas) {
    repository.deleteByAsiakas(asiakas);
  }
}