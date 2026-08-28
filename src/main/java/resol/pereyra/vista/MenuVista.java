package resol.pereyra.vista;

import java.util.Scanner;

public class MenuVista {

    private Scanner scanner;

    public MenuVista() {
        scanner = new Scanner(System.in);
    }

    public int mostrarMenuPrincipal() {

        System.out.println();
        System.out.println("=== TIENDA DE VIDEOJUEGOS ===");
        System.out.println("1. Gestion de Videojuegos");
        System.out.println("2. Gestion de Ventas");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");

        return scanner.nextInt();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}