package src.vista;

import src.controlador.*;
import src.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaVentas {
    private JFrame frame;
    private JTable tablaVentas;
    private JButton botonNuevaVenta;
    private JButton botonDetalles;
    private JButton botonBuscar;
    private JButton botonRegresar;
    private JLabel etiquetaTotal;
    private ControladorVentas controlador;
    private ControladorPrincipal controladorPadre;
    private DefaultTableModel modeloTabla;

    public VistaVentas() {
        inicializarComponentes();
    }

    public void setControladorPadre(ControladorPrincipal controlador) {
        this.controladorPadre = controlador;
    }

    private void inicializarComponentes() {
        // Configuración de la ventana principal
        frame = new JFrame("Gestión de Ventas");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // Crear modelo de tabla para ventas
        String[] columnas = {"ID", "Fecha", "Total", "Items", "Método Pago", "Empleado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };

        // Configurar tabla de ventas
        tablaVentas = new JTable(modeloTabla);
        tablaVentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaVentas);

        // Panel superior con total y botones
        JPanel panelSuperior = new JPanel(new BorderLayout());
        etiquetaTotal = new JLabel("Total del día: $0.00", JLabel.CENTER);
        etiquetaTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(etiquetaTotal, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        botonNuevaVenta = new JButton("Nueva Venta");
        botonNuevaVenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onNuevaVentaClick(e);
            }
        });

        botonDetalles = new JButton("Ver Detalles");
        botonDetalles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onDetallesClick(e);
            }
        });

        botonBuscar = new JButton("Buscar Venta");
        botonBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBuscarClick(e);
            }
        });

        botonRegresar = new JButton("Regresar");
        botonRegresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (controladorPadre != null) {
                    controladorPadre.volverAMenu();
                }
            }
        });

        panelBotones.add(botonNuevaVenta);
        panelBotones.add(botonDetalles);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonRegresar);

        // Agregar componentes a la ventana
        frame.add(panelSuperior, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    public void mostrarVentas(List<Venta> ventas) {
        modeloTabla.setRowCount(0); // Limpiar tabla

        double totalDia = 0;
        for (Venta venta : ventas) {
            Object[] fila = {
                venta.getId(),
                venta.getFecha(),
                String.format("$%.2f", venta.getTotal()),
                venta.getItems().size(),
                venta.getMetodoPago(),
                "Empleado #" + venta.getEmpleadoId()
            };
            modeloTabla.addRow(fila);
            totalDia += venta.getTotal();
        }

        etiquetaTotal.setText(String.format("Total del día: $%.2f", totalDia));
    }

    private void onNuevaVentaClick(ActionEvent e) {
        JDialog dialogo = new JDialog(frame, "Nueva Venta", true);
        dialogo.setSize(800, 500);
        dialogo.setLayout(new BorderLayout());

        // Panel para items de venta
        DefaultTableModel modeloItems = new DefaultTableModel(
            new String[]{"ID", "Producto", "Precio", "Cantidad", "Subtotal"}, 0);
        JTable tablaItems = new JTable(modeloItems);
        JScrollPane scrollItems = new JScrollPane(tablaItems);

        // Panel para controles
        JPanel panelControles = new JPanel(new GridLayout(1, 3, 5, 5));

        JButton botonAgregarItem = new JButton("Agregar Producto");
        JButton botonEliminarItem = new JButton("Quitar Producto");
        JComboBox<String> comboMetodoPago = new JComboBox<>(new String[]{"EFECTIVO", "TARJETA", "TRANSFERENCIA"});

        panelControles.add(botonAgregarItem);
        panelControles.add(botonEliminarItem);
        panelControles.add(comboMetodoPago);

        // Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel etiquetaTotalVenta = new JLabel("Total: $0.00");
        JButton botonConfirmar = new JButton("Confirmar Venta");
        JButton botonCancelar = new JButton("Cancelar");

        panelInferior.add(etiquetaTotalVenta);
        panelInferior.add(botonConfirmar);
        panelInferior.add(botonCancelar);

        // Agregar componentes al diálogo
        dialogo.add(scrollItems, BorderLayout.CENTER);
        dialogo.add(panelControles, BorderLayout.NORTH);
        dialogo.add(panelInferior, BorderLayout.SOUTH);

        // Configurar acciones
        botonAgregarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                // Diálogo para seleccionar producto y cantidad
                // Implementar lógica para agregar items a la venta
            }
        });

        botonEliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                // Implementar lógica para quitar items seleccionados
                int filaSeleccionada = tablaItems.getSelectedRow();
                if (filaSeleccionada != -1) {
                    ((DefaultTableModel) tablaItems.getModel()).removeRow(filaSeleccionada);
                    // Actualizar total
                    actualizarTotalVenta(tablaItems, etiquetaTotalVenta);
                } else {
                    mostrarMensaje("Seleccione un producto para quitar.");
                }
            }
        });

        botonConfirmar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                // Crear objeto Venta y llamar al controlador
                dialogo.dispose();
            }
        });

        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                dialogo.dispose();
            }
        });

        dialogo.setVisible(true);
    }

    private void actualizarTotalVenta(JTable tablaItems, JLabel etiquetaTotalVenta) {
        double total = 0;
        DefaultTableModel modelo = (DefaultTableModel) tablaItems.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += (double) modelo.getValueAt(i, 4); // Suponiendo que el subtotal está en la columna 4
        }
        etiquetaTotalVenta.setText(String.format("Total: $%.2f", total));
    }

    private void onDetallesClick(ActionEvent e) {
        int filaSeleccionada = tablaVentas.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione una venta para ver detalles",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idVenta = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        // Llamar al controlador para obtener detalles de la venta
        // Mostrar diálogo con los detalles completos
    }

    private void onBuscarClick(ActionEvent e) {
        String criterio = JOptionPane.showInputDialog(frame, "Ingrese ID de venta o fecha (yyyy-mm-dd):");
        if (criterio != null && !criterio.isEmpty() && controlador != null) {
            // Llamar al controlador para buscar ventas
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setControlador(ControladorVentas controlador) {
        this.controlador = controlador;
    }

    public void limpiarFormulario() {
        // Limpiar tabla de items de venta
        modeloTabla.setRowCount(0);

        // Restablecer total a 0
        etiquetaTotal.setText("Total del día: $0.00");
    }
}
