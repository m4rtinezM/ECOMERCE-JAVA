/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuegocrud;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ProductoDAO {
    
    public boolean insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (id_producto, nombre, genero, precio) VALUES (?, ?, ?, ?)";
        
        System.out.println(" INSERT SQL: " + sql);
        System.out.println(" Valores: " + producto.getIdProducto() + ", " + producto.getNombre() + ", " + producto.getGenero() + ", " + producto.getPrecio());
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getIdProducto());
            pstmt.setString(2, producto.getNombre());
            pstmt.setString(3, producto.getGenero());
            pstmt.setDouble(4, producto.getPrecio());
            
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println(" INSERT EXITOSO - Filas afectadas: " + filasAfectadas);
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println(" ERROR SQL: " + e.getMessage());
            System.out.println(" Código Error: " + e.getErrorCode());
            
            if (e.getErrorCode() == 1062) {
                System.out.println(" Problema: ID DUPLICADO");
            } else if (e.getErrorCode() == 1146) {
                System.out.println(" Problema: TABLA NO EXISTE");
            } else if (e.getErrorCode() == 1364) {
                System.out.println(" Problema: FALTA CAMPO OBLIGATORIO");
            }
            
            return false;
        }
    }
    
    public List<Producto> obtenerTodosLosProductos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        
        System.out.println(" Obteniendo todos los productos...");
        
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println(" Consulta ejecutada");
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getString("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setGenero(rs.getString("genero"));
                producto.setPrecio(rs.getDouble("precio"));
                
                productos.add(producto);
            }
            
            System.out.println(" Productos obtenidos: " + productos.size());
            
        } catch (SQLException e) {
            System.out.println(" Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return productos;
    }
    
    // READ - Obtener producto por ID
    public Producto obtenerProductoPorId(String idProducto) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        Producto producto = null;
        
        System.out.println(" Buscando producto con ID: " + idProducto);
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idProducto);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                producto = new Producto();
                producto.setIdProducto(rs.getString("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setGenero(rs.getString("genero"));
                producto.setPrecio(rs.getDouble("precio"));
                System.out.println(" Producto encontrado: " + producto.getNombre());
            } else {
                System.out.println("️ Producto no encontrado");
            }
            
            rs.close();
            
        } catch (SQLException e) {
            System.out.println(" Error al obtener producto: " + e.getMessage());
            e.printStackTrace();
        }
        
        return producto;
    }
    
    public List<Producto> buscarProductos(String consulta) {
        List<Producto> resultados = new ArrayList<>();
        
        String sql = "SELECT * FROM productos WHERE " +
                     "id_producto LIKE ? OR " +
                     "nombre LIKE ? OR " +
                     "genero LIKE ? OR " +
                     "precio LIKE ? " +
                     "ORDER BY id_producto";
        
        System.out.println("🔍 Ejecutando búsqueda universal: '" + consulta + "'");
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String likeConsulta = "%" + consulta + "%";
            
            pstmt.setString(1, likeConsulta);  // id_producto
            pstmt.setString(2, likeConsulta);  // nombre
            pstmt.setString(3, likeConsulta);  // genero
            pstmt.setString(4, likeConsulta);  // precio
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getString("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setGenero(rs.getString("genero"));
                producto.setPrecio(rs.getDouble("precio"));
                resultados.add(producto);
            }
            
            rs.close();
            System.out.println(" Búsqueda completada. Resultados: " + resultados.size());
            
        } catch (SQLException e) {
            System.out.println(" Error en búsqueda universal: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultados;
    }
    
    public List<Producto> buscarPorCampo(String campo, String valor) {
        List<Producto> resultados = new ArrayList<>();
        
        List<String> camposValidos = Arrays.asList("id_producto", "nombre", "genero", "precio");
        if (!camposValidos.contains(campo)) {
            System.out.println("❌ Campo no válido: " + campo);
            return resultados;
        }
        
        String sql = "SELECT * FROM productos WHERE " + campo + " LIKE ? ORDER BY id_producto";
        
        System.out.println("🔍 Búsqueda por campo '" + campo + "': '" + valor + "'");
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + valor + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getString("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setGenero(rs.getString("genero"));
                producto.setPrecio(rs.getDouble("precio"));
                resultados.add(producto);
            }
            
            rs.close();
            System.out.println("✅ Búsqueda por campo completada. Resultados: " + resultados.size());
            
        } catch (SQLException e) {
            System.out.println("❌ Error en búsqueda por campo: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultados;
    }
    
    public List<Producto> obtenerProductosGratuitos() {
        List<Producto> resultados = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE precio = 0.00 ORDER BY nombre";
        
        System.out.println(" Obteniendo productos gratuitos...");
        
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = new Producto();
                producto.setIdProducto(rs.getString("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setGenero(rs.getString("genero"));
                producto.setPrecio(rs.getDouble("precio"));
                resultados.add(producto);
            }
            
            System.out.println(" Productos gratuitos encontrados: " + resultados.size());
            
        } catch (SQLException e) {
            System.out.println(" Error al obtener productos gratuitos: " + e.getMessage());
            e.printStackTrace();
        }
        
        return resultados;
    }
    
    public boolean actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, genero = ?, precio = ? WHERE id_producto = ?";
        
        System.out.println(" Actualizando producto: " + producto.getIdProducto());
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getGenero());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setString(4, producto.getIdProducto());
            
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println(" Actualización - Filas afectadas: " + filasAfectadas);
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println(" Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE - Eliminar producto
    public boolean eliminarProducto(String idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        
        System.out.println("🗑️ Eliminando producto: " + idProducto);
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, idProducto);
            int filasAfectadas = pstmt.executeUpdate();
            System.out.println(" Eliminación - Filas afectadas: " + filasAfectadas);
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.out.println(" Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // 📊 CONTAR TOTAL DE PRODUCTOS
    public int contarProductos() {
        String sql = "SELECT COUNT(*) as total FROM productos";
        int total = 0;
        
        try (Connection conn = Conexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                total = rs.getInt("total");
            }
            
        } catch (SQLException e) {
            System.out.println(" Error al contar productos: " + e.getMessage());
        }
        
        return total;
    }
}