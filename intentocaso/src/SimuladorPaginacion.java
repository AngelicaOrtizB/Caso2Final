
import java.util.*;

class SimuladorPaginacion extends Thread {
    private int numMarcos;
    private List<Referencia> referencias;  
    private List<Pagina> marcos;  
    private Map<Integer, Pagina> tablaPaginas;  
    private int hits = 0;
    private int fallas = 0;
    private long tiempoHits = 0;
    private long tiempoFallas = 0;
    private boolean terminar = false;

    public SimuladorPaginacion(int numMarcos, List<Referencia> referencias) {
        this.numMarcos = numMarcos;
        this.referencias = referencias;
        this.marcos = new ArrayList<>(numMarcos);  
        this.tablaPaginas = new HashMap<>();
    }

    @Override
    public void run() {
        for (Referencia ref : referencias) {
            int pagina = ref.paginaVirtual;
            boolean esEscritura = ref.accion.equals("W");

            accesoMemoria(pagina, esEscritura);

            if (terminar) break; 
        }
        double f= (double)fallas*100/referencias.size();
        double h= (double)hits*100/referencias.size();


        System.out.println("Número de hits: " + hits);
        System.out.println("Número de fallas de página: " + fallas);
        System.out.println("Porcentaje de hits: " + h);
        System.out.println("Porcentaje de fallas de página: " + f);
        System.out.println("Tiempo total para hits: " + tiempoHits + " ns");
        System.out.println("Tiempo total para fallas: " + tiempoFallas + " ns");
        System.out.println("Tiempo total si fueran solo hits: " + hits*25 + " ns");
        System.out.println("Tiempo total solo fallas: " + fallas*10000000+ " ns");
    }
    private void accesoMemoria(int numeroPagina, boolean esEscritura) {
        synchronized (tablaPaginas) {
            
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
