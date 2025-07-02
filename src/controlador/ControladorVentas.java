package src.controlador;

import src.modelo.*;
import src.vista.*;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ControladorVentas {
    private VentaDAO ventaDAO;
    private ProductoDAO productoDAO;
    private VistaVentas vistaVentas;

    // Constructor
    public ControladorVentas(VentaDAO ventaDAO, ProductoDAO productoDAO, VistaVentas vistaVentas) {
        this.ventaDAO = ventaDAO;
        this.productoDAO = productoDAO;
        this.vistaVentas = vistaVentas;
    }

    /**
     * Registra una nueva venta en el sistema
     * @param venta La venta a registrar
     */
    public void registrarVenta(Venta venta) {
        try {
            // Validar stock antes de registrar la venta
            if (!validarStockDisponible(venta.getItems())) {
                vistaVentas.mostrarError("No hay suficiente stock para algunos productos");
                return;
            }

            // Registrar la venta
            boolean exito = ventaDAO.registrarVenta(venta);
            
            if (exito) {
                // Actualizar stock de productos
                actualizarStockProductos(venta.getItems());
                vistaVentas.mostrarMensaje("Venta registrada exitosamente");
                vistaVentas.limpiarFormulario();
            } else {
                vistaVentas.mostrarError("No se pudo registrar la venta");
            }
        } catch (SQLException e) {
            vistaVentas.mostrarError("Error al registrar venta: " + e.getMessage());
        }
    }

    /**
     * Obtiene las ventas del día actual
     * @return Lista de ventas realizadas hoy
     */
    public List<Venta> obtenerVentasDiarias() {
        try {
            List<Venta> ventas = ventaDAO.obtenerVentasPorFecha(new Date());
            vistaVentas.mostrarVentas(ventas);
            return ventas;
        } catch (SQLException e) {
            vistaVentas.mostrarError("Error al obtener ventas: " + e.getMessage());
            return null;
        }
    }

    /**
     * Valida que haya suficiente stock para todos los items de la venta
     * @param items Lista de items de la venta
     * @return true si hay stock suficiente, false si no
     */
    private boolean validarStockDisponible(List<ItemVenta> items) throws SQLException {
        for (ItemVenta item : items) {
            Producto producto = productoDAO.obtenerProductoPorId(item.getIdProducto());
            if (producto == null || producto.getStock() < item.getCantidad()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Actualiza el stock de los productos vendidos
     * @param items Lista de items de la venta
     */
    private void actualizarStockProductos(List<ItemVenta> items) throws SQLException {
        for (ItemVenta item : items) {
            Producto producto = productoDAO.obtenerProductoPorId(item.getIdProducto());
            producto.setStock(producto.getStock() - item.getCantidad());
            productoDAO.actualizarProducto(producto);
        }
    }

    /**
     * Método para inicializar el controlador
     */
    public void iniciar() {
        obtenerVentasDiarias();
    }
}
