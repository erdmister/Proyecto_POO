package vista;

import controlador.*;
import modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaLogin {
    private JFrame frame;
    private JTextField campoUsuario;
    private JPasswordField campoContraseña;
    private JButton botonLogin;
    private ControladorPrincipal controlador;

    // Constructor
    public VistaLogin() {
        inicializarComponentes();
    }

    /**
     * Inicializa los componentes de la vista
     */
    private void inicializarComponentes() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        JLabel etiquetaUsuario = new JLabel("Usuario:");
        campoUsuario = new JTextField();
        
        JLabel etiquetaContraseña = new JLabel("Contraseña:");
        campoContraseña = new JPasswordField();
        
        botonLogin = new JButton("Iniciar Sesión");
        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLoginClick();
            }
        });

        frame.add(etiquetaUsuario);
        frame.add(campoUsuario);
        frame.add(etiquetaContraseña);
        frame.add(campoContraseña);
        frame.add(new JLabel()); // Espacio vacío
        frame.add(botonLogin);
    }

    /**
     * Muestra la ventana de login
     */
    public void mostrar() {
        frame.setVisible(true);
    }

    /**
     * Oculta la ventana de login
     */
    public void ocultar() {
        frame.setVisible(false);
    }

    /**
     * Maneja el evento de clic en el botón de login
     */
    public void onLoginClick() {
        String usuario = campoUsuario.getText();
        String contraseña = new String(campoContraseña.getPassword());

        // Aquí se debería validar el usuario y la contraseña
        // Simulamos un login exitoso con rol "ADMIN"
        if (usuario.equals("admin") && contraseña.equals("admin123")) {
            controlador.onLoginExitoso("ADMIN");
        } else {
            JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Establece el controlador para manejar eventos
     * @param controlador Controlador principal
     */
    public void setControlador(ControladorPrincipal controlador) {
        this.controlador = controlador;
    }
}
