import java.io.*;
import java.util.*;

public class SimuladorMemoria {

    public void iniciarSimulacion(int numMarcos, String archivoReferencias) {
        List<Referencia> referencias = leerReferencias(archivoReferencias);

        Object lock = new Object();

        SimuladorPaginacion simulador = new SimuladorPaginacion(numMarcos, referencias, lock);
        ActualizarBitR actualizadorR = new ActualizarBitR(simulador.getTablaPaginas(), lock);

        simulador.start();
        actualizadorR.start();

        try {
            simulador.join();         
            actualizadorR.finalizar();  
            actualizadorR.join();      
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    
    private List<Referencia> leerReferencias(String archivoReferencias) {
        List<Referencia> referencias = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(archivoReferencias))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                if (linea.startsWith("NR")) {
                    String[] partes = linea.split("=");
                    int ref = Integer.parseInt(partes[1]);
                    System.out.println("Cantidad de referencias:"+ ref);
                }
                if (linea.startsWith("NP")) {
                    String[] partes = linea.split("=");
                    int pag = Integer.parseInt(partes[1]);
                    System.out.println("Cantidad de paginas:"+ pag);

                }

                if (linea.startsWith("Imagen") || linea.startsWith("Mensaje")) {
                    String[] partes = linea.split(",");
                    String objeto = partes[0];
                    int paginaVirtual = Integer.parseInt(partes[1]);
                    int desplazamiento = Integer.parseInt(partes[2]);
                    String accion = partes[3];
                    referencias.add(new Referencia(objeto, paginaVirtual, desplazamiento, accion));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return referencias;
    }
}

