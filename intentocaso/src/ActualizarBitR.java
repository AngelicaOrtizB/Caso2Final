
import java.util.*;

class ActualizarBitR extends Thread {
    private Map<Integer, Pagina> tablaPaginas;
    private boolean terminar = false;
    private Object lock;

    public ActualizarBitR(Map<Integer, Pagina> tablaPaginas, Object lock) {
        this.tablaPaginas = tablaPaginas;
        this.lock = lock;
    }

    @Override
    public void run() {
        while (!terminar) {
            try {
                Thread.sleep(2); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock) {
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

