package modelo;

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

    public Venta(int id, Date fecha, List<ItemVenta> items, double total, int empleadoId, String metodoPago) {
        this.id = id;
        this.fecha = fecha;
        this.items = items;
        this.total = total;
        this.empleadoId = empleadoId;
        this.metodoPago = metodoPago;
    }

    // Métodos para manipular items
    public void agregarItem(ItemVenta item) {
        this.items.add(item);
        calcularTotal();
    }

    public void quitarItem(ItemVenta item) {
        this.items.remove(item);
        calcularTotal();
    }

    private void calcularTotal() {
        this.total = items.stream()
                .mapToDouble(ItemVenta::getSubtotal)
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
        calcularTotal();
    }

    public double getTotal() {
        return total;
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