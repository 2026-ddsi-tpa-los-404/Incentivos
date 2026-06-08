package ar.edu.utn.dds.k3003.exceptions;

public class InsigniaNoEncontradaExpection extends RuntimeException {
    public InsigniaNoEncontradaExpection(String id) {
        super("No existe insignia con ID: " + id);
    }
}
