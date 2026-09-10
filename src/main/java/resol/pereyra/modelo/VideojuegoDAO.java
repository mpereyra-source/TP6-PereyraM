package resol.pereyra.modelo;

import java.sql.SQLException;
import java.util.List;

public interface VideojuegoDao {

    List<Videojuego> listarVideojuegos()
            throws SQLException;

    List<Videojuego> listarDisponibles()
            throws SQLException;

    List<Videojuego> listarQueNecesitanReposicion()
            throws SQLException;

    Videojuego obtenerPorId(long id)
            throws SQLException, VideojuegoNoEncontradoException;

    Videojuego agregarVideojuego(Videojuego videojuego)
            throws SQLException, ReglaNegocioException;

    boolean actualizarVideojuego(Videojuego videojuego)
            throws SQLException;

    boolean eliminarVideojuego(long id)
            throws SQLException;
}