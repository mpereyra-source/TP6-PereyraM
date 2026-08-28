package resol.pereyra.vista;

import java.time.LocalDate;
import java.util.Scanner;

public class VentaVista {

    private Scanner scanner;

    public VentaVista() {
        scanner = new Scanner(System.in);
    }

    public int mostrarMenuVentas() {

        System.out.println();
        System.out.println("=== GESTION DE VENTAS ===");
        System.out.println("1. Listar ventas");
        System.out.println("2. Buscar venta por ID");
        System.out.println("3. Registrar venta");
        System.out.println("4. Buscar ventas por videojuego");
        System.out.println("5. Reporte mensual");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opcion: ");

        return scanner.nextInt();
    }

    public int pedirVideojuegoId() {
        System.out.print("Ingrese el ID del videojuego: ");
        return scanner.nextInt();
    }

    public int pedirCantidad() {
        System.out.print("Ingrese la cantidad: ");
        return scanner.nextInt();
    }

    public LocalDate pedirFecha() {

        System.out.print("Ingrese el anio: ");
        int anio = scanner.nextInt();

        System.out.print("Ingrese el mes: ");
        int mes = scanner.nextInt();

        System.out.print("Ingrese el dia: ");
        int dia = scanner.nextInt();

        return LocalDate.of(anio, mes, dia);
    }

    public int pedirAnio() {
        System.out.print("Ingrese el anio: ");
        return scanner.nextInt();
    }

    public int pedirMes() {
        System.out.print("Ingrese el mes: ");
        return scanner.nextInt();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
    public int pedirVentaId() {
    System.out.print("Ingrese el ID de la venta: ");
    return scanner.nextInt();
}
}