package resol.pereyra.controlador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import resol.pereyra.dto.VentaDto;
import resol.pereyra.modelo.ReglaNegocioException;
import resol.pereyra.modelo.Venta;
import resol.pereyra.modelo.VentaDao;
import resol.pereyra.modelo.VentaDaoImpl;
import resol.pereyra.modelo.VentaInvalidaException;
import resol.pereyra.modelo.Videojuego;
import resol.pereyra.modelo.VideojuegoDao;
import resol.pereyra.modelo.VideojuegoDaoImpl;
import resol.pereyra.modelo.VideojuegoNoEncontradoException;
import resol.pereyra.vista.VentaVista;

public class VentaControlador {

    private final VentaDao ventaDao;
    private final VideojuegoDao videojuegoDao;
    private final VentaVista vista;

    public VentaControlador() {

        ventaDao =
                new VentaDaoImpl();

        videojuegoDao =
                new VideojuegoDaoImpl();

        vista =
                new VentaVista();
    }

    public void iniciar() {

        int opcion;

        do {

            opcion =
                    vista.mostrarMenuVentas();

            switch (opcion) {

                case 1:
                    listarVentas();
                    break;

                case 2:
                    buscarVentaPorId();
                    break;

                case 3:
                    registrarVenta();
                    break;

                case 4:
                    buscarVentasPorVideojuego();
                    break;

                case 5:
                    reporteMensual();
                    break;

                case 0:
                    vista.mostrarMensaje(
                            "Volviendo al menu principal..."
                    );
                    break;

                default:
                    vista.mostrarMensaje(
                            "Opcion invalida."
                    );
            }

        } while (opcion != 0);
    }

    private void listarVentas() {

        try {

            List<Venta> ventas =
                    ventaDao.listarVentas();

            if (ventas.isEmpty()) {

                vista.mostrarMensaje(
                        "No hay ventas registradas."
                );

                return;
            }

            for (Venta venta : ventas) {

                VentaDto dto =
                        convertirADto(venta);

                vista.mostrarVenta(dto);
            }

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error al listar ventas: "
                    + e.getMessage()
            );

        } catch (
                VideojuegoNoEncontradoException e
        ) {

            vista.mostrarMensaje(
                    "Error: "
                    + e.getMessage()
            );
        }
    }

    private void buscarVentaPorId() {

        int id =
                vista.pedirVentaId();

        try {

            Venta venta =
                    ventaDao.obtenerPorId(id);

            if (venta == null) {

                vista.mostrarMensaje(
                        "No se encontro una venta con ID "
                        + id
                );

                return;
            }

            VentaDto dto =
                    convertirADto(venta);

            vista.mostrarVenta(dto);

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );

        } catch (
                VideojuegoNoEncontradoException e
        ) {

            vista.mostrarMensaje(
                    "Error: "
                    + e.getMessage()
            );
        }
    }

    private void registrarVenta() {

        try {

            int videojuegoId =
                    vista.pedirVideojuegoId();

            int cantidad =
                    vista.pedirCantidad();

            LocalDate fecha =
                    vista.pedirFecha();

            Venta venta =
                    new Venta(
                            0,
                            fecha,
                            cantidad,
                            videojuegoId
                    );

            ventaDao.registrarVenta(
                    venta
            );

            VentaDto dto =
                    convertirADto(venta);

            vista.mostrarMensaje(
                    "Venta registrada correctamente."
            );

            vista.mostrarVenta(dto);

        } catch (
                VentaInvalidaException
                | VideojuegoNoEncontradoException
                | ReglaNegocioException e
        ) {

            vista.mostrarMensaje(
                    "Error: "
                    + e.getMessage()
            );

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );
        }
    }

    private void buscarVentasPorVideojuego() {

        int videojuegoId =
                vista.pedirVideojuegoId();

        try {

            List<Venta> ventas =
                    ventaDao.listarPorVideojuego(
                            videojuegoId
                    );

            if (ventas.isEmpty()) {

                vista.mostrarMensaje(
                        "No hay ventas para el videojuego con ID "
                        + videojuegoId
                );

                return;
            }

            for (Venta venta : ventas) {

                VentaDto dto =
                        convertirADto(venta);

                vista.mostrarVenta(dto);
            }

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );

        } catch (
                VideojuegoNoEncontradoException e
        ) {

            vista.mostrarMensaje(
                    "Error: "
                    + e.getMessage()
            );
        }
    }

    private void reporteMensual() {

        try {

            List<Venta> ventas =
                    ventaDao.listarVentasDelMes();

            if (ventas.isEmpty()) {

                vista.mostrarMensaje(
                        "No hay ventas en el mes actual."
                );

                return;
            }

            vista.mostrarMensaje(
                    "=== REPORTE DEL MES ACTUAL ==="
            );

            double totalMes = 0;
            int unidadesVendidas = 0;

            for (Venta venta : ventas) {

                VentaDto dto =
                        convertirADto(venta);

                vista.mostrarVenta(dto);

                unidadesVendidas +=
                        dto.getCantidad();

                totalMes +=
                        dto.getTotal();
            }

            vista.mostrarMensaje(
                    "Cantidad de ventas: "
                    + ventas.size()
            );

            vista.mostrarMensaje(
                    "Unidades vendidas: "
                    + unidadesVendidas
            );

            vista.mostrarMensaje(
                    "Total vendido: $"
                    + String.format(
                            "%.2f",
                            totalMes
                    )
            );

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error al generar reporte mensual: "
                    + e.getMessage()
            );

        } catch (
                VideojuegoNoEncontradoException e
        ) {

            vista.mostrarMensaje(
                    "Error: "
                    + e.getMessage()
            );
        }
    }

    private VentaDto convertirADto(
            Venta venta
    ) throws SQLException,
            VideojuegoNoEncontradoException {

        Videojuego videojuego =
                videojuegoDao.obtenerPorId(
                        venta.getVideojuegoId()
                );

        return new VentaDto(
                venta.getId(),
                venta.getFecha(),
                videojuego.getNombre(),
                venta.getCantidad(),
                venta.getDescuento(),
                venta.getTotal()
        );
    }
}