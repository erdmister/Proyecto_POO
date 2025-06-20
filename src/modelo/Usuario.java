package modelo;

public class Usuario {
    private String nombreUsuario;
    private String contraseña;
    private String rol;
    
    public Usuario(String nombreUsuario, String contraseña, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contraseña = contraseña;
        this.rol = rol;
    }
    
    // Autenticación básica comparando strings directamente (solo para desarrollo)
    public boolean autenticar(String contraseñaIngresada) {
        return this.contraseña.equals(contraseñaIngresada);
    }
    
    public String obtenerRol() {
        return rol;
    }
    
    // Getters
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public String getContraseña() {
        return contraseña;
    }
    
    public String getRol() {
        return rol;
    }
}