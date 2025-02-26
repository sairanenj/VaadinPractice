package com.example.application.data;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Asiakas extends AbstractEntity {

  private String etunimi;
  private String sukunimi;
  private String puhnro;
  private String sposti;

  // ManyToOne suhde Sijainti-entiteettiin
  // Useilla asiakkailla voi olla sama sijainti
  @ManyToOne
  private Sijainti sijainti;

  // OneToMany suhde Peliapuri-entiteettiin
  // Asiakkaat voivat liittyä useisiin peliapureihin
  @OneToMany(mappedBy = "asiakas")
  private List<Peliapuri> peliapurit;

  // OneToMany suhde Kotipelit-entiteettiin
  // Asiakkaat voivat liittyä useisiin kotipeleihin
  @OneToMany(mappedBy = "asiakas")
  private List<Kotipelit> kotipelit;

  public String getEtunimi() {
    return etunimi;
  }

  public void setEtunimi(String etunimi) {
    this.etunimi = etunimi;
  }

  public String getSukunimi() {
    return sukunimi;
  }

  public void setSukunimi(String sukunimi) {
    this.sukunimi = sukunimi;
  }

  public String getPuhnro() {
    return puhnro;
  }

  public void setPuhnro(String puhnro) {
    this.puhnro = puhnro;
  }

  public String getSposti() {
    return sposti;
  }

  public void setSposti(String sposti) {
    this.sposti = sposti;
  }

  public Sijainti getSijainti() {
    return sijainti;
  }

  public void setSijainti(Sijainti sijainti) {
    this.sijainti = sijainti;
  }

  public List<Peliapuri> getPeliapurit() {
    return peliapurit;
  }

  public void setPeliapurit(List<Peliapuri> peliapurit) {
    this.peliapurit = peliapurit;
  }

  public List<Kotipelit> getKotipelit() {
    return kotipelit;
  }

  public void setKotipelit(List<Kotipelit> kotipelit) {
    this.kotipelit = kotipelit;
  }
}