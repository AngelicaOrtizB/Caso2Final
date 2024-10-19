class Pagina {
    int numero;     
    boolean bitR;    
    boolean bitM;  

    public Pagina(int numero) {
        this.numero = numero;
        this.bitR = true;  
        this.bitM = false; 
    }

    public void referenciar() {
        this.bitR = true;  
    }

    public void modificar() {
        this.bitM = true;  
    }

    public void resetR() {
        this.bitR = false; 
    }

}

