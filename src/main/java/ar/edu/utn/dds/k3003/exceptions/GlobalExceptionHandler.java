package ar.edu.utn.dds.k3003.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InsigniaNoEncontradaExpection.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleInsigniaNotFound(InsigniaNoEncontradaExpection e) {
        log.warn("404 - {}", e.getMessage());
        return cuerpo(e);
    }

    @ExceptionHandler(MisionNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleMisionNotFound(MisionNoEncontradaException e) {
        log.warn("404 - {}", e.getMessage());
        return cuerpo(e);
    }

    @ExceptionHandler(DonadorNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleDonadorNotFound(DonadorNoEncontradoException e) {
        log.warn("404 - {}", e.getMessage());
        return cuerpo(e);
    }

    @ExceptionHandler(DonadorSinMisionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDonadorSinMision(DonadorSinMisionException e) {
        log.warn("409 - {}", e.getMessage());
        return cuerpo(e);
    }

    @ExceptionHandler(ServicioExternoException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public Map<String, String> handleServicioExterno(ServicioExternoException e) {
        log.error("503 - {}", e.getMessage());
        return cuerpo(e);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBadRequest(RuntimeException e) {
        log.warn("400 - {}", e.getMessage());
        return cuerpo(e);
    }

    @ExceptionHandler(MisionNoCompletadaException.class)
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> handleMisionNoCompletada(MisionNoCompletadaException e) {
        return cuerpo(e);
    }

    // Respuesta JSON {"mensaje": "..."}: es la clave que lee el MCP. Map.of no acepta null.
    private Map<String, String> cuerpo(RuntimeException e) {
        String mensaje = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        return Map.of("mensaje", mensaje);
    }

}
