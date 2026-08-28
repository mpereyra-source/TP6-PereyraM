package resol.pereyra;

import resol.pereyra.controlador.VideojuegoControlador;
import resol.pereyra.controlador.VentaControlador;
import resol.pereyra.modelo.ConexionBD;
import resol.pereyra.vista.MenuVista;

public class App {

    public static void main(String[] args) {

        ConexionBD.crearTablaVideojuegos();
        ConexionBD.crearTablaVentas();

        MenuVista menuVista = new MenuVista();

        VideojuegoControlador videojuegoControlador =
                new VideojuegoControlador();

        VentaControlador ventaControlador =
                new VentaControlador();

        int opcion;

        do {

            opcion = menuVista.mostrarMenuPrincipal();

            switch (opcion) {

                case 1:
                    videojuegoControlador.iniciar();
                    break;

                case 2:
                    ventaControlador.iniciar();
                    break;

                case 0:
                    menuVista.mostrarMensaje(
                            "Programa finalizado."
                    );
                    break;

                default:
                    menuVista.mostrarMensaje(
                            "Opcion invalida."
                    );
            }

        } while (opcion != 0);
    }
}