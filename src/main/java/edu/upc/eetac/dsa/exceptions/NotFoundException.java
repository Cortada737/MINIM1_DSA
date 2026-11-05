package edu.upc.eetac.dsa.exceptions;

/**
 * Recurs inexistent. El servei REST la tradueix a HTTP 404.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
