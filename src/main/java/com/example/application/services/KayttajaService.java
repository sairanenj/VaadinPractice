package com.example.application.services;

import com.example.application.data.Kayttaja;
import com.example.application.repositories.KayttajaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KayttajaService {

  @Autowired
  private KayttajaRepository kayttajaRepository;

  // Servicet toistaa samaa ideaa, kommentointi tehty AsiakasServiceen
  public List<Kayttaja> findAll() {
    return kayttajaRepository.findAll();
  }

  public Kayttaja save(Kayttaja kayttaja) {
    return kayttajaRepository.save(kayttaja);
  }

  public void delete(Long id) {
    kayttajaRepository.deleteById(id);
  }

  public Page<Kayttaja> list(Pageable pageable) {
    return kayttajaRepository.findAll(pageable);
  }

  public Optional<Kayttaja> findById(Long id) {
    return kayttajaRepository.findById(id);
  }
}