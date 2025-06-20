package src.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    private Connection conexion;

    public EmpleadoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public List<Empleado> obtenerTodosEmpleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT id, nombre, rfc, telefono, rol, sucursal, email, activo FROM empleados";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Empleado empleado = new Empleado(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("rfc"),
                    rs.getString("telefono"),
                    rs.getString("rol"),
                    rs.getString("sucursal"),
                    rs.getString("email"),
                    rs.getBoolean("activo")
                );
                empleados.add(empleado);
            }
        }
        return empleados;
    }

    public Empleado obtenerEmpleadoPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, rfc, telefono, rol, sucursal, email, activo FROM empleados WHERE id = ?";
        Empleado empleado = null;
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    empleado = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rfc"),
                        rs.getString("telefono"),
                        rs.getString("rol"),
                        rs.getString("sucursal"),
                        rs.getString("email"),
                        rs.getBoolean("activo")
                    );
                }
            }
        }
        return empleado;
    }

    public boolean agregarEmpleado(Empleado empleado) throws SQLException {
        String sql = "INSERT INTO empleados (nombre, rfc, telefono, rol, sucursal, email, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getRfc());
            stmt.setString(3, empleado.getTelefono());
            stmt.setString(4, empleado.getRol());
            stmt.setString(5, empleado.getSucursal());
            stmt.setString(6, empleado.getEmail());
            stmt.setBoolean(7, empleado.isActivo());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    empleado.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        }
    }

    public boolean actualizarEmpleado(Empleado empleado) throws SQLException {
        String sql = "UPDATE empleados SET nombre = ?, rfc = ?, telefono = ?, rol = ?, sucursal = ?, email = ?, activo = ? WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getRfc());
            stmt.setString(3, empleado.getTelefono());
            stmt.setString(4, empleado.getRol());
            stmt.setString(5, empleado.getSucursal());
            stmt.setString(6, empleado.getEmail());
            stmt.setBoolean(7, empleado.isActivo());
            stmt.setInt(8, empleado.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminarEmpleado(int id) throws SQLException {
        // Eliminación lógica (marcar como inactivo)
        String sql = "UPDATE empleados SET activo = FALSE WHERE id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Empleado> buscarEmpleados(String criterio) throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT id, nombre, rfc, telefono, rol, sucursal, email, activo FROM empleados " +
                     "WHERE nombre LIKE ? OR rfc LIKE ? OR email LIKE ? OR telefono LIKE ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            String likePattern = "%" + criterio + "%";
            stmt.setString(1, likePattern);
            stmt.setString(2, likePattern);
            stmt.setString(3, likePattern);
            stmt.setString(4, likePattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Empleado empleado = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rfc"),
                        rs.getString("telefono"),
                        rs.getString("rol"),
                        rs.getString("sucursal"),
                        rs.getString("email"),
                        rs.getBoolean("activo")
                    );
                    empleados.add(empleado);
                }
            }
        }
        return empleados;
    }
}
