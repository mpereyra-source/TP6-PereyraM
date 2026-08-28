package resol.pereyra.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class VentaDAO {

    public void registrarVenta(Venta venta)
        throws VentaInvalidaException,
               VideojuegoNoEncontradoException,
               ReglaNegocioException {

    // VALIDAR CANTIDAD
    if (venta.getCantidad() <= 0) {
        throw new VentaInvalidaException(
                "La cantidad debe ser mayor a 0."
        );
    }

    // VALIDAR FECHA
    if (venta.getFecha().isAfter(java.time.LocalDate.now())) {
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

        java.sql.ResultSet resultado = buscar.executeQuery();

        // VERIFICAR QUE EL VIDEOJUEGO EXISTA
        if (!resultado.next()) {
            throw new VideojuegoNoEncontradoException(
                    "No se encontro un videojuego con ID "
                    + venta.getVideojuegoId()
            );
        }

        double precio = resultado.getDouble("precio");
        int stock = resultado.getInt("unidadesDisponibles");
        int suspendido = resultado.getInt("suspendido");

        // VERIFICAR QUE NO ESTE SUSPENDIDO
        if (suspendido == 0) {
            throw new ReglaNegocioException(
                    "El videojuego esta suspendido y no puede venderse."
            );
        }

        // VERIFICAR STOCK
        if (venta.getCantidad() > stock) {
            throw new ReglaNegocioException(
                    "Stock insuficiente. Stock disponible: " + stock
            );
        }

        // CALCULAR DESCUENTO Y TOTAL
double porcentaje = venta.calcularPorcentajeDescuento();
double total = venta.calcularTotal(precio);

// REGISTRAR LA VENTA
String sqlVenta =
        "INSERT INTO ventas "
        + "(fecha, cantidad, videojuego_id, descuento, total) "
        + "VALUES (?, ?, ?, ?, ?)";

try (PreparedStatement insertar =
             conexion.prepareStatement(sqlVenta)) {

    insertar.setDate(
            1,
            java.sql.Date.valueOf(venta.getFecha())
    );

    insertar.setInt(2, venta.getCantidad());
    insertar.setInt(3, venta.getVideojuegoId());

    // Guardamos el porcentaje: 0, 5, 10 o 15
    insertar.setDouble(4, porcentaje * 100);

    insertar.setDouble(5, total);

    insertar.executeUpdate();
}

        // DESCONTAR STOCK
        String sqlStock =
                "UPDATE videojuegos "
                + "SET unidadesDisponibles = unidadesDisponibles - ? "
                + "WHERE id = ?";

        try (PreparedStatement actualizarStock =
                     conexion.prepareStatement(sqlStock)) {

            actualizarStock.setInt(1, venta.getCantidad());
            actualizarStock.setInt(2, venta.getVideojuegoId());

            actualizarStock.executeUpdate();
        }

        

        System.out.println("Venta registrada correctamente.");
        System.out.println(
                "Descuento: "
                + (porcentaje * 100)
                + "%"
        );
        System.out.printf(
        "Total: $%.2f%n",
        total
);

    } catch (SQLException e) {
        System.out.println(
                "Error al registrar venta: " + e.getMessage()
        );
    }
}
    // 2. LISTAR VENTAS
public void listarVentas() {

    String sql =
            "SELECT v.id, v.fecha, v.cantidad, "
            + "v.descuento, v.total, j.nombre "
            + "FROM ventas v "
            + "JOIN videojuegos j ON v.videojuego_id = j.id";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql);
         java.sql.ResultSet resultado = statement.executeQuery()) {

        System.out.println("=== LISTADO DE VENTAS ===");

        while (resultado.next()) {

            int id = resultado.getInt("id");
            java.sql.Date fecha = resultado.getDate("fecha");
            String videojuego = resultado.getString("nombre");
            int cantidad = resultado.getInt("cantidad");
            double descuento = resultado.getDouble("descuento");
            double total = resultado.getDouble("total");

            System.out.println(
                    "ID: " + id
                    + " | Fecha: " + fecha
                    + " | Videojuego: " + videojuego
                    + " | Cantidad: " + cantidad
                    + " | Descuento: " + descuento + "%"
                    + " | Total: $" + String.format("%.2f", total)
            );
        }

    } catch (SQLException e) {
        System.out.println(
                "Error al listar ventas: " + e.getMessage()
        );
    }
}
public void buscarVentasPorVideojuego(int videojuegoId) {

    String sql = "SELECT * FROM ventas WHERE videojuego_id = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql)) {

        statement.setInt(1, videojuegoId);

        java.sql.ResultSet resultado = statement.executeQuery();

        boolean encontrada = false;

        while (resultado.next()) {

            encontrada = true;

            int id = resultado.getInt("id");
            java.sql.Date fecha = resultado.getDate("fecha");
            int cantidad = resultado.getInt("cantidad");

            System.out.println(
                    "ID Venta: " + id
                    + " | Fecha: " + fecha
                    + " | Cantidad: " + cantidad
                    + " | Videojuego ID: " + videojuegoId
            );
        }

        if (!encontrada) {
            System.out.println(
                    "No hay ventas para el videojuego con ID " + videojuegoId
            );
        }

    } catch (SQLException e) {
        System.out.println(
                "Error al buscar ventas: " + e.getMessage()
        );
    }
}
public void reporteMensual(int anio, int mes) {

    String sql =
            "SELECT COUNT(*) AS cantidad_ventas, "
            + "SUM(cantidad) AS unidades_vendidas, "
            + "SUM(total) AS total_mes "
            + "FROM ventas "
            + "WHERE YEAR(fecha) = ? AND MONTH(fecha) = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql)) {

        statement.setInt(1, anio);
        statement.setInt(2, mes);

        java.sql.ResultSet resultado = statement.executeQuery();

        if (resultado.next()) {

            int cantidadVentas =
                    resultado.getInt("cantidad_ventas");

            int unidadesVendidas =
                    resultado.getInt("unidades_vendidas");

            double totalMes =
                    resultado.getDouble("total_mes");

            System.out.println("=== REPORTE MENSUAL ===");
            System.out.println("Anio: " + anio);
            System.out.println("Mes: " + mes);
            System.out.println(
                    "Cantidad de ventas: " + cantidadVentas
            );
            System.out.println(
                    "Unidades vendidas: " + unidadesVendidas
            );
            System.out.printf(
                    "Total vendido: $%.2f%n",
                    totalMes
            );
        }

    } catch (SQLException e) {
        System.out.println(
                "Error al generar reporte mensual: "
                + e.getMessage()
        );
    }
}
public void buscarVentaPorId(int id) {

    String sql =
            "SELECT v.id, v.fecha, v.cantidad, "
            + "v.descuento, v.total, j.nombre "
            + "FROM ventas v "
            + "JOIN videojuegos j ON v.videojuego_id = j.id "
            + "WHERE v.id = ?";

    try (Connection conexion = ConexionBD.obtenerConexion();
         PreparedStatement statement = conexion.prepareStatement(sql)) {

        statement.setInt(1, id);

        java.sql.ResultSet resultado = statement.executeQuery();

        if (resultado.next()) {

            System.out.println("=== VENTA ENCONTRADA ===");

            System.out.println(
                    "ID: " + resultado.getInt("id")
                    + " | Fecha: " + resultado.getDate("fecha")
                    + " | Videojuego: " + resultado.getString("nombre")
                    + " | Cantidad: " + resultado.getInt("cantidad")
                    + " | Descuento: " + resultado.getDouble("descuento") + "%"
                    + " | Total: $"
                    + String.format("%.2f", resultado.getDouble("total"))
            );

        } else {

            System.out.println(
                    "No se encontro una venta con ID " + id
            );
        }

    } catch (SQLException e) {

        System.out.println(
                "Error al buscar venta: " + e.getMessage()
        );
    }
}
}