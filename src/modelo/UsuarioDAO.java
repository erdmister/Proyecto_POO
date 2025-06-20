package src.modelo;

import java.sql.*;

public class UsuarioDAO {
    private Connection conexion;
    
    public UsuarioDAO(Connection conexion) {
        this.conexion = conexion;
    }
    
    // Obtiene un usuario por su nombre de usuario
    public Usuario obtenerUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT nombre_usuario, contraseña, rol FROM usuarios WHERE nombre_usuario = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getString("nombre_usuario"),
                        rs.getString("contraseña"),
                        rs.getString("rol")
                    );
                }
            }
        }
        return null;
    }
    
    // Valida las credenciales del usuario
    public boolean validarCredenciales(String nombreUsuario, String contrasenia) throws SQLException {
        Usuario usuario = obtenerUsuario(nombreUsuario);
        return usuario != null && usuario.autenticar(contrasenia);
    }
    
    // Método adicional: crear usuario (para pruebas)
    public boolean crearUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre_usuario, contraseña, rol) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNombreUsuario());
            stmt.setString(2, usuario.getContrasenia());
            stmt.setString(3, usuario.getRol());
            
            return stmt.executeUpdate() > 0;
        }
    }
}
