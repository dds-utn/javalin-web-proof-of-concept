package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.model.Usuarie;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.apache.commons.codec.digest.DigestUtils;

public class RepositorioUsuaries implements WithSimplePersistenceUnit {

  public static RepositorioUsuaries INSTANCE = new RepositorioUsuaries();

  public void registrar(Usuarie usuarie) {
    entityManager().persist(usuarie);
  }

  public long contar() {
    return entityManager().createQuery("from Usuarie").getResultStream().count();
  }
  public Usuarie buscar(String nombre, String contrasenia) {
    return entityManager()
        .createQuery("from Usuarie where nombre = :nombre and hashPassorwd = :hashPassorwd"
            , Usuarie.class)
        .setParameter("nombre", nombre)
        .setParameter("hashPassorwd", DigestUtils.sha256Hex(contrasenia))
        .getResultList()
        .get(0);
  }

}
