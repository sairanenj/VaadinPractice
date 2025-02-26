package com.example.application.data;

import jakarta.persistence.Entity;

@Entity
public class Kayttaja extends AbstractEntity {

  private String kayttajanimi;
  private String rooli;
  private String tehtava;

  public String getKayttajanimi() {
    return kayttajanimi;
  }

  public void setKayttajanimi(String kayttajanimi) {
    this.kayttajanimi = kayttajanimi;
  }

  public String getRooli() {
    return rooli;
  }

  public void setRooli(String rooli) {
    this.rooli = rooli;
  }

  public String getTehtava() {
    return tehtava;
  }

  public void setTehtava(String tehtava) {
    this.tehtava = tehtava;
  }
}