package resol.pereyra.dto;

import java.time.LocalDate;

public class VentaDto {

    private int id;
    private LocalDate fecha;
    private String nombreVideojuego;
    private int cantidad;
    private double porcentajeDescuento;
    private double total;

    public VentaDto() {
    }

    public VentaDto(
            int id,
            LocalDate fecha,
            String nombreVideojuego,
            int cantidad,
            double porcentajeDescuento,
            double total
    ) {
        this.id = id;
        this.fecha = fecha;
        this.nombreVideojuego = nombreVideojuego;
        this.cantidad = cantidad;
        this.porcentajeDescuento = porcentajeDescuento;
        this.total = total;
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

    public String getNombreVideojuego() {
        return nombreVideojuego;
    }

    public void setNombreVideojuego(
            String nombreVideojuego
    ) {
        this.nombreVideojuego = nombreVideojuego;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(
            double porcentajeDescuento
    ) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}