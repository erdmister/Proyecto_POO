package src.controlador;

import src.modelo.*;
import src.vista.*;
import java.util.List;
import java.sql.SQLException;


public class ControladorInventario {
    private ProductoDAO productoDAO;
    private VistaInventario vistaInventario;

    // Constructor
    public ControladorInventario(ProductoDAO productoDAO, VistaInventario vistaInventario) {
        this.productoDAO = productoDAO;
        this.vistaInventario = vistaInventario;
    }

    /**
     * Agrega un nuevo producto al inventario
     * @param producto El producto a agregar
     */
    public void agregarProducto(Producto producto) {
        try {
            boolean exito = productoDAO.agregarProducto(producto);
            if (exito) {
                actualizarVista();
                vistaInventario.mostrarMensaje("Producto agregado exitosamente");
            } else {
                vistaInventario.mostrarError("No se pudo agregar el producto");
            }
        } catch (SQLException e) {
            vistaInventario.mostrarError("Error al agregar producto: " + e.getMessage());
        }
    }

    /**
     * Actualiza un producto existente
     * @param producto El producto con los datos actualizados
     */
    public void actualizarProducto(Producto producto) {
        try {
            boolean exito = productoDAO.actualizarProducto(producto);
            if (exito) {
                actualizarVista();
                vistaInventario.mostrarMensaje("Producto actualizado exitosamente");
            } else {
                vistaInventario.mostrarError("No se pudo actualizar el producto");
            }
        } catch (SQLException e) {
            vistaInventario.mostrarError("Error al actualizar producto: " + e.getMessage());
        }
    }

    /**
     * Elimina un producto del inventario
     * @param id El ID del producto a eliminar
     */
    public void eliminarProducto(int id) {
        try {
            boolean exito = productoDAO.eliminarProducto(id);
            if (exito) {
                actualizarVista();
                vistaInventario.mostrarMensaje("Producto eliminado exitosamente");
            } else {
                vistaInventario.mostrarError("No se pudo eliminar el producto");
            }
        } catch (SQLException e) {
            vistaInventario.mostrarError("Error al eliminar producto: " + e.getMessage());
        }
    }

    /**
     * Busca productos según un criterio
     * @param criterio Texto para buscar coincidencias en nombre o ID
     * @return Lista de productos que coinciden con el criterio
     */
    public List<Producto> buscarProductos(String criterio) {
        try {
            List<Producto> resultados = productoDAO.buscarProductos(criterio);
            return resultados;
        } catch (SQLException e) {
            vistaInventario.mostrarError("Error al buscar productos: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza la vista con los productos actuales
     */
   public void actualizarVista() {
       try {
           System.out.println("Intentando obtener productos...");
           List<Producto> productos = productoDAO.obtenerTodosProductos();
           System.out.println("Productos obtenidos: " + productos.size());
           vistaInventario.mostrarProductos(productos);
       } catch (SQLException e) {
           vistaInventario.mostrarError("Error al cargar productos: " + e.getMessage());
       }
   }

    // Método para inicializar el controlador
    public void iniciar() {
        actualizarVista();
    }
}
