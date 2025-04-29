package com.biblioteca.bibliotecavirtual;
import java.sql.Connection;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.biblioteca.bibliotecavirtual.repositorios.AutorRepositorio;

@SpringBootTest
class ConnectionTest {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private DataSource dataSource;

    @Test
    void testDatabaseConnectionWithDataSource() {
    assertDoesNotThrow(() -> {
        try (Connection conn = dataSource.getConnection()) {
                assertFalse(conn.isClosed(), "A conexão com o banco de dados deve estar aberta");
                // Test database connection
            }
        }, "Não foi possível conectar ao banco de dados MariaDB");
    }
        
        @Test
        void testRepositoryIsNotNull() {
        assertNotNull(autorRepositorio, "O repositório AutorRepositorio deve estar injetado e não deve ser nulo");
    }

    @Test
    void testDataSourceIsNotNull() {
        assertNotNull(dataSource, "O DataSource deve estar injetado");
    }
}