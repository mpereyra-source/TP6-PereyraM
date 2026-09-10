package resol.pereyra.controlador;

import java.sql.SQLException;
import java.util.List;

import resol.pereyra.dto.VideojuegoDto;
import resol.pereyra.modelo.ReglaNegocioException;
import resol.pereyra.modelo.Videojuego;
import resol.pereyra.modelo.VideojuegoDao;
import resol.pereyra.modelo.VideojuegoDaoImpl;
import resol.pereyra.modelo.VideojuegoNoEncontradoException;
import resol.pereyra.vista.VideojuegoVista;

public class VideojuegoControlador {

    private final VideojuegoDao videojuegoDao;
    private final VideojuegoVista vista;

    public VideojuegoControlador() {
        videojuegoDao = new VideojuegoDaoImpl();
        vista = new VideojuegoVista();
    }

    public void iniciar() {

        int opcion;

        do {

            opcion = vista.mostrarMenuVideojuegos();

            switch (opcion) {

                case 1:
                    listarVideojuegos();
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
                    listarQueNecesitanReposicion();
                    break;

                case 7:
                    listarDisponibles();
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

    private void listarVideojuegos() {

        try {

            List<Videojuego> videojuegos =
                    videojuegoDao.listarVideojuegos();

            if (videojuegos.isEmpty()) {
                vista.mostrarMensaje(
                        "No hay videojuegos registrados."
                );
                return;
            }

            for (Videojuego videojuego : videojuegos) {

                VideojuegoDto dto =
                        convertirADto(videojuego);

                vista.mostrarVideojuego(dto);
            }

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error al listar videojuegos: "
                    + e.getMessage()
            );
        }
    }

    private void buscarPorId() {

        int id = vista.pedirId();

        try {

            Videojuego videojuego =
                    videojuegoDao.obtenerPorId(id);

            VideojuegoDto dto =
                    convertirADto(videojuego);

            vista.mostrarVideojuego(dto);

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );

        } catch (VideojuegoNoEncontradoException e) {

            vista.mostrarMensaje(
                    "Error: " + e.getMessage()
            );
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

            videojuegoDao.agregarVideojuego(videojuego);

            vista.mostrarMensaje(
                    "Videojuego agregado correctamente."
            );

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );

        } catch (ReglaNegocioException e) {

            vista.mostrarMensaje(
                    "Error: " + e.getMessage()
            );
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

        Videojuego videojuego = new Videojuego(
                id,
                nombre,
                genero,
                precio,
                unidades,
                nivelReposicion,
                suspendido
        );

        try {

            boolean actualizado =
                    videojuegoDao.actualizarVideojuego(
                            videojuego
                    );

            if (actualizado) {

                vista.mostrarMensaje(
                        "Videojuego actualizado correctamente."
                );

            } else {

                vista.mostrarMensaje(
                        "No se encontro el videojuego."
                );
            }

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );
        }
    }

    private void eliminarVideojuego() {

        int id = vista.pedirId();

        try {

            boolean eliminado =
                    videojuegoDao.eliminarVideojuego(id);

            if (eliminado) {

                vista.mostrarMensaje(
                        "Videojuego eliminado correctamente."
                );

            } else {

                vista.mostrarMensaje(
                        "No se encontro el videojuego."
                );
            }

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );
        }
    }

    private void listarQueNecesitanReposicion() {

        try {

            List<Videojuego> videojuegos =
                    videojuegoDao
                            .listarQueNecesitanReposicion();

            if (videojuegos.isEmpty()) {

                vista.mostrarMensaje(
                        "No hay videojuegos que necesiten reposicion."
                );

                return;
            }

            for (Videojuego videojuego : videojuegos) {

                VideojuegoDto dto =
                        convertirADto(videojuego);

                vista.mostrarVideojuego(dto);
            }

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );
        }
    }

    private void listarDisponibles() {

        try {

            List<Videojuego> videojuegos =
                    videojuegoDao.listarDisponibles();

            if (videojuegos.isEmpty()) {

                vista.mostrarMensaje(
                        "No hay videojuegos disponibles."
                );

                return;
            }

            for (Videojuego videojuego : videojuegos) {

                VideojuegoDto dto =
                        convertirADto(videojuego);

                vista.mostrarVideojuego(dto);
            }

        } catch (SQLException e) {

            vista.mostrarMensaje(
                    "Error de base de datos: "
                    + e.getMessage()
            );
        }
    }

    private VideojuegoDto convertirADto(
            Videojuego videojuego
    ) {

        return new VideojuegoDto(
                videojuego.getId(),
                videojuego.getNombre(),
                videojuego.getPrecio(),
                videojuego.necesitaReposicion()
        );
    }
}