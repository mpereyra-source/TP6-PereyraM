package resol.pereyra.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VideojuegoDAO {

    public void insertarVideojuego(Videojuego videojuego)
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
    // VALIDACIONES DEL TP6
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

    String sql = "INSERT INTO videojuegos "
            + "(nombre, genero, precio, unidadesDisponibles, nivelReposicion, suspendido) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql)) {

        statement.setString(1, videojuego.getNombre());
        statement.setString(2, videojuego.getGenero());
        statement.setDouble(3, videojuego.getPrecio());
        statement.setInt(4, videojuego.getUnidadesDisponibles());
        statement.setInt(5, videojuego.getNivelReposicion());
        statement.setInt(6, videojuego.getSuspendido());

        statement.executeUpdate();

        System.out.println("Videojuego insertado correctamente.");

    } catch (SQLException e) {
        System.out.println(
                "Error al insertar videojuego: " + e.getMessage()
        );
    }
}

    public void listarVideojuegos() {

    String sql = "SELECT * FROM videojuegos";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql);
         java.sql.ResultSet resultado = statement.executeQuery()) {

        while (resultado.next()) {

            int id = resultado.getInt("id");
            String nombre = resultado.getString("nombre");
            String genero = resultado.getString("genero");
            double precio = resultado.getDouble("precio");

            int unidadesDisponibles =
                    resultado.getInt("unidadesDisponibles");

            int nivelReposicion =
                    resultado.getInt("nivelReposicion");

            int suspendido =
                    resultado.getInt("suspendido");

            System.out.println(
                    "ID: " + id
                    + " | Nombre: " + nombre
                    + " | Genero: " + genero
                    + " | Precio: $" + precio
                    + " | Stock: " + unidadesDisponibles
                    + " | Nivel reposicion: " + nivelReposicion
                    + " | Suspendido: " + suspendido
            );
        }

    } catch (SQLException e) {
        System.out.println(
                "Error al listar videojuegos: " + e.getMessage()
        );
    }
}
public void listarVideojuegosDisponibles() {

    String sql = "SELECT * FROM videojuegos WHERE suspendido = 1";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql);
         java.sql.ResultSet resultado = statement.executeQuery()) {

        System.out.println("=== VIDEOJUEGOS DISPONIBLES ===");

        while (resultado.next()) {

            int id = resultado.getInt("id");
            String nombre = resultado.getString("nombre");
            String genero = resultado.getString("genero");
            double precio = resultado.getDouble("precio");
            int unidadesDisponibles =
                    resultado.getInt("unidadesDisponibles");

            System.out.println(
                    "ID: " + id
                    + " | Nombre: " + nombre
                    + " | Genero: " + genero
                    + " | Precio: $" + precio
                    + " | Stock: " + unidadesDisponibles
            );
        }

    } catch (SQLException e) {
        System.out.println(
                "Error al listar videojuegos disponibles: "
                + e.getMessage()
        );
    }
}
public void listarVideojuegosConReposicion() {

    String sql = "SELECT * FROM videojuegos "
            + "WHERE unidadesDisponibles < nivelReposicion";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql);
         java.sql.ResultSet resultado = statement.executeQuery()) {

        System.out.println("=== VIDEOJUEGOS QUE NECESITAN REPOSICION ===");

        boolean encontrado = false;

        while (resultado.next()) {

            encontrado = true;

            int id = resultado.getInt("id");
            String nombre = resultado.getString("nombre");
            int unidadesDisponibles =
                    resultado.getInt("unidadesDisponibles");
            int nivelReposicion =
                    resultado.getInt("nivelReposicion");

            System.out.println(
                    "ID: " + id
                    + " | Nombre: " + nombre
                    + " | Stock: " + unidadesDisponibles
                    + " | Nivel reposicion: " + nivelReposicion
            );
        }

        if (!encontrado) {
            System.out.println(
                    "No hay videojuegos que necesiten reposicion."
            );
        }

    } catch (SQLException e) {
        System.out.println(
                "Error al buscar videojuegos para reposicion: "
                + e.getMessage()
        );
    }
}
public void buscarPorId(int id) throws VideojuegoNoEncontradoException {

    String sql = "SELECT * FROM videojuegos WHERE id = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql)) {

        statement.setInt(1, id);

        java.sql.ResultSet resultado = statement.executeQuery();

        if (resultado.next()) {

            System.out.println("Videojuego encontrado:");
            System.out.println(
                    "ID: " + resultado.getInt("id")
                    + " | Nombre: " + resultado.getString("nombre")
                    + " | Genero: " + resultado.getString("genero")
                    + " | Precio: $" + resultado.getDouble("precio")
            );

        } else {
            throw new VideojuegoNoEncontradoException(
                    "No se encontro un videojuego con ID " + id
            );
        }

    } catch (SQLException e) {
        System.out.println("Error al buscar videojuego: " + e.getMessage());
    }
}
public void actualizarVideojuego(
        int id,
        String nombre,
        String genero,
        double precio,
        int unidadesDisponibles,
        int nivelReposicion,
        int suspendido
)
        throws VideojuegoNoEncontradoException,
               ReglaNegocioException {

    if (nombre == null || nombre.trim().isEmpty()) {
        throw new ReglaNegocioException(
                "El nombre del videojuego no puede estar vacio."
        );
    }

    if (genero == null || genero.trim().isEmpty()) {
        throw new ReglaNegocioException(
                "El genero del videojuego no puede estar vacio."
        );
    }

    if (precio <= 0) {
        throw new ReglaNegocioException(
                "El precio debe ser mayor a 0."
        );
    }

    if (unidadesDisponibles < 0) {
        throw new ReglaNegocioException(
                "Las unidades disponibles no pueden ser negativas."
        );
    }

    String sql =
            "UPDATE videojuegos "
            + "SET nombre = ?, genero = ?, precio = ?, "
            + "unidadesDisponibles = ?, nivelReposicion = ?, suspendido = ? "
            + "WHERE id = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql)) {

        statement.setString(1, nombre);
        statement.setString(2, genero);
        statement.setDouble(3, precio);
        statement.setInt(4, unidadesDisponibles);
        statement.setInt(5, nivelReposicion);
        statement.setInt(6, suspendido);
        statement.setInt(7, id);

        int filas = statement.executeUpdate();

        if (filas == 0) {
            throw new VideojuegoNoEncontradoException(
                    "No se encontro un videojuego con ID " + id
            );
        }

        System.out.println(
                "Videojuego actualizado correctamente."
        );

    } catch (SQLException e) {
        System.out.println(
                "Error al actualizar videojuego: "
                + e.getMessage()
        );
    }
}
public void eliminarVideojuego(int id) throws VideojuegoNoEncontradoException {

    String sql = "DELETE FROM videojuegos WHERE id = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql)) {

        statement.setInt(1, id);

        int filasEliminadas = statement.executeUpdate();

        if (filasEliminadas > 0) {
            System.out.println("Videojuego eliminado correctamente.");
        } else {
            throw new VideojuegoNoEncontradoException(
                    "No se encontro un videojuego con ID " + id
            );
        }

    } catch (SQLException e) {
        System.out.println("Error al eliminar videojuego: " + e.getMessage());
    }
}
}