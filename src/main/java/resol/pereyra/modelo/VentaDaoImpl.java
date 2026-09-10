package resol.pereyra.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDaoImpl implements VentaDao {

    @Override
    public void registrarVenta(Venta venta)
            throws SQLException,
            VentaInvalidaException,
            VideojuegoNoEncontradoException,
            ReglaNegocioException {

        if (venta.getCantidad() <= 0) {
            throw new VentaInvalidaException(
                    "La cantidad debe ser mayor a 0."
            );
        }

        if (venta.getFecha().isAfter(LocalDate.now())) {
            throw new VentaInvalidaException(
                    "La fecha de la venta no puede ser futura."
            );
        }

        String sqlBuscarVideojuego =
                "SELECT * FROM videojuegos WHERE id = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement buscar =
                     conexion.prepareStatement(sqlBuscarVideojuego)) {

            buscar.setInt(1, venta.getVideojuegoId());

            try (ResultSet resultado = buscar.executeQuery()) {

                if (!resultado.next()) {
                    throw new VideojuegoNoEncontradoException(
                            "No se encontro un videojuego con ID "
                            + venta.getVideojuegoId()
                    );
                }

                double precio =
                        resultado.getDouble("precio");

                int stock =
                        resultado.getInt(
                                "unidadesDisponibles"
                        );

                int suspendido =
                        resultado.getInt("suspendido");

                if (suspendido == 0) {
                    throw new ReglaNegocioException(
                            "El videojuego esta suspendido "
                            + "y no puede venderse."
                    );
                }

                if (venta.getCantidad() > stock) {
                    throw new ReglaNegocioException(
                            "Stock insuficiente. Stock disponible: "
                            + stock
                    );
                }

                double porcentaje =
                        venta.calcularPorcentajeDescuento();

                double total =
                        venta.calcularTotal(precio);

                venta.setDescuento(porcentaje * 100);
                venta.setTotal(total);

                String sqlVenta =
                        "INSERT INTO ventas "
                        + "(fecha, cantidad, videojuego_id, "
                        + "descuento, total) "
                        + "VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement insertar =
                             conexion.prepareStatement(
                                     sqlVenta
                             )) {

                    insertar.setDate(
                            1,
                            java.sql.Date.valueOf(
                                    venta.getFecha()
                            )
                    );

                    insertar.setInt(
                            2,
                            venta.getCantidad()
                    );

                    insertar.setInt(
                            3,
                            venta.getVideojuegoId()
                    );

                    insertar.setDouble(
                            4,
                            venta.getDescuento()
                    );

                    insertar.setDouble(
                            5,
                            venta.getTotal()
                    );

                    insertar.executeUpdate();
                }

                String sqlStock =
                        "UPDATE videojuegos "
                        + "SET unidadesDisponibles = "
                        + "unidadesDisponibles - ? "
                        + "WHERE id = ?";

                try (PreparedStatement actualizarStock =
                             conexion.prepareStatement(
                                     sqlStock
                             )) {

                    actualizarStock.setInt(
                            1,
                            venta.getCantidad()
                    );

                    actualizarStock.setInt(
                            2,
                            venta.getVideojuegoId()
                    );

                    actualizarStock.executeUpdate();
                }
            }
        }
    }

    @Override
    public List<Venta> listarVentas()
            throws SQLException {

        List<Venta> ventas =
                new ArrayList<>();

        String sql =
                "SELECT * FROM ventas";

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     statement.executeQuery()) {

            while (resultado.next()) {
                ventas.add(
                        crearVentaDesdeResultado(
                                resultado
                        )
                );
            }
        }

        return ventas;
    }

    @Override
    public Venta obtenerPorId(long id)
            throws SQLException {

        String sql =
                "SELECT * FROM ventas WHERE id = ?";

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultado =
                         statement.executeQuery()) {

                if (resultado.next()) {
                    return crearVentaDesdeResultado(
                            resultado
                    );
                }
            }
        }

        return null;
    }

    @Override
    public List<Venta> listarPorVideojuego(
            long idVideojuego
    ) throws SQLException {

        List<Venta> ventas =
                new ArrayList<>();

        String sql =
                "SELECT * FROM ventas "
                + "WHERE videojuego_id = ?";

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setLong(
                    1,
                    idVideojuego
            );

            try (ResultSet resultado =
                         statement.executeQuery()) {

                while (resultado.next()) {
                    ventas.add(
                            crearVentaDesdeResultado(
                                    resultado
                            )
                    );
                }
            }
        }

        return ventas;
    }

    @Override
    public List<Venta> listarVentasDelMes()
            throws SQLException {

        List<Venta> ventas =
                new ArrayList<>();

        String sql =
                "SELECT * FROM ventas "
                + "WHERE YEAR(fecha) = ? "
                + "AND MONTH(fecha) = ?";

        LocalDate hoy =
                LocalDate.now();

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    hoy.getYear()
            );

            statement.setInt(
                    2,
                    hoy.getMonthValue()
            );

            try (ResultSet resultado =
                         statement.executeQuery()) {

                while (resultado.next()) {
                    ventas.add(
                            crearVentaDesdeResultado(
                                    resultado
                            )
                    );
                }
            }
        }

        return ventas;
    }

    private Venta crearVentaDesdeResultado(
            ResultSet resultado
    ) throws SQLException {

        Venta venta =
                new Venta();

        venta.setId(
                resultado.getInt("id")
        );

        venta.setFecha(
                resultado.getDate("fecha")
                        .toLocalDate()
        );

        venta.setCantidad(
                resultado.getInt("cantidad")
        );

        venta.setVideojuegoId(
                resultado.getInt(
                        "videojuego_id"
                )
        );

        venta.setDescuento(
                resultado.getDouble(
                        "descuento"
                )
        );

        venta.setTotal(
                resultado.getDouble("total")
        );

        return venta;
    }
}