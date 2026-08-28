package resol.pereyra.modelo;

import java.time.LocalDate;

public class Venta {

    private int id;
    private LocalDate fecha;
    private int cantidad;
    private int videojuegoId;
    private double descuento;
    private double total;

    public Venta() {
    }

    public Venta(
            int id,
            LocalDate fecha,
            int cantidad,
            int videojuegoId
    ) {
        this.id = id;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.videojuegoId = videojuegoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getVideojuegoId() {
        return videojuegoId;
    }

    public void setVideojuegoId(int videojuegoId) {
        this.videojuegoId = videojuegoId;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double calcularPorcentajeDescuento() {

        if (cantidad >= 10) {
            return 0.15;
        } else if (cantidad >= 5) {
            return 0.10;
        } else if (cantidad >= 2) {
            return 0.05;
        } else {
            return 0.0;
        }
    }

    public double calcularTotal(double precioVideojuego) {

        double subtotal = precioVideojuego * cantidad;
        double descuentoCalculado =
                subtotal * calcularPorcentajeDescuento();

        return subtotal - descuentoCalculado;
    }
}