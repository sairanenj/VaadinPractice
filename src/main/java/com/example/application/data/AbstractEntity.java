package com.example.application.data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class AbstractEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
  // Alkuarvo on asetettu data.sql demodatan id-arvojen huomioimiseksi
  @SequenceGenerator(name = "idgenerator", initialValue = 1000)
  private Long id;

  // Tämä on näissä valmiissa abstractentitypohjissa. Ymmärtääkseni käytetään versionhallintaan, eli pystytään selvittämään päällekkäisyydet / muutokset eritellen.
  @Version
  private int version;

  // Perus gettersetterit
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getVersion() {
    return version;
  }

  // Overridetään hashCode-metodi käyttämään entityn id:tä
  @Override
  public int hashCode() {
    if (getId() != null) {
      return getId().hashCode();
    }
    return super.hashCode();
  }

  // Overridetään equals-metodi vertaamaan entityn id:tä
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof AbstractEntity that)) {
      return false; // null tai ei AbstractEntity-luokkaa
    }
    if (getId() != null) {
      return getId().equals(that.getId());
    }
    return super.equals(that);
  }
}