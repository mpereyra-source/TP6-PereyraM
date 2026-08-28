package resol.pereyra.controlador;

import resol.pereyra.modelo.ReglaNegocioException;
import resol.pereyra.modelo.Venta;
import resol.pereyra.modelo.VentaDAO;
import resol.pereyra.modelo.VentaInvalidaException;
import resol.pereyra.modelo.VideojuegoNoEncontradoException;
import resol.pereyra.vista.VentaVista;

import java.time.LocalDate;

public class VentaControlador {

    private VentaDAO ventaDAO;
    private VentaVista vista;

    public VentaControlador() {
        ventaDAO = new VentaDAO();
        vista = new VentaVista();
    }

    public void iniciar() {

        int opcion;

        do {

            opcion = vista.mostrarMenuVentas();

            switch (opcion) {

                case 1:
                    ventaDAO.listarVentas();
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
                    System.out.println("Volviendo al menu principal...");
                    break;

                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }
    private void buscarVentaPorId() {

    int id = vista.pedirVentaId();

    ventaDAO.buscarVentaPorId(id);
}

    private void registrarVenta() {

        try {

            int videojuegoId = vista.pedirVideojuegoId();
            int cantidad = vista.pedirCantidad();
            LocalDate fecha = vista.pedirFecha();

            Venta venta = new Venta(
                    0,
                    fecha,
                    cantidad,
                    videojuegoId
            );

            ventaDAO.registrarVenta(venta);

        } catch (VentaInvalidaException
                | VideojuegoNoEncontradoException
                | ReglaNegocioException e) {

            vista.mostrarMensaje(
                    "Error: " + e.getMessage()
            );
        }
    }

    private void buscarVentasPorVideojuego() {

        int videojuegoId = vista.pedirVideojuegoId();

        ventaDAO.buscarVentasPorVideojuego(
                videojuegoId
        );
    }

    private void reporteMensual() {

        int anio = vista.pedirAnio();
        int mes = vista.pedirMes();

        ventaDAO.reporteMensual(
                anio,
                mes
        );
    }
}