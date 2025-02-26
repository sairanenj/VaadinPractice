package com.example.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.application.data.Pelit;
import com.example.application.repositories.PelitRepository;

@Service
public class PelitService {

  private final PelitRepository repository;
  
  // Servicet toistaa samaa ideaa, kommentointi tehty AsiakasServiceen
  public PelitService(PelitRepository repository) {
    this.repository = repository;
  }

  public Optional<Pelit> get(Long id) {
    return repository.findById(id);
  }

  public Pelit save(Pelit entity) {
    return repository.save(entity);
  }

  public List<Pelit> findAlltoList() {
    return repository.findAll();
  }
}
