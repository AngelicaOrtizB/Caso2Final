
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GenerarReferencia {
    private int filas;
    private int columnas;
    private int numReferencias;
    private int numPaginas;
    private int tamanio;
    private String nom;
    private ArrayList<String> rgb = new ArrayList<>(Arrays.asList("R", "G", "B"));
    private String nomArch;
    public GenerarReferencia(int tamanio, String nombreArchivo,String nom) {
        this.tamanio = tamanio;
        this.nom = nombreArchivo;
        this.nomArch= nom;
    }

    public void realizarReferencia() {
        Imagen imagen = new Imagen(nom);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(nomArch+".txt"));
            writer.write("P=" + tamanio + "\n");
            columnas = imagen.ancho;
            filas = imagen.alto;
            writer.write("NF=" + filas + "\n");
            writer.write("NC=" + columnas + "\n");
            numReferencias = 17 * imagen.leerLongitud() + 16;
            writer.write("NR=" + numReferencias + "\n");
            numPaginas = (int) ((int) Math.ceil((double)((filas * columnas * 3) / (double)tamanio)) + Math.ceil((double)(imagen.leerLongitud() / (double)tamanio)));
            writer.write("NP=" + numPaginas+ "\n");
            int contadorBytes = 0;
            boolean salir = false;
            int pagina = 0;
            int fil = 0;
            int colu = 0;
            for (int i = 0; i < filas; i++) {
                if (salir) break;
                for (int a = 0; a < columnas; a++) {
                    if (salir) break;
                    for (String color : rgb) {
                        writer.write("Imagen[" + i + "][" + a + "]." + color + "," + pagina + "," + contadorBytes + ",R\n");
                        contadorBytes++;
                        fil = i;
                        colu = a;
                        if (contadorBytes == 16) {
                            salir = true;
                            break;
                        }
                    }
                }
            }
            int comienzoPag = (int) Math.ceil(((filas * columnas * 3) / tamanio));
            int longitud = imagen.leerLongitud();
            int numBits = 0;
            int mensaje = 0;
            int pagIm = 0;

            for (int i = fil; i < filas; i++) {
                if (i != fil) {
                    colu = 0;  
                }
                for (int a = colu; a < columnas; a++) {
                    for (String color : rgb) {
                        if (salir) {
                            salir = false;
                            continue;
                        }
                        if (numBits == 8) {
                            numBits = 0;
                            mensaje++;
                            pagina++;
                            if (mensaje % tamanio == 0) {
                                comienzoPag++;
                                pagina = 0;
                            }
                            if (pagina == tamanio) {
                                pagina = 0;
                            }
                        }
                        if (mensaje == longitud) {
                            break;
                        }
                        if (contadorBytes == tamanio) {
                            contadorBytes = 0;
                            pagIm++;
                        }
                        if (numBits == 0) {
                            writer.write("Mensaje[" + mensaje + "]," + comienzoPag + "," + pagina + ",W\n");
                        }
                        writer.write("Imagen[" + i + "][" + a + "]." + color + "," + pagIm + "," + contadorBytes + ",R\n");
                        writer.write("Mensaje[" + mensaje + "]," + comienzoPag + "," + pagina + ",W\n");
                        numBits++;
                        contadorBytes++;
                    }
                    if (mensaje == longitud) {
                        break;
                    }
                }
                if (mensaje == longitud) {
                    break;
                }
            }
            writer.close();
            System.out.println("Archivo de referencias generado con éxito.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}