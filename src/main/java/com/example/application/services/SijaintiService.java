package com.example.application.services;

import com.example.application.data.Sijainti;
import com.example.application.repositories.SijaintiRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SijaintiService {

  private final SijaintiRepository repository;
  private final AsiakasService asiakasServ;

  // Servicet toistaa samaa ideaa, kommentointi tehty AsiakasServiceen
  public SijaintiService(SijaintiRepository repository, AsiakasService asiakasServ) {
    this.repository = repository;
    this.asiakasServ = asiakasServ;
  }

  public Optional<Sijainti> get(Long id) {
    return repository.findById(id);
  }

  public Sijainti save(Sijainti entity) {
    return repository.save(entity);
  }

  @Transactional
  public void delete(Sijainti entity) {
    asiakasServ.deleteBySijainti(entity);
    repository.delete(entity);
  }

  public Page<Sijainti> list(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public Page<Sijainti> list(Pageable pageable, Specification<Sijainti> filter) {
    return repository.findAll(filter, pageable);
  }

  public int count() {
    return (int) repository.count();
  }

  public List<Sijainti> findAlltoList() {
    return repository.findAll();
  }
}