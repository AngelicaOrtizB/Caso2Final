import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        SimuladorMemoria simuladorMemoria = new SimuladorMemoria();  // Crear instancia del simulador de memoria

        while (!salir) {
            // Mostrar el menú
            System.out.println("---- Menú ----");
            System.out.println("1. Obtener referencia");
            System.out.println("2. Simular paginación");
            System.out.println("3. Salir");
            System.out.print("Elige una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); 

            switch (opcion) {
                case 1:
                    System.out.print("Introduce el tamaño de página: ");
                    int tamanoPagina = scanner.nextInt();
                    scanner.nextLine();  
                    System.out.print("Introduce el nombre del archivo: ");
                    String nombreArchivo = scanner.nextLine();
                    System.out.print("Introduce el nombre del archivo a generar: ");
                    String nombreArchivo2 = scanner.nextLine();
                    GenerarReferencia referencia = new GenerarReferencia(tamanoPagina, nombreArchivo,nombreArchivo2);
                    referencia.realizarReferencia();
                    break;

                case 2:
                    System.out.print("Introduce el número de marcos de página: ");
                    int numMarcos = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Introduce el nombre del archivo de referencias: ");
                    String archivoReferencias = scanner.nextLine();
                    simuladorMemoria.iniciarSimulacion(numMarcos, archivoReferencias);
                    break;

                case 3:
                    System.out.println("Saliendo del programa...");
                    salir = true;
                    break;

                default:
                    System.out.println("Opción no válida. Por favor, elige una opción válida.");
            }

            System.out.println();
        }

        scanner.close();
    }
}

