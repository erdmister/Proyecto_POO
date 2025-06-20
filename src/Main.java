import controlador.*;
import modelo.*;
import vista.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        // Configuración de la conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/sistema_inventario";
        String usuario = "local";//tu_usuario
        String contraseña = "123456.d.a1.";//tu_contraseña
        
        try {
            // Registrar el driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión
            try (Connection conexion = DriverManager.getConnection(url, usuario, contraseña)) {
                // Inicialización de DAOs
                ProductoDAO productoDAO = new ProductoDAO(conexion);
                VentaDAO ventaDAO = new VentaDAO(conexion);
                EmpleadoDAO empleadoDAO = new EmpleadoDAO(conexion);
                ProveedorDAO proveedorDAO = new ProveedorDAO(conexion);
                UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
                
                // Inicialización de Vistas
                VistaLogin vistaLogin = new VistaLogin();
                VistaMenuPrincipal vistaMenu = new VistaMenuPrincipal();
                VistaInventario vistaInventario = new VistaInventario();
                VistaVentas vistaVentas = new VistaVentas();
                VistaPersonal vistaPersonal = new VistaPersonal();
                VistaProveedores vistaProveedores = new VistaProveedores();
                
                // Inicialización de Controladores
                ControladorInventario controladorInventario = new ControladorInventario(productoDAO, vistaInventario);
                ControladorVentas controladorVentas = new ControladorVentas(ventaDAO, productoDAO, vistaVentas);
                ControladorPersonal controladorPersonal = new ControladorPersonal(empleadoDAO, vistaPersonal);
                ControladorProveedores controladorProveedores = new ControladorProveedores(proveedorDAO, vistaProveedores);
                
                // Configurar controlador principal
                ControladorPrincipal controladorPrincipal = new ControladorPrincipal(
                    vistaLogin, 
                    vistaMenu,
                    controladorInventario,
                    controladorVentas,
                    controladorPersonal,
                    controladorProveedores
                );
                
                // Establecer relaciones entre vistas y controladores
                vistaLogin.setControlador(controladorPrincipal);
                vistaMenu.setControlador(controladorPrincipal);
                vistaInventario.setControlador(controladorInventario);
                vistaVentas.setControlador(controladorVentas);
                vistaPersonal.setControlador(controladorPersonal);
                vistaProveedores.setControlador(controladorProveedores);
                
                // Iniciar la aplicación
                controladorPrincipal.mostrarLogin();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, 
                    "Error al conectar con la base de datos: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Driver JDBC no encontrado: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
