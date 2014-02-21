package com.spontecorp.littleligues.utils;

/**
 *
 * @author jgcastillo
 */
public enum StatusPartidoEnum {

    NO_COMENZADO("Sin Empezar", 0),
    PRIMER_TIEMPO("1er Tiempo", 1),
    SEGUNDO_TIEMPO("2do Tiempo", 2),
    PRORROGA("Prorroga", 3),
    PENALTIES("Penalties", 4),
    FINALIZADO("Finalizado", 6),
    SUSPENDIDO("Suspendido", 7),
    FORFAIT("Incomparecencia", 8),
    COM_JUSTICIA("Comisión de Justicia", 9),
    DEC_COM_JUSTICIA("Decidido por Com. de Justicia", 10);
    
    private final int valor;
    private final String etiqueta;
    
    StatusPartidoEnum(String etiqueta, int valor){
        this.etiqueta = etiqueta;
        this.valor = valor;
    }
    
    public int valor(){
        return valor;
    }
    
    public String etiqueta(){
        return etiqueta;
    }
    
}
