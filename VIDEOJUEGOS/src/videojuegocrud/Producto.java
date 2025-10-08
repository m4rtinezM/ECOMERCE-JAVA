/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package videojuegocrud;


public class Producto {
    private String idProducto;
    private String nombre;
    private String genero;
    private double precio;
    
    public Producto() {
    }
    
    public Producto(String idProducto, String nombre, String genero, double precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.genero = genero;
        this.precio = precio;
    }
    
    public String getIdProducto() { return idProducto; }
    public void setIdProducto(String idProducto) { this.idProducto = idProducto; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}