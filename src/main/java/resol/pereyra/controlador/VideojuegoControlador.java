package resol.pereyra.controlador;

import resol.pereyra.modelo.ReglaNegocioException;
import resol.pereyra.modelo.Videojuego;
import resol.pereyra.modelo.VideojuegoDAO;
import resol.pereyra.modelo.VideojuegoNoEncontradoException;
import resol.pereyra.vista.VideojuegoVista;

public class VideojuegoControlador {

    private VideojuegoDAO videojuegoDAO;
    private VideojuegoVista vista;

    public VideojuegoControlador() {
        videojuegoDAO = new VideojuegoDAO();
        vista = new VideojuegoVista();
    }

    public void iniciar() {

        int opcion;

        do {

            opcion = vista.mostrarMenuVideojuegos();

            switch (opcion) {

                case 1:
                    videojuegoDAO.listarVideojuegos();
                    break;

                case 2:
                    buscarPorId();
                    break;

                case 3:
                    agregarVideojuego();
                    break;

                case 4:
                    actualizarVideojuego();
                    break;

                case 5:
                    eliminarVideojuego();
                    break;

                case 6:
                    videojuegoDAO.listarVideojuegosConReposicion();
                    break;

                case 7:
                    videojuegoDAO.listarVideojuegosDisponibles();
                    break;

                case 0:
                    vista.mostrarMensaje("Volviendo al menu principal...");
                    break;

                default:
                    vista.mostrarMensaje("Opcion invalida.");
            }

        } while (opcion != 0);
    }

    private void buscarPorId() {

        int id = vista.pedirId();

        try {
            videojuegoDAO.buscarPorId(id);
        } catch (VideojuegoNoEncontradoException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private void agregarVideojuego() {

        String nombre = vista.pedirNombre();
        String genero = vista.pedirGenero();
        double precio = vista.pedirPrecio();
        int unidades = vista.pedirUnidadesDisponibles();
        int nivelReposicion = vista.pedirNivelReposicion();
        int suspendido = vista.pedirSuspendido();

        Videojuego videojuego = new Videojuego(
                0,
                nombre,
                genero,
                precio,
                unidades,
                nivelReposicion,
                suspendido
        );

        try {
            videojuegoDAO.insertarVideojuego(videojuego);
        } catch (ReglaNegocioException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private void actualizarVideojuego() {

    int id = vista.pedirId();
    String nombre = vista.pedirNombre();
    String genero = vista.pedirGenero();
    double precio = vista.pedirPrecio();
    int unidades = vista.pedirUnidadesDisponibles();
    int nivelReposicion = vista.pedirNivelReposicion();
    int suspendido = vista.pedirSuspendido();

    try {

        videojuegoDAO.actualizarVideojuego(
                id,
                nombre,
                genero,
                precio,
                unidades,
                nivelReposicion,
                suspendido
        );

    } catch (VideojuegoNoEncontradoException
            | ReglaNegocioException e) {

        vista.mostrarMensaje(
                "Error: " + e.getMessage()
        );
    }
}

    private void eliminarVideojuego() {

        int id = vista.pedirId();

        try {
            videojuegoDAO.eliminarVideojuego(id);
        } catch (VideojuegoNoEncontradoException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }
}