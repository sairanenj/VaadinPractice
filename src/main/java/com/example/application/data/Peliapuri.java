package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Peliapuri extends AbstractEntity {

  private boolean kaytossa;
  private boolean jatkoon;
  private boolean eijatkoon;
  private String huomiot;

  // Monta peliapuria yhdelle asiakkaalle
  @ManyToOne
  private Asiakas asiakas;

  // Monta peliapuria yhdelle pelille
  @ManyToOne
  private Pelit pelit;

  public boolean isKaytossa() {
    return kaytossa;
  }

  public void setKaytossa(boolean kaytossa) {
    this.kaytossa = kaytossa;
  }

  public boolean isJatkoon() {
    return jatkoon;
  }

  public void setJatkoon(boolean jatkoon) {
    this.jatkoon = jatkoon;
  }

  public boolean isEijatkoon() {
    return eijatkoon;
  }

  public void setEijatkoon(boolean eijatkoon) {
    this.eijatkoon = eijatkoon;
  }

  public String getHuomiot() {
    return huomiot;
  }

  public void setHuomiot(String huomiot) {
    this.huomiot = huomiot;
  }

  public Asiakas getAsiakas() {
    return asiakas;
  }

  public void setAsiakas(Asiakas asiakas) {
    this.asiakas = asiakas;
  }

  public Pelit getPelit() {
    return pelit;
  }

  public void setPelit(Pelit pelit) {
    this.pelit = pelit;
  }
}