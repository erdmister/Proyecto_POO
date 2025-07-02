package src.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VentaDAO {
    private Connection conexion;
    private ProductoDAO productoDAO;

    public VentaDAO(Connection conexion, ProductoDAO productoDAO) {
        this.conexion = conexion;
        this.productoDAO = productoDAO;
    }

    public boolean registrarVenta(Venta venta) throws SQLException {
        try {
            conexion.setAutoCommit(false); // Iniciar transacción
            
            // 1. Registrar la venta principal
            String sqlVenta = "INSERT INTO ventas (fecha, total, empleado_id, metodo_pago) VALUES (?, ?, ?, ?)";
            int ventaId;
            
            try (PreparedStatement stmt = conexion.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setTimestamp(1, new Timestamp(venta.getFecha().getTime()));
                // Aquí no se establece el total, ya que se calculará automáticamente
                stmt.setDouble(2, venta.getTotal()); // Puedes dejar esto si necesitas el total en la base de datos
                stmt.setInt(3, venta.getEmpleadoId());
                stmt.setString(4, venta.getMetodoPago());
                
                stmt.executeUpdate();
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ventaId = generatedKeys.getInt(1);
                        venta.setId(ventaId);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la venta");
                    }
                }
            }
            
            // 2. Registrar los items de la venta y actualizar inventario
            String sqlItem = "INSERT INTO items_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmt = conexion.prepareStatement(sqlItem)) {
                for (ItemVenta item : venta.getItems()) {
                    // Registrar item
                    stmt.setInt(1, ventaId);
                    stmt.setInt(2, item.getIdProducto());
                    stmt.setInt(3, item.getCantidad());
                    stmt.setDouble(4, item.getPrecioUnitario());
                    stmt.setDouble(5, item.getSubtotal());
                    stmt.addBatch();
                    
                    // Actualizar stock del producto
                    Producto producto = productoDAO.obtenerProductoPorId(item.getIdProducto());
                    producto.setStock(producto.getStock() - item.getCantidad());
                    productoDAO.actualizarProducto(producto);
                }
                stmt.executeBatch();
            }
            
            conexion.commit(); // Confirmar transacción
            return true;
            
        } catch (SQLException e) {
            conexion.rollback(); // Revertir en caso de error
            throw e;
        } finally {
            conexion.setAutoCommit(true); // Restaurar comportamiento por defecto
        }
    }

    public List<Venta> obtenerVentasPorFecha(Date fecha) throws SQLException {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT v.*, e.nombre AS empleado_nombre " +
                     "FROM ventas v " +
                     "JOIN empleados e ON v.empleado_id = e.id " +
                     "WHERE DATE(v.fecha) = ? " +
                     "ORDER BY v.fecha DESC";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(fecha.getTime()));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Venta venta = new Venta();
                    venta.setId(rs.getInt("id"));
                    venta.setFecha(rs.getTimestamp("fecha"));
                    venta.setEmpleadoId(rs.getInt("empleado_id"));
                    venta.setMetodoPago(rs.getString("metodo_pago"));
                    
                    // Obtener items de la venta
                    venta.setItems(obtenerItemsVenta(venta.getId()));
                    
                    ventas.add(venta);
                }
            }
        }
        return ventas;
    }

    private List<ItemVenta> obtenerItemsVenta(int ventaId) throws SQLException {
        List<ItemVenta> items = new ArrayList<>();
        String sql = "SELECT iv.*, p.nombre AS producto_nombre " +
                     "FROM items_venta iv " +
                     "JOIN productos p ON iv.producto_id = p.id " +
                     "WHERE iv.venta_id = ?";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, ventaId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemVenta item = new ItemVenta(
                        rs.getInt("producto_id"),
                        rs.getString("producto_nombre"),
                        rs.getInt("cantidad"),
                        rs.getDouble("precio_unitario")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }

    // Método adicional útil
    public List<Venta> obtenerVentasDelDia() throws SQLException {
        return obtenerVentasPorFecha(new Date());
    }
}
