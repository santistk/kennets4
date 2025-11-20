package com.darwinruiz.shoplite.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static DbConnection instance;
    private Connection connection;
    
    // Configuración de la base de datos - ajustar según tu entorno
    private static final String DB_URL = "jdbc:postgresql://localhost:5433/shoplite";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";
    
    private DbConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Conexión a PostgreSQL establecida correctamente");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error: Driver de PostgreSQL no encontrado. Verifica que la dependencia esté en pom.xml", e);
        } catch (SQLException e) {
            String errorMsg = String.format(
                "Error al conectar con la base de datos PostgreSQL:\n" +
                "URL: %s\n" +
                "Usuario: %s\n" +
                "Mensaje: %s\n\n" +
                "Posibles causas:\n" +
                "1. PostgreSQL no está corriendo\n" +
                "2. La base de datos 'shoplite' no existe\n" +
                "3. Usuario o contraseña incorrectos\n" +
                "4. Puerto incorrecto (actual: 5433)",
                DB_URL, DB_USER, e.getMessage()
            );
            throw new RuntimeException(errorMsg, e);
        }
    }
    
    public static synchronized DbConnection getInstance() {
        if (instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conexión", e);
        }
        return connection;
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar conexión", e);
        }
    }
}

