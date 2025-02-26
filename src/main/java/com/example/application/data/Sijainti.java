package com.example.application.data;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Sijainti extends AbstractEntity {

  private String paikka;
  private String postitoimipaikka;
  private String postinumero;
  private String lisatiedot;

  // Monta asiakasta yhdelle sijainnille
  @OneToMany(mappedBy = "sijainti")
  private List<Asiakas> asiakkaat;

  public String getPaikka() {
    return paikka;
  }

  public void setPaikka(String paikka) {
    this.paikka = paikka;
  }

  public String getPostitoimipaikka() {
    return postitoimipaikka;
  }

  public void setPostitoimipaikka(String postitoimipaikka) {
    this.postitoimipaikka = postitoimipaikka;
  }

  public String getPostinumero() {
    return postinumero;
  }

  public void setPostinumero(String postinumero) {
    this.postinumero = postinumero;
  }

  public String getLisatiedot() {
    return lisatiedot;
  }

  public void setLisatiedot(String lisatiedot) {
    this.lisatiedot = lisatiedot;
  }

  public List<Asiakas> getAsiakkaat() {
    return asiakkaat;
  }

  public void setAsiakkaat(List<Asiakas> asiakkaat) {
    this.asiakkaat = asiakkaat;
  }
}