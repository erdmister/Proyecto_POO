package src.controlador;

import src.modelo.*;
import src.vista.*;
import java.sql.SQLException;
import java.util.List;

public class ControladorPersonal {
    private EmpleadoDAO empleadoDAO;
    private VistaPersonal vistaPersonal;

    // Constructor
    public ControladorPersonal(EmpleadoDAO empleadoDAO, VistaPersonal vistaPersonal) {
        this.empleadoDAO = empleadoDAO;
        this.vistaPersonal = vistaPersonal;
    }

    /**
     * Agrega un nuevo empleado al sistema
     * @param empleado El empleado a agregar
     */
    public void agregarEmpleado(Empleado empleado) {
        try {
            // Validar datos antes de agregar
            if (!validarDatosEmpleado(empleado)) {
                vistaPersonal.mostrarError("Datos del empleado incompletos o inválidos");
                return;
            }

            boolean exito = empleadoDAO.agregarEmpleado(empleado);
            
            if (exito) {
                vistaPersonal.mostrarMensaje("Empleado agregado exitosamente");
                actualizarListaEmpleados();
                vistaPersonal.limpiarFormulario();
            } else {
                vistaPersonal.mostrarError("No se pudo agregar el empleado");
            }
        } catch (SQLException e) {
            manejarErrorSQL(e, "agregar empleado");
        }
    }

    /**
     * Actualiza los datos de un empleado existente
     * @param empleado El empleado con los datos actualizados
     */
    public void actualizarEmpleado(Empleado empleado) {
        try {
            // Validar datos antes de actualizar
            if (!validarDatosEmpleado(empleado)) {
                vistaPersonal.mostrarError("Datos del empleado incompletos o inválidos");
                return;
            }

            boolean exito = empleadoDAO.actualizarEmpleado(empleado);
            
            if (exito) {
                vistaPersonal.mostrarMensaje("Empleado actualizado exitosamente");
                actualizarListaEmpleados();
            } else {
                vistaPersonal.mostrarError("No se pudo actualizar el empleado");
            }
        } catch (SQLException e) {
            manejarErrorSQL(e, "actualizar empleado");
        }
    }

    /**
     * Elimina un empleado del sistema (eliminación lógica)
     * @param id El ID del empleado a eliminar
     */
    public void eliminarEmpleado(int id) {
        try {
            boolean exito = empleadoDAO.eliminarEmpleado(id);
            
            if (exito) {
                vistaPersonal.mostrarMensaje("Empleado marcado como inactivo");
                actualizarListaEmpleados();
            } else {
                vistaPersonal.mostrarError("No se pudo eliminar el empleado");
            }
        } catch (SQLException e) {
            manejarErrorSQL(e, "eliminar empleado");
        }
    }

    /**
     * Busca empleados según un criterio
     * @param criterio Texto para buscar coincidencias
     * @return Lista de empleados que coinciden
     */
    public List<Empleado> buscarEmpleados(String criterio) {
        try {
            List<Empleado> resultados = empleadoDAO.buscarEmpleados(criterio);
            vistaPersonal.mostrarEmpleados(resultados);
            return resultados;
        } catch (SQLException e) {
            manejarErrorSQL(e, "buscar empleados");
            return null;
        }
    }

    /**
     * Actualiza la lista de empleados en la vista
     */
    public void actualizarListaEmpleados() {
        try {
            List<Empleado> empleados = empleadoDAO.obtenerTodosEmpleados();
            vistaPersonal.mostrarEmpleados(empleados);
        } catch (SQLException e) {
            manejarErrorSQL(e, "cargar empleados");
        }
    }

    /**
     * Método para inicializar el controlador
     */
    public void iniciar() {
        actualizarListaEmpleados();
    }

    /**
     * Valida los datos básicos del empleado
     */
    private boolean validarDatosEmpleado(Empleado empleado) {
        return empleado.getNombre() != null && !empleado.getNombre().isEmpty() &&
               empleado.getRol() != null && !empleado.getRol().isEmpty();
    }

    /**
     * Maneja errores de SQL mostrándolos en la vista
     */
    private void manejarErrorSQL(SQLException e, String operacion) {
        String mensaje = "Error al " + operacion + ": ";
        
        // Detectar error de duplicado (RFC o email único)
        if (e.getMessage().contains("Duplicate entry")) {
            if (e.getMessage().contains("rfc")) {
                mensaje += "Ya existe un empleado con este RFC";
            } else if (e.getMessage().contains("email")) {
                mensaje += "Ya existe un empleado con este email";
            } else {
                mensaje += e.getMessage();
            }
        } else {
            mensaje += e.getMessage();
        }
        
        vistaPersonal.mostrarError(mensaje);
    }
	
	public void editarEmpleado(Empleado empleado) {
        try {
            // Validar datos antes de editar
            if (!validarDatosEmpleado(empleado)) {
                vistaPersonal.mostrarError("Datos del empleado incompletos o inválidos");
                return;
            }
            boolean exito = empleadoDAO.actualizarEmpleado(empleado);
            
            if (exito) {
                vistaPersonal.mostrarMensaje("Empleado editado exitosamente");
                actualizarListaEmpleados();
            } else {
                vistaPersonal.mostrarError("No se pudo editar el empleado");
            }
        } catch (SQLException e) {
            manejarErrorSQL(e, "editar empleado");
        }
    }
}
