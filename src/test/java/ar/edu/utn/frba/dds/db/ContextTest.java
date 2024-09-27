package ar.edu.utn.frba.dds.db;

import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import org.junit.jupiter.api.Test;

public class ContextTest implements SimplePersistenceTest {

    @Test
    void contextUp() {
        withTransaction(() -> {
        });
    }


}