package com.darwinruiz.shoplite.repositories;

import com.darwinruiz.shoplite.database.DbConnection;
import com.darwinruiz.shoplite.models.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IProductRepository {
    private static ProductRepository instance;
    private final DbConnection dbConnection;

    private ProductRepository() {
        this.dbConnection = DbConnection.getInstance();
    }

    public static synchronized ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT id, name, price, stock, created_at FROM products ORDER BY id";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los productos", e);
        }
        
        return products;
    }

    @Override
    public List<Product> findAll(int page, int size) {
        String sql = "SELECT id, name, price, stock, created_at FROM products ORDER BY id LIMIT ? OFFSET ?";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            int offset = (page - 1) * size;
            stmt.setInt(1, size);
            stmt.setInt(2, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener productos paginados", e);
        }
        
        return products;
    }

    @Override
    public Product findById(Integer id) {
        String sql = "SELECT id, name, price, stock, created_at FROM products WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar producto por ID", e);
        }
        
        return null;
    }

    @Override
    public void save(Product product) {
        String sql = "INSERT INTO products (name, price, stock) VALUES (?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStock() != null ? product.getStock() : 0);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Error al crear producto, ninguna fila afectada");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Error al obtener ID generado");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar producto", e);
        }
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, stock = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setInt(3, product.getStock() != null ? product.getStock() : 0);
            stmt.setInt(4, product.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar producto", e);
        }
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }

    @Override
    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM products";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al contar productos", e);
        }
        
        return 0;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        Double price = rs.getDouble("price");
        Integer stock = rs.getInt("stock");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        
        return new Product(id, name, price, stock, createdAt);
    }
}
