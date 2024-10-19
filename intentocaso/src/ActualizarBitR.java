
import java.util.*;

class ActualizarBitR extends Thread {
    private Map<Integer, Pagina> tablaPaginas;
    private boolean terminar = false;

    public ActualizarBitR(Map<Integer, Pagina> tablaPaginas) {
        this.tablaPaginas = tablaPaginas;
    }

    @Override
    public void run() {
        while (!terminar) {
            try {
                Thread.sleep(2); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (tablaPaginas) {
                for (Pagina pagina : tablaPaginas.values()) {
                    pagina.resetR();
                }
            }
        }
    }
    public void finalizar() {
        terminar = true;
    }
}

