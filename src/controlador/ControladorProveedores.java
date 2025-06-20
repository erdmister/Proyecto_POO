package src.controlador;

import src.modelo.*;
import src.vista.*;
import java.sql.SQLException;
import java.util.List;

public class ControladorProveedores {
    private ProveedorDAO proveedorDAO;
    private VistaProveedores vistaProveedores;

    // Constructor
    public ControladorProveedores(ProveedorDAO proveedorDAO, VistaProveedores vistaProveedores) {
        this.proveedorDAO = proveedorDAO;
        this.vistaProveedores = vistaProveedores;
    }

    /**
     * Agrega un nuevo proveedor al sistema
     * @param proveedor El proveedor a agregar
     */
    public void agregarProveedor(Proveedor proveedor) {
        try {
            // Validar datos antes de agregar
            if (!validarDatosProveedor(proveedor)) {
                vistaProveedores.mostrarError("Datos del proveedor incompletos o inválidos");
                return;
            }

            boolean exito = proveedorDAO.agregarProveedor(proveedor);
            
            if (exito) {
                vistaProveedores.mostrarMensaje("Proveedor agregado exitosamente");
                actualizarListaProveedores();
                vistaProveedores.limpiarFormulario();
            } else {
                vistaProveedores.mostrarError("No se pudo agregar el proveedor");
            }
        } catch (SQLException e) {
            manejarErrorSQL(e, "agregar proveedor");
        }
    }

    /**
     * Actualiza los datos de un proveedor existente
     * @param proveedor El proveedor con los datos actualizados
     */
    public void actualizarProveedor(Proveedor proveedor) {
        try {
            // Validar datos antes de actualizar
            if (!validarDatosProveedor(proveedor)) {
                vistaProveedores.mostrarError("Datos del proveedor incompletos o inválidos");
                return;
            }

            boolean exito = proveedorDAO.actualizarProveedor(proveedor);
            
            if (exito) {
                vistaProveedores.mostrarMensaje("Proveedor actualizado exitosamente");
                actualizarListaProveedores();
            } else {
                vistaProveedores.mostrarError("No se pudo actualizar el proveedor");
            }
        } catch (SQLException e) {
            manejarErrorSQL(e, "actualizar proveedor");
        }
    }

    /**
     * Elimina un proveedor del sistema
     * @param id El ID del proveedor a eliminar
     */
    public void eliminarProveedor(int id) {
        try {
            boolean exito = proveedorDAO.eliminarProveedor(id);
            
            if (exito) {
                vistaProveedores.mostrarMensaje("Proveedor eliminado exitosamente");
                actualizarListaProveedores();
            } else {
                vistaProveedores.mostrarError("No se pudo eliminar el proveedor");
            }
        } catch (SQLException e) {
            manejarErrorSQL(e, "eliminar proveedor");
        }
    }

    /**
     * Busca proveedores según un criterio
     * @param criterio Texto para buscar coincidencias
     * @return Lista de proveedores que coinciden
     */
    public List<Proveedor> buscarProveedores(String criterio) {
        try {
            List<Proveedor> resultados = proveedorDAO.buscarProveedores(criterio);
            vistaProveedores.mostrarProveedores(resultados);
            return resultados;
        } catch (SQLException e) {
            manejarErrorSQL(e, "buscar proveedores");
            return null;
        }
    }

    /**
     * Actualiza la lista de proveedores en la vista
     */
    public void actualizarListaProveedores() {
        try {
            List<Proveedor> proveedores = proveedorDAO.obtenerTodosProveedores();
            vistaProveedores.mostrarProveedores(proveedores);
        } catch (SQLException e) {
            manejarErrorSQL(e, "cargar proveedores");
        }
    }

    /**
     * Método para inicializar el controlador
     */
    public void iniciar() {
        actualizarListaProveedores();
    }

    /**
     * Valida los datos básicos del proveedor
     */
    private boolean validarDatosProveedor(Proveedor proveedor) {
        return proveedor.getNombre() != null && !proveedor.getNombre().isEmpty() &&
               proveedor.getRfc() != null && !proveedor.getRfc().isEmpty();
    }

    /**
     * Maneja errores de SQL mostrándolos en la vista
     */
    private void manejarErrorSQL(SQLException e, String operacion) {
        String mensaje = "Error al " + operacion + ": ";
        
        // Detectar error de duplicado (RUC único)
        if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("ruc")) {
            mensaje += "Ya existe un proveedor con este RUC";
        } else {
            mensaje += e.getMessage();
        }
        
        vistaProveedores.mostrarError(mensaje);
    }
    
    /**
     * Obtiene proveedor por ID (método adicional útil)
     */
    public Proveedor obtenerProveedorPorId(int id) {
        try {
            return proveedorDAO.obtenerProveedorPorId(id);
        } catch (SQLException e) {
            manejarErrorSQL(e, "obtener proveedor");
            return null;
        }
    }
}
