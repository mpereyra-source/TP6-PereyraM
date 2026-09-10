package resol.pereyra.modelo;

import java.sql.SQLException;
import java.util.List;

public interface VentaDao {

    void registrarVenta(Venta venta)
            throws SQLException,
            VentaInvalidaException,
            VideojuegoNoEncontradoException,
            ReglaNegocioException;

    List<Venta> listarVentas()
            throws SQLException;

    Venta obtenerPorId(long id)
            throws SQLException;

    List<Venta> listarPorVideojuego(long idVideojuego)
            throws SQLException;

    List<Venta> listarVentasDelMes()
            throws SQLException;
}