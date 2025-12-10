package br.gov.caixa.service;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.MSSQLServerContainer;

import java.util.HashMap;
import java.util.Map;

public class SqlServerTestResource implements QuarkusTestResourceLifecycleManager {

    private MSSQLServerContainer<?> sqlServer;

    @Override
    public Map<String, String> start() {
        sqlServer = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2022-latest")
                .acceptLicense()
                .withPassword("StrongPassword123!")
                .withInitScript("schema.sql");
        sqlServer.start();

        Map<String, String> props = new HashMap<>();
        props.put("quarkus.datasource.jdbc.url", sqlServer.getJdbcUrl());
        props.put("quarkus.datasource.username", sqlServer.getUsername());
        props.put("quarkus.datasource.password", sqlServer.getPassword());
        props.put("quarkus.datasource.db-kind", "mssql");
        props.put("quarkus.hibernate-orm.database.generation", "drop-and-create");

        return props;
    }

    @Override
    public void stop() {
        if (sqlServer != null) {
            sqlServer.stop();
        }
    }
}