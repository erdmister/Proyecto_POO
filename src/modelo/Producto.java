package modelo;

import java.util.Date;

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private double costo;
    private int stock;
    private Date fechaCaducidad;

    // Constructor
    public Producto(int id, String nombre, double precio, double costo, int stock, Date fechaCaducidad) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.costo = costo;
        this.stock = stock;
        this.fechaCaducidad = fechaCaducidad;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", costo=" + costo +
                ", stock=" + stock +
                ", fechaCaducidad=" + fechaCaducidad +
                '}';
    }
}