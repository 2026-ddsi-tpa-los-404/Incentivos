package ar.edu.utn.dds.k3003.exceptions;

public class DonadorNoEncontradoException extends RuntimeException {
  public DonadorNoEncontradoException(String id) {
    super("No existe donador con ID: " + id);
  }
}
