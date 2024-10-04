package ar.edu.utn.frba.dds.model;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Usuarie {

  @Id
  @GeneratedValue
  private Long id;
  private String hashPassorwd;
  private String nombre;

  public Usuarie() {

  }

  public Usuarie(String nombre, String password) {
    super();
    this.nombre = nombre;
    this.hashPassorwd = DigestUtils.sha256Hex(password);
  }

  public Long getId() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public String getHashPassorwd() {
    return hashPassorwd;
  }
}
