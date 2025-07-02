package src;

import src.controlador.*;
import src.modelo.*;
import src.vista.*;
import src.servicios.ServicioBaseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/sistema_inventario?useSSL=false&serverTimezone=UTC";
        String usuario = "root";
        String contrasenia = "admin";

        Connection conexion = null;

        try {
            // Inicializar Base de Datos (crea BD, tablas e inserciones iniciales)
            ServicioBaseDatos.inicializarBaseDatos(usuario, contrasenia);

            // Establecer conexión para el resto de la app
            conexion = DriverManager.getConnection(url, usuario, contrasenia);

            // DAOs
            ProductoDAO productoDAO = new ProductoDAO(conexion);
            VentaDAO ventaDAO = new VentaDAO(conexion, productoDAO);
            EmpleadoDAO empleadoDAO = new EmpleadoDAO(conexion);
            ProveedorDAO proveedorDAO = new ProveedorDAO(conexion);
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

            // Vistas
            VistaLogin vistaLogin = new VistaLogin();
            VistaMenuPrincipal vistaMenu = new VistaMenuPrincipal();
            VistaInventario vistaInventario = new VistaInventario();
            VistaVentas vistaVentas = new VistaVentas();
            VistaPersonal vistaPersonal = new VistaPersonal();
            VistaProveedores vistaProveedores = new VistaProveedores();

            // Controladores
            ControladorInventario controladorInventario = new ControladorInventario(productoDAO, vistaInventario);
            ControladorVentas controladorVentas = new ControladorVentas(ventaDAO, productoDAO, vistaVentas);
            ControladorPersonal controladorPersonal = new ControladorPersonal(empleadoDAO, vistaPersonal);
            ControladorProveedores controladorProveedores = new ControladorProveedores(proveedorDAO, vistaProveedores);

            // Controlador principal
            ControladorPrincipal controladorPrincipal = new ControladorPrincipal(
                    vistaLogin, vistaMenu,
                    vistaInventario, vistaVentas,
                    vistaPersonal, vistaProveedores,
                    controladorInventario, controladorVentas,
                    controladorPersonal, controladorProveedores
            );

            // Enlazar vistas
            vistaLogin.setControlador(controladorPrincipal);
            vistaMenu.setControlador(controladorPrincipal);
            vistaInventario.setControlador(controladorInventario);
            vistaVentas.setControlador(controladorVentas);
            vistaPersonal.setControlador(controladorPersonal);
            vistaProveedores.setControlador(controladorProveedores);

            // Iniciar aplicación
            controladorPrincipal.mostrarLogin();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                    "Driver JDBC no encontrado: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
