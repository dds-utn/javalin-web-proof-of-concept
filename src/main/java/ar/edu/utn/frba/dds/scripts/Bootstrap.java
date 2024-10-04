package ar.edu.utn.frba.dds.scripts;

import ar.edu.utn.frba.dds.model.Usuarie;
import ar.edu.utn.frba.dds.repositories.RepositorioUsuaries;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bootstrap implements WithSimplePersistenceUnit {
  public void init() {
    withTransaction(() -> {
      var usuaries = Arrays.asList(
          new Usuarie("feli", "feli"),
          new Usuarie("dani", "dani"),
          new Usuarie("umi", "umi")
      );
      usuaries.forEach((usuarie) -> RepositorioUsuaries.INSTANCE.registrar(usuarie));
    });
  }

}
