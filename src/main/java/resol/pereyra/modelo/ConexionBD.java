package resol.pereyra.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

    private static final String URL = "jdbc:h2:./videojuegos";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "";

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    public static void crearTablaVideojuegos() {

        String sql = "CREATE TABLE IF NOT EXISTS videojuegos ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "nombre VARCHAR(100) NOT NULL, "
                + "genero VARCHAR(50) NOT NULL, "
                + "precio DECIMAL(10,2) NOT NULL"
                + ")";

        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {

            statement.execute(sql);

            statement.execute(
                    "ALTER TABLE videojuegos ADD COLUMN IF NOT EXISTS unidadesDisponibles INT DEFAULT 0"
            );

            statement.execute(
                    "ALTER TABLE videojuegos ADD COLUMN IF NOT EXISTS nivelReposicion INT DEFAULT 0"
            );

            statement.execute(
                    "ALTER TABLE videojuegos ADD COLUMN IF NOT EXISTS suspendido INT DEFAULT 1"
            );

            System.out.println("Tabla videojuegos creada correctamente.");

        } catch (SQLException e) {
            System.out.println(
                    "Error al crear la tabla: " + e.getMessage()
            );
        }
    }

    public static void crearTablaVentas() {

        String sql = "CREATE TABLE IF NOT EXISTS ventas ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "fecha DATE NOT NULL, "
                + "cantidad INT NOT NULL, "
                + "videojuego_id INT NOT NULL, "
                + "FOREIGN KEY (videojuego_id) REFERENCES videojuegos(id)"
                + ")";

        try (Connection conexion = obtenerConexion();
             Statement statement = conexion.createStatement()) {

            statement.execute(sql);

            statement.execute(
                    "ALTER TABLE ventas ADD COLUMN IF NOT EXISTS descuento DECIMAL(5,2) DEFAULT 0"
            );

            statement.execute(
                    "ALTER TABLE ventas ADD COLUMN IF NOT EXISTS total DECIMAL(10,2) DEFAULT 0"
            );

            System.out.println("Tabla ventas creada correctamente.");

        } catch (SQLException e) {
            System.out.println(
                    "Error al crear la tabla ventas: " + e.getMessage()
            );
        }
    }
}