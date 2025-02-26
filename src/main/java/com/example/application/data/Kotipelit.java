package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Kotipelit extends AbstractEntity {

  private String pelitkotona;
  private String pelitpvkoti;
  private String tietoja;

  // Useat kotipelit samaan asiakkaaseen
  @ManyToOne
  private Asiakas asiakas;

  public Asiakas getAsiakas() {
    return asiakas;
  }

  public void setAsiakas(Asiakas asiakas) {
    this.asiakas = asiakas;
  }

  public String getPelitkotona() {
    return pelitkotona;
  }

  public void setPelitkotona(String pelitkotona) {
    this.pelitkotona = pelitkotona;
  }

  public String getPelitpvkoti() {
    return pelitpvkoti;
  }

  public void setPelitpvkoti(String pelitpvkoti) {
    this.pelitpvkoti = pelitpvkoti;
  }

  public String getTietoja() {
    return tietoja;
  }

  public void setTietoja(String tietoja) {
    this.tietoja = tietoja;
  }
}