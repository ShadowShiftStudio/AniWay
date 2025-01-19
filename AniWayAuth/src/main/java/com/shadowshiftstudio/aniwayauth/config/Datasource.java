package com.shadowshiftstudio.aniwayauth.config;

import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

public class Datasource {
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/aniway_db")
                .username("admin")
                .password("admin")
                .build();
    }
}
