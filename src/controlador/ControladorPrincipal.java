import modelo.*;
import vista.*;

public class ControladorPrincipal {
    private VistaLogin vistaLogin;
    private VistaMenuPrincipal vistaMenu;
    private ControladorInventario controladorInventario;
    private ControladorVentas controladorVentas;
    private ControladorPersonal controladorPersonal;
    private ControladorProveedores controladorProveedores;

    // Constructor
    public ControladorPrincipal(VistaLogin vistaLogin, VistaMenuPrincipal vistaMenu,
                                ControladorInventario controladorInventario,
                                ControladorVentas controladorVentas,
                                ControladorPersonal controladorPersonal,
                                ControladorProveedores controladorProveedores) {
        this.vistaLogin = vistaLogin;
        this.vistaMenu = vistaMenu;
        this.controladorInventario = controladorInventario;
        this.controladorVentas = controladorVentas;
        this.controladorPersonal = controladorPersonal;
        this.controladorProveedores = controladorProveedores;

        // Configurar eventos de la vista de login
        this.vistaLogin.setControlador(this);
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
        if (rolUsuario.equals("ADMIN")) {
            vistaMenu.habilitarOpcionesAdministrativas();
        } else if (rolUsuario.equals("VENDEDOR")) {
            vistaMenu.habilitarOpcionesVentas();
        } else if (rolUsuario.equals("GERENTE")) {
            vistaMenu.habilitarOpcionesGerenciales();
        }
    }

    /**
     * Muestra la vista de inventario
     */
    public void mostrarVistaInventario() {
        controladorInventario.actualizarVista();
        vistaMenu.ocultar();
        vistaInventario.mostrar();
    }

    /**
     * Muestra la vista de ventas
     */
    public void mostrarVistaVentas() {
        controladorVentas.obtenerVentasDiarias(); // Cargar ventas del día
        vistaMenu.ocultar();
        vistaVentas.mostrar();
    }

    /**
     * Muestra la vista de personal
     */
    public void mostrarVistaPersonal() {
        controladorPersonal.actualizarListaEmpleados(); // Cargar empleados
        vistaMenu.ocultar();
        vistaPersonal.mostrar();
    }

    /**
     * Muestra la vista de proveedores
     */
    public void mostrarVistaProveedores() {
        controladorProveedores.actualizarListaProveedores(); // Cargar proveedores
        vistaMenu.ocultar();
        vistaProveedores.mostrar();
    }
}
