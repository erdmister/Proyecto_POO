package vista;

import controlador.*;
import modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VistaProveedores {
    private JFrame frame;
    private JTable tablaProveedores;
    private JButton botonAgregar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonBuscar;
    private JButton botonRegresar;
    private ControladorProveedores controlador;
    private DefaultTableModel modeloTabla;

    public VistaProveedores() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Configuración de la ventana
        frame = new JFrame("Gestión de Proveedores");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLayout(new BorderLayout(10, 10));

        // Modelo de tabla
        String[] columnas = {"ID", "Nombre", "RUC", "Teléfono", "Email", "Productos Suministrados"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Configurar tabla
        tablaProveedores = new JTable(modeloTabla);
        tablaProveedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaProveedores);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        // Botón Agregar
        botonAgregar = new JButton("Agregar");
        botonAgregar.setFont(new Font("Arial", Font.PLAIN, 14));
        botonAgregar.addActionListener(this::onAgregarClick);
        
        // Botón Editar
        botonEditar = new JButton("Editar");
        botonEditar.setFont(new Font("Arial", Font.PLAIN, 14));
        botonEditar.addActionListener(this::onEditarClick);
        
        // Botón Eliminar
        botonEliminar = new JButton("Eliminar");
        botonEliminar.setFont(new Font("Arial", Font.PLAIN, 14));
        botonEliminar.addActionListener(this::onEliminarClick);
        
        // Botón Buscar
        botonBuscar = new JButton("Buscar");
        botonBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        botonBuscar.addActionListener(this::onBuscarClick);
        
        // Botón Regresar
        botonRegresar = new JButton("Regresar");
        botonRegresar.setFont(new Font("Arial", Font.PLAIN, 14));
        botonRegresar.addActionListener(e -> frame.dispose());

        // Agregar botones al panel
        panelBotones.add(botonAgregar);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonBuscar);
        panelBotones.add(botonRegresar);

        // Agregar componentes a la ventana
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);
    }

    public void mostrar() {
        frame.setVisible(true);
    }

    public void mostrarProveedores(List<Proveedor> proveedores) {
        modeloTabla.setRowCount(0); // Limpiar tabla
        
        for (Proveedor proveedor : proveedores) {
            String productos = String.join(", ", proveedor.getProductosSuministrados());
            
            Object[] fila = {
                proveedor.getId(),
                proveedor.getNombre(),
                proveedor.getRuc(),
                proveedor.getTelefono(),
                proveedor.getEmail(),
                productos.length() > 50 ? productos.substring(0, 47) + "..." : productos
            };
            modeloTabla.addRow(fila);
        }
    }

    private void onAgregarClick(ActionEvent e) {
        // Diálogo para agregar proveedor
        JDialog dialogo = new JDialog(frame, "Agregar Proveedor", true);
        dialogo.setSize(500, 350);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setLocationRelativeTo(frame);

        // Componentes del formulario
        JTextField campoNombre = new JTextField();
        JTextField campoRuc = new JTextField();
        JTextField campoTelefono = new JTextField();
        JTextField campoEmail = new JTextField();
        JTextField campoDireccion = new JTextField();
        JTextArea areaProductos = new JTextArea(3, 20);
        areaProductos.setLineWrap(true);
        JScrollPane scrollProductos = new JScrollPane(areaProductos);

        // Agregar componentes al diálogo
        agregarCampo(dialogo, "Nombre:", campoNombre);
        agregarCampo(dialogo, "RUC:", campoRuc);
        agregarCampo(dialogo, "Teléfono:", campoTelefono);
        agregarCampo(dialogo, "Email:", campoEmail);
        agregarCampo(dialogo, "Dirección:", campoDireccion);
        dialogo.add(new JLabel("Productos (separados por coma):"));
        dialogo.add(scrollProductos);

        // Botones
        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.addActionListener(ev -> {
            Proveedor nuevo = new Proveedor();
            nuevo.setNombre(campoNombre.getText());
            nuevo.setRuc(campoRuc.getText());
            nuevo.setTelefono(campoTelefono.getText());
            nuevo.setEmail(campoEmail.getText());
            nuevo.setDireccion(campoDireccion.getText());
            nuevo.setProductosSuministrados(List.of(areaProductos.getText().split(",")));

            if (controlador != null) {
                controlador.agregarProveedor(nuevo);
            }
            dialogo.dispose();
        });

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(ev -> dialogo.dispose());

        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void onEditarClick(ActionEvent e) {
        int filaSeleccionada = tablaProveedores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un proveedor para editar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener ID del proveedor seleccionado
        int idProveedor = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        
        // En una implementación real, aquí se obtendría el proveedor completo del controlador
        // Por ahora creamos un diálogo vacío
        JDialog dialogoEditar = new JDialog(frame, "Editar Proveedor", true);
        // ... (Implementación similar a onAgregarClick pero con datos precargados)
        dialogoEditar.setVisible(true);
    }

    private void onEliminarClick(ActionEvent e) {
        int filaSeleccionada = tablaProveedores.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un proveedor para eliminar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idProveedor = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreProveedor = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(
            frame, 
            "¿Está seguro de eliminar al proveedor: " + nombreProveedor + "?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION && controlador != null) {
            controlador.eliminarProveedor(idProveedor);
        }
    }

    private void onBuscarClick(ActionEvent e) {
        String criterio = JOptionPane.showInputDialog(frame, "Ingrese nombre, RUC o email:");
        if (criterio != null && !criterio.isEmpty() && controlador != null) {
            List<Proveedor> resultados = controlador.buscarProveedores(criterio);
            mostrarProveedores(resultados);
        }
    }

    private void agregarCampo(JDialog dialogo, String etiqueta, JComponent componente) {
        dialogo.add(new JLabel(etiqueta, JLabel.RIGHT));
        dialogo.add(componente);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setControlador(ControladorProveedores controlador) {
        this.controlador = controlador;
    }
}
