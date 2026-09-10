package resol.pereyra.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VideojuegoDaoImpl implements VideojuegoDao {

    @Override
    public List<Videojuego> listarVideojuegos() throws SQLException {

        List<Videojuego> videojuegos = new ArrayList<>();

        String sql = "SELECT * FROM videojuegos";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                videojuegos.add(crearVideojuegoDesdeResultado(resultado));
            }
        }

        return videojuegos;
    }

    @Override
    public List<Videojuego> listarDisponibles() throws SQLException {

        List<Videojuego> videojuegos = new ArrayList<>();

        String sql = "SELECT * FROM videojuegos WHERE suspendido = 1";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                videojuegos.add(crearVideojuegoDesdeResultado(resultado));
            }
        }

        return videojuegos;
    }

    @Override
    public List<Videojuego> listarQueNecesitanReposicion()
            throws SQLException {

        List<Videojuego> videojuegos = new ArrayList<>();

        String sql = "SELECT * FROM videojuegos "
                + "WHERE unidadesDisponibles < nivelReposicion";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultado = statement.executeQuery()) {

            while (resultado.next()) {
                videojuegos.add(crearVideojuegoDesdeResultado(resultado));
            }
        }

        return videojuegos;
    }

    @Override
    public Videojuego obtenerPorId(long id)
            throws SQLException, VideojuegoNoEncontradoException {

        String sql = "SELECT * FROM videojuegos WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {
                    return crearVideojuegoDesdeResultado(resultado);
                }
            }
        }

        throw new VideojuegoNoEncontradoException(
                "No se encontro un videojuego con ID " + id
        );
    }

    @Override
    public Videojuego agregarVideojuego(Videojuego videojuego)
            throws SQLException, ReglaNegocioException {

        validarVideojuego(videojuego);

        String sql = "INSERT INTO videojuegos "
                + "(nombre, genero, precio, unidadesDisponibles, "
                + "nivelReposicion, suspendido) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(
                     sql,
                     Statement.RETURN_GENERATED_KEYS
             )) {

            statement.setString(1, videojuego.getNombre());
            statement.setString(2, videojuego.getGenero());
            statement.setDouble(3, videojuego.getPrecio());
            statement.setInt(4, videojuego.getUnidadesDisponibles());
            statement.setInt(5, videojuego.getNivelReposicion());
            statement.setInt(6, videojuego.getSuspendido());

            statement.executeUpdate();

            try (ResultSet claves = statement.getGeneratedKeys()) {

                if (claves.next()) {
                    videojuego.setId(claves.getInt(1));
                }
            }
        }

        return videojuego;
    }

    @Override
    public boolean actualizarVideojuego(Videojuego videojuego)
            throws SQLException {

        String sql = "UPDATE videojuegos "
                + "SET nombre = ?, genero = ?, precio = ?, "
                + "unidadesDisponibles = ?, nivelReposicion = ?, suspendido = ? "
                + "WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, videojuego.getNombre());
            statement.setString(2, videojuego.getGenero());
            statement.setDouble(3, videojuego.getPrecio());
            statement.setInt(4, videojuego.getUnidadesDisponibles());
            statement.setInt(5, videojuego.getNivelReposicion());
            statement.setInt(6, videojuego.getSuspendido());
            statement.setInt(7, videojuego.getId());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminarVideojuego(long id) throws SQLException {

        String sql = "DELETE FROM videojuegos WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setLong(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    private Videojuego crearVideojuegoDesdeResultado(ResultSet resultado)
            throws SQLException {

        return new Videojuego(
                resultado.getInt("id"),
                resultado.getString("nombre"),
                resultado.getString("genero"),
                resultado.getDouble("precio"),
                resultado.getInt("unidadesDisponibles"),
                resultado.getInt("nivelReposicion"),
                resultado.getInt("suspendido")
        );
    }

    private void validarVideojuego(Videojuego videojuego)
            throws ReglaNegocioException {

        if (videojuego.getNombre() == null
                || videojuego.getNombre().trim().isEmpty()) {

            throw new ReglaNegocioException(
                    "El nombre del videojuego no puede estar vacio."
            );
        }

        if (videojuego.getGenero() == null
                || videojuego.getGenero().trim().isEmpty()) {

            throw new ReglaNegocioException(
                    "El genero del videojuego no puede estar vacio."
            );
        }

        if (videojuego.getPrecio() <= 0) {
            throw new ReglaNegocioException(
                    "El precio del videojuego debe ser mayor a 0."
            );
        }

        if (videojuego.getUnidadesDisponibles() < 0) {
            throw new ReglaNegocioException(
                    "Las unidades disponibles no pueden ser negativas."
            );
        }
    }
}