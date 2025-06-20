package src.vista;

import src.controlador.*;
import src.modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VistaInventario {
    private JFrame frame;
    private JTable tablaProductos;
    private JButton botonAgregar;
    private JButton botonEditar;
    private JButton botonEliminar;
    private JButton botonBuscar;
    private JButton botonRegresar;
    private ControladorInventario controlador;
    private DefaultTableModel modeloTabla;

    public VistaInventario() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Configuración de la ventana
        frame = new JFrame("Gestión de Inventario");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout(10, 10));

        // Crear modelo de tabla
        String[] columnas = {"ID", "Nombre", "Precio", "Costo", "Stock", "Fecha Caducidad"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable directamente
            }
        };

        // Configurar tabla
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaProductos);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        botonAgregar = new JButton("Agregar");
        botonAgregar.addActionListener(e -> onAgregarClick());
        
        botonEditar = new JButton("Editar");
        botonEditar.addActionListener(e -> onEditarClick());
        
        botonEliminar = new JButton("Eliminar");
        botonEliminar.addActionListener(e -> onEliminarClick());
        
        botonBuscar = new JButton("Buscar");
        botonBuscar.addActionListener(e -> onBuscarClick());
        
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

    public void mostrarProductos(List<Producto> productos) {
        modeloTabla.setRowCount(0); // Limpiar tabla
        
        for (Producto producto : productos) {
            Object[] fila = {
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCosto(),
                producto.getStock(),
                producto.getFechaCaducidad()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void onAgregarClick() {
        // Diálogo para agregar nuevo producto
        JDialog dialogo = new JDialog(frame, "Agregar Producto", true);
        dialogo.setLayout(new GridLayout(6, 2, 5, 5));
        dialogo.setSize(400, 300);

        JTextField campoNombre = new JTextField();
        JTextField campoPrecio = new JTextField();
        JTextField campoCosto = new JTextField();
        JTextField campoStock = new JTextField();
        JTextField campoFecha = new JTextField();

        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Precio:"));
        dialogo.add(campoPrecio);
        dialogo.add(new JLabel("Costo:"));
        dialogo.add(campoCosto);
        dialogo.add(new JLabel("Stock:"));
        dialogo.add(campoStock);
        dialogo.add(new JLabel("Fecha Caducidad (yyyy-mm-dd):"));
        dialogo.add(campoFecha);

        JButton botonConfirmar = new JButton("Guardar");
        botonConfirmar.addActionListener(e -> {
            try {
                Producto nuevo = new Producto(
                    0, // ID se genera automáticamente
                    campoNombre.getText(),
                    Double.parseDouble(campoPrecio.getText()),
                    Double.parseDouble(campoCosto.getText()),
                    Integer.parseInt(campoStock.getText()),
                    java.sql.Date.valueOf(campoFecha.getText())
                );
                
                if (controlador != null) {
                    controlador.agregarProducto(nuevo);
                }
                dialogo.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error en los datos: " + ex.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.add(botonConfirmar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void onEditarClick() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un producto para editar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obtener datos del producto seleccionado
        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
        double precio = (double) modeloTabla.getValueAt(filaSeleccionada, 2);
        double costo = (double) modeloTabla.getValueAt(filaSeleccionada, 3);
        int stock = (int) modeloTabla.getValueAt(filaSeleccionada, 4);
        java.sql.Date fechaCaducidad = (java.sql.Date) modeloTabla.getValueAt(filaSeleccionada, 5);

        // Diálogo para editar producto
        JDialog dialogo = new JDialog(frame, "Editar Producto", true);
        dialogo.setLayout(new GridLayout(6, 2, 5, 5));
        dialogo.setSize(400, 300);

        JTextField campoNombre = new JTextField(nombre);
        JTextField campoPrecio = new JTextField(String.valueOf(precio));
        JTextField campoCosto = new JTextField(String.valueOf(costo));
        JTextField campoStock = new JTextField(String.valueOf(stock));
        JTextField campoFecha = new JTextField(fechaCaducidad.toString());

        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Precio:"));
        dialogo.add(campoPrecio);
        dialogo.add(new JLabel("Costo:"));
        dialogo.add(campoCosto);
        dialogo.add(new JLabel("Stock:"));
        dialogo.add(campoStock);
        dialogo.add(new JLabel("Fecha Caducidad (yyyy-mm-dd):"));
        dialogo.add(campoFecha);

        JButton botonConfirmar = new JButton("Guardar");
        botonConfirmar.addActionListener(e -> {
            try {
                Producto editado = new Producto(
                    id,
                    campoNombre.getText(),
                    Double.parseDouble(campoPrecio.getText()),
                    Double.parseDouble(campoCosto.getText()),
                    Integer.parseInt(campoStock.getText()),
                    java.sql.Date.valueOf(campoFecha.getText())
                );
                
                if (controlador != null) {
                    controlador.actualizarProducto(editado);
                }
                dialogo.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error en los datos: " + ex.getMessage(), 
                                            "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dialogo.dispose());

        dialogo.add(botonConfirmar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void onEliminarClick() {
        int filaSeleccionada = tablaProductos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un producto para eliminar", 
                                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(
            frame, 
            "¿Está seguro de eliminar el producto: " + nombre + "?", 
            "Confirmar Eliminación", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION && controlador != null) {
            controlador.eliminarProducto(id);
        }
    }

    private void onBuscarClick() {
        String criterio = JOptionPane.showInputDialog(frame, "Ingrese nombre o ID del producto:");
        if (criterio != null && !criterio.isEmpty() && controlador != null) {
            List<Producto> resultados = controlador.buscarProductos(criterio);
            mostrarProductos(resultados);
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void setControlador(ControladorInventario controlador) {
        this.controlador = controlador;
    }
	
    public void limpiarFormulario() {
        // Limpiar la tabla de productos
        modeloTabla.setRowCount(0);
        
        // Opcional: mostrar todos los productos nuevamente
        // controlador.mostrarTodosProductos();
    }
}
