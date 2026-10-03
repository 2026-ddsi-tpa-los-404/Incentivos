package ar.edu.utn.dds.k3003.exceptions;

public class DonadorSinMisionException extends RuntimeException {
    public DonadorSinMisionException(String donadorID) {
        super("El donador " + donadorID + " no tiene una misión asignada");
    }
}
