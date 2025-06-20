package modelo;

import java.util.ArrayList;
import java.util.List;

public class Proveedor {
    private int id;
    private String nombre;
    private String rfc;
    private String telefono;
    private String email;
    private String direccion;
    private List<String> productosSuministrados;

    // Constrfctor
    public Proveedor() {
        this.productosSuministrados = new ArrayList<>();
    }

    public Proveedor(int id, String nombre, String rfc, String telefono, String email, String direccion, List<String> productosSuministrados) {
        this.id = id;
        this.nombre = nombre;
        this.rfc = rfc;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.productosSuministrados = productosSuministrados;
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

    public String getrfc() {
        return rfc;
    }

    public void setrfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<String> getProductosSuministrados() {
        return productosSuministrados;
    }

    public void setProductosSuministrados(List<String> productosSuministrados) {
        this.productosSuministrados = productosSuministrados;
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", rfc='" + rfc + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", productosSuministrados=" + productosSuministrados +
                '}';
    }
}