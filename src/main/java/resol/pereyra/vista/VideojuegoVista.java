package resol.pereyra.vista;

import java.util.Scanner;

public class VideojuegoVista {

    private Scanner scanner;

    public VideojuegoVista() {
        scanner = new Scanner(System.in);
    }

    public int mostrarMenuVideojuegos() {

        System.out.println();
        System.out.println("=== GESTION DE VIDEOJUEGOS ===");
        System.out.println("1. Listar videojuegos");
        System.out.println("2. Buscar videojuego por ID");
        System.out.println("3. Agregar videojuego");
        System.out.println("4. Actualizar videojuego");
        System.out.println("5. Eliminar videojuego");
        System.out.println("6. Mostrar videojuegos que necesitan reposicion");
        System.out.println("7. Mostrar videojuegos disponibles");
        System.out.println("0. Volver");
        System.out.print("Seleccione una opcion: ");

        return scanner.nextInt();
    }

    public int pedirId() {
        System.out.print("Ingrese el ID: ");
        return scanner.nextInt();
    }

    public String pedirNombre() {
        scanner.nextLine();
        System.out.print("Ingrese el nombre: ");
        return scanner.nextLine();
    }

    public String pedirGenero() {
        System.out.print("Ingrese el genero: ");
        return scanner.nextLine();
    }

    public double pedirPrecio() {
        System.out.print("Ingrese el precio: ");
        return scanner.nextDouble();
    }

    public int pedirUnidadesDisponibles() {
        System.out.print("Ingrese las unidades disponibles: ");
        return scanner.nextInt();
    }

    public int pedirNivelReposicion() {
        System.out.print("Ingrese el nivel de reposicion: ");
        return scanner.nextInt();
    }

    public int pedirSuspendido() {
        System.out.print(
                "Ingrese 1 si esta disponible o 0 si esta suspendido: "
        );
        return scanner.nextInt();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}