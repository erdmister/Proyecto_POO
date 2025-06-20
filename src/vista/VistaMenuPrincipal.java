package vista;

import controlador.*;
import modelo.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VistaMenuPrincipal {
    private JFrame frame;
    private JButton botonInventario;
    private JButton botonPersonal;
    private JButton botonVentas;
    private JButton botonProveedores;
    private ControladorPrincipal controlador;

    public VistaMenuPrincipal() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Configuración de la ventana principal
        frame = new JFrame("Menú Principal - Sistema de Inventario");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(4, 1, 10, 10));
        
        // Panel para contener los botones con márgenes
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Botón de Inventario
        botonInventario = new JButton("Gestión de Inventario");
        botonInventario.setFont(new Font("Arial", Font.BOLD, 14));
        botonInventario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onInventarioClick();
            }
        });

        // Botón de Ventas
        botonVentas = new JButton("Gestión de Ventas");
        botonVentas.setFont(new Font("Arial", Font.BOLD, 14));
        botonVentas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onVentasClick();
            }
        });

        // Botón de Personal
        botonPersonal = new JButton("Gestión de Personal");
        botonPersonal.setFont(new Font("Arial", Font.BOLD, 14));
        botonPersonal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPersonalClick();
            }
        });

        // Botón de Proveedores
        botonProveedores = new JButton("Gestión de Proveedores");
        botonProveedores.setFont(new Font("Arial", Font.BOLD, 14));
        botonProveedores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onProveedoresClick();
            }
        });

        // Agregar botones al panel
        panelBotones.add(botonInventario);
        panelBotones.add(botonVentas);
        panelBotones.add(botonPersonal);
        panelBotones.add(botonProveedores);

        // Agregar panel a la ventana
        frame.add(panelBotones);
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    public void ocultar() {
        frame.setVisible(false);
    }

    public void onInventarioClick() {
        if (controlador != null) {
            controlador.mostrarVistaInventario();
        }
    }

    public void onVentasClick() {
        if (controlador != null) {
            controlador.mostrarVistaVentas();
        }
    }

    public void onPersonalClick() {
        if (controlador != null) {
            controlador.mostrarVistaPersonal();
        }
    }

    public void onProveedoresClick() {
        if (controlador != null) {
            controlador.mostrarVistaProveedores();
        }
    }

    public void setControlador(ControladorPrincipal controlador) {
        this.controlador = controlador;
    }

    /**
     * Habilita o deshabilita opciones según el rol del usuario
     * @param rol El rol del usuario (ADMIN, GERENTE, VENDEDOR)
     */
    public void habilitarOpcionesPorRol(String rol) {
        switch (rol) {
            case "ADMIN":
                botonInventario.setEnabled(true);
                botonVentas.setEnabled(true);
                botonPersonal.setEnabled(true);
                botonProveedores.setEnabled(true);
                break;
            case "GERENTE":
                botonInventario.setEnabled(true);
                botonVentas.setEnabled(true);
                botonPersonal.setEnabled(false);
                botonProveedores.setEnabled(true);
                break;
            case "VENDEDOR":
                botonInventario.setEnabled(false);
                botonVentas.setEnabled(true);
                botonPersonal.setEnabled(false);
                botonProveedores.setEnabled(false);
                break;
            default:
                botonInventario.setEnabled(false);
                botonVentas.setEnabled(false);
                botonPersonal.setEnabled(false);
                botonProveedores.setEnabled(false);
        }
    }
}
