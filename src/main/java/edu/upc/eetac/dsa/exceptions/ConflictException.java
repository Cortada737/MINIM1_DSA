package edu.upc.eetac.dsa.exceptions;

/**
 * Conflicte amb l'estat actual (duplicat, solapament, dron en manteniment).
 * El servei REST la tradueix a HTTP 409.
 */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
