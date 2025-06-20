package src.vista;

import src.controlador.*;
import src.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VistaPersonal {
    private JFrame frame;
    private JTable tablaEmpleados;
    private JButton botonAgregar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonBuscar;
    private JButton botonRegresar;
    private ControladorPersonal controlador;
    private DefaultTableModel modeloTabla;

    // Campos para el formulario de agregar/editar empleado
    private JTextField campoNombre;
    private JTextField campoRfc;
    private JTextField campoTelefono;
    private JTextField campoEmail;
    private JComboBox<String> comboRol;
    private JTextField campoSucursal;
    private JCheckBox checkActivo;

    public VistaPersonal() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Configuración de la ventana
        frame = new JFrame("Gestión de Personal");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 500);
        frame.setLayout(new BorderLayout(10, 10));

        // Crear modelo de tabla
        String[] columnas = {"ID", "Nombre", "RFC", "Teléfono", "Email", "Rol", "Sucursal", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable directamente
            }
        };

        // Configurar tabla
        tablaEmpleados = new JTable(modeloTabla);
        tablaEmpleados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaEmpleados);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        botonAgregar = new JButton("Agregar");
        botonAgregar.addActionListener(this::onAgregarClick);
        
        botonEditar = new JButton("Editar");
        botonEditar.addActionListener(this::onEditarClick);
        
        botonEliminar = new JButton("Eliminar");
        botonEliminar.addActionListener(this::onEliminarClick);
        
        botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(this::onBuscarClick);
        
        botonRegresar = new JButton("Regresar");
        botonRegresar.addActionListener(e -> frame.dispose());

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

    public void mostrarEmpleados(List<Empleado> empleados) {
        modeloTabla.setRowCount(0); // Limpiar tabla
        
        for (Empleado empleado : empleados) {
            Object[] fila = {
                empleado.getId(),
                empleado.getNombre(),
                empleado.getRfc(),
                empleado.getTelefono(),
                empleado.getEmail(),
                empleado.getRol(),
                empleado.getSucursal(),
                empleado.isActivo() ? "Activo" : "Inactivo"
            };
            modeloTabla.addRow(fila);
        }
    }

    private void onAgregarClick(ActionEvent e) {
        JDialog dialogo = new JDialog(frame, "Agregar Empleado", true);
        dialogo.setLayout(new GridLayout(8, 2, 5, 5));
        dialogo.setSize(500, 400);

        // Inicializar campos del formulario
        campoNombre = new JTextField();
        campoRfc = new JTextField();
        campoTelefono = new JTextField();
        campoEmail = new JTextField();
        comboRol = new JComboBox<>(new String[]{"ADMIN", "GERENTE", "VENDEDOR"});
        campoSucursal = new JTextField();
        checkActivo = new JCheckBox("Activo", true);

        // Agregar componentes al diálogo
        agregarCampo(dialogo, "Nombre:", campoNombre);
        agregarCampo(dialogo, "RFC:", campoRfc);
        agregarCampo(dialogo, "Teléfono:", campoTelefono);
        agregarCampo(dialogo, "Email:", campoEmail);
        agregarCampo(dialogo, "Rol:", comboRol);
        agregarCampo(dialogo, "Sucursal:", campoSucursal);
        dialogo.add(new JLabel("Estado:"));
        dialogo.add(checkActivo);

        JButton botonConfirmar = new JButton("Guardar");
        botonConfirmar.addActionListener(ev -> {
            Empleado nuevo = new Empleado(
                0, // ID se genera automáticamente
                campoNombre.getText(),
                campoRfc.getText(),
                campoTelefono.getText(),
                comboRol.getSelectedItem().toString(),
                campoSucursal.getText(),
                campoEmail.getText(),
                checkActivo.isSelected()
            );
            
            if (controlador != null) {
                controlador.agregarEmpleado(nuevo);
            }
            dialogo.dispose();
        });

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(ev -> dialogo.dispose());

        dialogo.add(botonConfirmar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void onEditarClick(ActionEvent e) {
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un empleado para editar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos del empleado seleccionado
        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        String rfc = (String) modeloTabla.getValueAt(filaSeleccionada, 2);
        String telefono = (String) modeloTabla.getValueAt(filaSeleccionada, 3);
        String email = (String) modeloTabla.getValueAt(filaSeleccionada, 4);
        String rol = (String) modeloTabla.getValueAt(filaSeleccionada, 5);
        String sucursal = (String) modeloTabla.getValueAt(filaSeleccionada, 6);
        boolean activo = modeloTabla.getValueAt(filaSeleccionada, 7).equals("Activo");

        // Diálogo para editar empleado
        JDialog dialogo = new JDialog(frame, "Editar Empleado", true);
        dialogo.setLayout(new GridLayout(8, 2, 5, 5));
        dialogo.setSize(500, 400);

        // Inicializar campos del formulario con datos existentes
        campoNombre = new JTextField(nombre);
        campoRfc = new JTextField(rfc);
        campoTelefono = new JTextField(telefono);
        campoEmail = new JTextField(email);
        comboRol = new JComboBox<>(new String[]{"ADMIN", "GERENTE", "VENDEDOR"});
        comboRol.setSelectedItem(rol);
        campoSucursal = new JTextField(sucursal);
        checkActivo = new JCheckBox("Activo", activo);

        // Agregar componentes al diálogo
        agregarCampo(dialogo, "Nombre:", campoNombre);
        agregarCampo(dialogo, "RFC:", campoRfc);
        agregarCampo(dialogo, "Teléfono:", campoTelefono);
        agregarCampo(dialogo, "Email:", campoEmail);
        agregarCampo(dialogo, "Rol:", comboRol);
        agregarCampo(dialogo, "Sucursal:", campoSucursal);
        dialogo.add(new JLabel("Estado:"));
        dialogo.add(checkActivo);

        JButton botonConfirmar = new JButton("Guardar");
        botonConfirmar.addActionListener(ev -> {
            Empleado empleadoEditado = new Empleado(
                id, // ID existente
                campoNombre.getText(),
                campoRfc.getText(),
                campoTelefono.getText(),
                comboRol.getSelectedItem().toString(),
                campoSucursal.getText(),
                campoEmail.getText(),
                checkActivo.isSelected()
            );
            
            if (controlador != null) {
                controlador.editarEmpleado(empleadoEditado);
            }
            dialogo.dispose();
        });

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(ev -> dialogo.dispose());

        dialogo.add(botonConfirmar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void onEliminarClick(ActionEvent e) {
        int filaSeleccionada = tablaEmpleados.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un empleado para eliminar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(
            frame, 
            "¿Está seguro de marcar como inactivo al empleado: " + nombre + "?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION && controlador != null) {
            controlador.eliminarEmpleado(id);
        }
    }

    private void onBuscarClick(ActionEvent e) {
        String criterio = JOptionPane.showInputDialog(frame, "Ingrese nombre, RFC o email del empleado:");
        if (criterio != null && !criterio.isEmpty() && controlador != null) {
            List<Empleado> resultados = controlador.buscarEmpleados(criterio);
            mostrarEmpleados(resultados);
        }
    }

    private void agregarCampo(JDialog dialogo, String etiqueta, JComponent componente) {
        dialogo.add(new JLabel(etiqueta));
        dialogo.add(componente);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setControlador(ControladorPersonal controlador) {
        this.controlador = controlador;
    }
	
    public void limpiarFormulario() {
        // Limpiar los campos del formulario
        if (campoNombre != null) campoNombre.setText("");
        if (campoRfc != null) campoRfc.setText("");
        if (campoTelefono != null) campoTelefono.setText("");
        if (campoEmail != null) campoEmail.setText("");
        if (campoSucursal != null) campoSucursal.setText("");
        if (checkActivo != null) checkActivo.setSelected(true); // Restablecer a "Activo"
        
        // Opcional: Deseleccionar fila en tabla si existe
        tablaEmpleados.clearSelection();
    }
}
