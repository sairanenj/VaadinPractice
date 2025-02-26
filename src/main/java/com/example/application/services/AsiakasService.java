package com.example.application.services;

import com.example.application.data.Asiakas;
import com.example.application.data.Sijainti;
import com.example.application.repositories.AsiakasRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AsiakasService {

  private final AsiakasRepository repository;
  private final KotipelitService kotipelitService;
  private final PeliapuriService peliapuriService;

  // Lisätään riippuvuudet
  public AsiakasService(AsiakasRepository repository, KotipelitService kotipelitService, PeliapuriService peliapuriService) {
    this.repository = repository;
    this.kotipelitService = kotipelitService;
    this.peliapuriService = peliapuriService;
  }

  // Perus servicemetodeja
  public Optional<Asiakas> get(Long id) {
    return repository.findById(id);
  }

  public Asiakas save(Asiakas entity) {
    return repository.save(entity);
  }

  public void deleteById(Long id) {
    repository.deleteById(id);
  }

  // Haetaan kaikki asiakkaat sivutettuna (helpottaa sql kyselyjen jakamista yhdelle sivulle)
  public Page<Asiakas> list(Pageable pageable) {
    return repository.findAll(pageable);
  }

  // Haetaan kaikki asiakkaat sivutettuna ja suodatettuna
  public Page<Asiakas> list(Pageable pageable, Specification<Asiakas> filter) {
    return repository.findAll(filter, pageable);
  }

  // Lasketaan asiakkaiden määrän
  public int count() {
    return (int) repository.count();
  }

  // Haetaan kaikki asiakkaat listana
  public List<Asiakas> findAlltoList() {
    return repository.findAll();
  }

  // Poistetaan kaikki asiakkaat, jotka liittyvät annettuun sijaintiin (tietokantayhteyksien vaatimia toimenpiteitä kun poistetaan foreign key yhteyksiä yms.)
  @Transactional // Metodi tekee useita tietokantakyselyjä ja @Transactional varmistaa että kaikki kyselyt tehdään yhdessä transaktiossa
  public void deleteBySijainti(Sijainti sijainti) {
    List<Asiakas> asiakasList = repository.findBySijainti(sijainti);
    for (Asiakas asiakas : asiakasList) {
      kotipelitService.deleteByAsiakas(asiakas);
      peliapuriService.deleteByAsiakas(asiakas);
      repository.delete(asiakas);
    }
  }
}