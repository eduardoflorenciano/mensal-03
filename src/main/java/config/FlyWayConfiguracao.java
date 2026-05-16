package config;

import org.flywaydb.core.Flyway;

public class FlyWayConfiguracao {

    private static final String URL = "jdbc:postgresql://localhost:5432/dbautocentersilva";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "2020";

    public static void migrate() {
        Flyway flyway = Flyway.configure()
                .dataSource(URL, USUARIO, SENHA)
                .locations("classpath:db.migration")
                .baselineOnMigrate(true)
                .load();
        flyway.migrate();
    }
}
