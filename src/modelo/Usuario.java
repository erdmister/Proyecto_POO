package src.modelo;

public class Usuario {
    private String nombreUsuario;
    private String contrasenia;
    private String rol;
    
    public Usuario(String nombreUsuario, String contrasenia, String rol) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }
    
    // Autenticación básica comparando strings directamente (solo para desarrollo)
    public boolean autenticar(String contraseniaIngresada) {
        return this.contrasenia.equals(contraseniaIngresada);
    }
    
    public String obtenerRol() {
        return rol;
    }
    
    // Getters
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public String getContrasenia() {
        return contrasenia;
    }
    
    public String getRol() {
        return rol;
    }
}