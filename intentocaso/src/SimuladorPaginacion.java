
import java.util.*;

class SimuladorPaginacion extends Thread {
    private int numMarcos;
    private List<Referencia> referencias;  // Lista de referencias leídas
    private List<Pagina> marcos;  // Los marcos de página en memoria (frames)
    private Map<Integer, Pagina> tablaPaginas;  // Tabla de páginas
    private int hits = 0;
    private int fallas = 0;
    private long tiempoHits = 0;
    private long tiempoFallas = 0;
    private boolean terminar = false;
    private Object lock;  // Sincronización

    public SimuladorPaginacion(int numMarcos, List<Referencia> referencias, Object lock) {
        this.numMarcos = numMarcos;
        this.referencias = referencias;
        this.marcos = new ArrayList<>(numMarcos);  // Los marcos físicos
        this.tablaPaginas = new HashMap<>();
        this.lock = lock;
    }

    @Override
    public void run() {
        // Procesar las referencias una por una
        for (Referencia ref : referencias) {
            int pagina = ref.paginaVirtual;
            boolean esEscritura = ref.accion.equals("W");

            procesarAcceso(pagina, esEscritura);

            if (terminar) break; 
        }

        System.out.println("Número de hits: " + hits);
        System.out.println("Número de fallas de página: " + fallas);
        System.out.println("Tiempo total para hits: " + tiempoHits + " ns");
        System.out.println("Tiempo total para fallas: " + tiempoFallas + " ns");
        System.out.println("Tiempo total si fueran solo hits: " + hits*25 + " ns");
        System.out.println("Tiempo total solo fallas: " + fallas*10000000+ " ns");
    }
    private void procesarAcceso(int numeroPagina, boolean esEscritura) {
        synchronized (lock) {
            Pagina pagina = tablaPaginas.get(numeroPagina);

            if (pagina != null && marcos.contains(pagina)) {
                hits++;
                tiempoHits += 25;  
                pagina.referenciar();
                if (esEscritura) {
                    pagina.modificar();
                }
            } else {
                fallas++;
                tiempoFallas += 10000000;  
                
                if (marcos.size() >= numMarcos) {
                    reemplazarPagina();
                }

                Pagina nuevaPagina = new Pagina(numeroPagina);
                nuevaPagina.referenciar();
                if (esEscritura) {
                    nuevaPagina.modificar();
                }
                tablaPaginas.put(numeroPagina, nuevaPagina);
                marcos.add(nuevaPagina);  
            }
        }
    }
    private void reemplazarPagina() {
        Pagina paginaReemplazo = null;
        for (int i = 0; i < marcos.size(); i++) {
            Pagina pagina = marcos.get(i);
            if (!pagina.bitR && !pagina.bitM) {
                paginaReemplazo = pagina;
                marcos.remove(i);
                tablaPaginas.remove(pagina.numero);
                break;
            }
        }
        if (paginaReemplazo == null) {
            for (int i = 0; i < marcos.size(); i++) {
                Pagina pagina = marcos.get(i);
                if (!pagina.bitR && pagina.bitM) {
                    paginaReemplazo = pagina;
                    marcos.remove(i);
                    tablaPaginas.remove(pagina.numero);
                    break;
                }
            }
        }
        if (paginaReemplazo == null) {
            for (int i = 0; i < marcos.size(); i++) {
                Pagina pagina = marcos.get(i);
                if (pagina.bitR && !pagina.bitM) {
                    paginaReemplazo = pagina;
                    marcos.remove(i);
                    tablaPaginas.remove(pagina.numero);
                    break;
                }
            }
        }
        if (paginaReemplazo == null) {
            for (int i = 0; i < marcos.size(); i++) {
                Pagina pagina = marcos.get(i);
                if (pagina.bitR && pagina.bitM) {
                    paginaReemplazo = pagina;
                    marcos.remove(i);
                    tablaPaginas.remove(pagina.numero);
                    break;
                }
            }
        }
    }
    public void finalizar() {
        terminar = true;
    }

    Map<Integer, Pagina> getTablaPaginas() {
        return tablaPaginas;
    }
}
