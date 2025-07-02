package src.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Venta {
    private int id;
    private Date fecha;
    private List<ItemVenta> items;
    private double total;
    private int empleadoId;
    private String metodoPago;

    // Constructores
    public Venta() {
        this.items = new ArrayList<>();
        this.fecha = new Date();
    }

    public Venta(int id, Date fecha, List<ItemVenta> items, int empleadoId, String metodoPago) {
        this.id = id;
        this.fecha = fecha;
        this.items = items;
        this.empleadoId = empleadoId;
        this.metodoPago = metodoPago;
        calcularTotal(); // Calcular el total al crear la venta
    }

    // Métodos para manipular items
    public void agregarItem(ItemVenta item) {
        this.items.add(item);
        calcularTotal(); // Actualiza el total al agregar un ítem
    }

    public void quitarItem(ItemVenta item) {
        this.items.remove(item);
        calcularTotal(); // Actualiza el total al quitar un ítem
    }

    private void calcularTotal() {
        this.total = items.stream()
                .mapToDouble(ItemVenta::getSubtotal) // Suma los subtotales de los ítems
                .sum();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<ItemVenta> getItems() {
        return items;
    }

    public void setItems(List<ItemVenta> items) {
        this.items = items;
        calcularTotal(); // Actualiza el total al establecer una nueva lista de ítems
    }

    public double getTotal() {
        return total; // Devuelve el total calculado
    }

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", items=" + items +
                ", total=" + total +
                ", empleadoId=" + empleadoId +
                ", metodoPago='" + metodoPago + '\'' +
                '}';
    }
}
