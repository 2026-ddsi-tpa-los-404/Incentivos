package ar.edu.utn.dds.k3003.exceptions;

public class MisionNoCompletadaException extends RuntimeException {
    public MisionNoCompletadaException(String donadorID) {
        super("El donador " + donadorID + " aún no completó los requisitos de la misión");
    }
}
