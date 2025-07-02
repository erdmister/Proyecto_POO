package src.controlador;

import src.modelo.*;
import src.vista.*;
import java.sql.SQLException;

public class ControladorPrincipal {
    private VistaLogin vistaLogin;
    private VistaMenuPrincipal vistaMenu;
    private VistaInventario vistaInventario; // Agregar vistaInventario
    private VistaVentas vistaVentas; // Agregar vistaVentas
    private VistaPersonal vistaPersonal; // Agregar vistaPersonal
    private VistaProveedores vistaProveedores; // Agregar vistaProveedores
    private ControladorInventario controladorInventario;
    private ControladorVentas controladorVentas;
    private ControladorPersonal controladorPersonal;
    private ControladorProveedores controladorProveedores;

    // Constructor
    public ControladorPrincipal(VistaLogin vistaLogin, VistaMenuPrincipal vistaMenu,
                                VistaInventario vistaInventario, // Agregar parámetro
                                VistaVentas vistaVentas, // Agregar parámetro
                                VistaPersonal vistaPersonal, // Agregar parámetro
                                VistaProveedores vistaProveedores, // Agregar parámetro
                                ControladorInventario controladorInventario,
                                ControladorVentas controladorVentas,
                                ControladorPersonal controladorPersonal,
                                ControladorProveedores controladorProveedores) {
        this.vistaLogin = vistaLogin;
        this.vistaMenu = vistaMenu;
        this.vistaInventario = vistaInventario; // Inicializar
        this.vistaVentas = vistaVentas; // Inicializar
        this.vistaPersonal = vistaPersonal; // Inicializar
        this.vistaProveedores = vistaProveedores; // Inicializar
        this.controladorInventario = controladorInventario;
        this.controladorVentas = controladorVentas;
        this.controladorPersonal = controladorPersonal;
        this.controladorProveedores = controladorProveedores;

        // Configurar eventos de la vista de login
        this.vistaLogin.setControlador(this);
        this.vistaMenu.setControlador(this); // Asegúrate de que el controlador se establezca en la vista del menú
    }

    /**
     * Muestra la vista de login
     */
    public void mostrarLogin() {
        vistaLogin.mostrar();
    }

    /**
     * Maneja el evento de login exitoso
     * @param rolUsuario Rol del usuario que ha iniciado sesión
     */
    public void onLoginExitoso(String rolUsuario) {
        vistaLogin.ocultar();
        vistaMenu.mostrar();

        // Configurar el menú según el rol del usuario
        configurarMenuPorRol(rolUsuario);
    }

    /**
     * Configura el menú principal según el rol del usuario
     * @param rolUsuario Rol del usuario
     */
    private void configurarMenuPorRol(String rolUsuario) {
        vistaMenu.habilitarOpcionesPorRol(rolUsuario);
    }
	
	public void volverAMenu() {
		vistaMenu.mostrar();
	}

    /**
     * Muestra la vista de inventario
     */
    public void mostrarVistaInventario() {
        controladorInventario.actualizarVista();
        vistaMenu.ocultar();
		vistaInventario.setControladorPadre(this);  // Nueva línea
        vistaInventario.mostrar();
    }

    /**
     * Muestra la vista de ventas
     */
    public void mostrarVistaVentas() {
        controladorVentas.obtenerVentasDiarias(); // Cargar ventas del día
        vistaMenu.ocultar();
		vistaVentas.setControladorPadre(this);  // Nueva línea
        vistaVentas.mostrar();
    }

    /**
     * Muestra la vista de personal
     */
    public void mostrarVistaPersonal() {
        controladorPersonal.actualizarListaEmpleados(); // Cargar empleados
        vistaMenu.ocultar();
		vistaPersonal.setControladorPadre(this);
        vistaPersonal.mostrar();
    }

    /**
     * Muestra la vista de proveedores
     */
    public void mostrarVistaProveedores() {
        controladorProveedores.actualizarListaProveedores(); // Cargar proveedores
        vistaMenu.ocultar();
		vistaProveedores.setControladorPadre(this);
        vistaProveedores.mostrar();
    }
}
