package ar.edu.utn.dds.k3003.exceptions;

public class MisionNoEncontradaException extends RuntimeException {
    public MisionNoEncontradaException(String id) {
        super("No existe misión con ID: " + id);
    }
}
