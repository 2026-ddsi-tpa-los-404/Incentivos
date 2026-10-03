package ar.edu.utn.dds.k3003.exceptions;

public class ServicioExternoException extends RuntimeException {
    public ServicioExternoException(String servicio, Throwable causa) {
        super("El servicio " + servicio + " no está disponible o respondió con error", causa);
    }
}
