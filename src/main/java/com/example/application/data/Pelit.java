package com.example.application.data;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
public class Pelit extends AbstractEntity {

  private String pelititle;
  private String pelisubtitle;
  private String pelidesctext;
  private String pelibadgetext;
  private String peliurl;

  // Monta peliapuria yhdelle pelille
  @OneToMany(mappedBy = "pelit")
    private List<Peliapuri> peliapurit;

  public String getPelititle() {
    return pelititle;
  }

  public void setPelititle(String pelititle) {
    this.pelititle = pelititle;
  }

  public String getPelisubtitle() {
    return pelisubtitle;
  }

  public void setPelisubtitle(String pelisubtitle) {
    this.pelisubtitle = pelisubtitle;
  }

  public String getPelibadgetext() {
    return pelibadgetext;
  }

  public void setPelibadgetext(String pelibadgetext) {
    this.pelibadgetext = pelibadgetext;
  }

  public String getPeliurl() {
    return peliurl;
  }

  public void setPeliurl(String peliurl) {
    this.peliurl = peliurl;
  }

  public String getPelidesctext() {
    return pelidesctext;
  }

  public void setPelidesctext(String pelidesctext) {
    this.pelidesctext = pelidesctext;
  }
}