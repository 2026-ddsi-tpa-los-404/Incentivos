package ar.edu.utn.dds.k3003.exceptions;


import ar.edu.utn.dds.k3003.model.Mision;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsigniaNoEncontradaExpection.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleInsigniaNotFound(InsigniaNoEncontradaExpection e) {
        return e.getMessage();
    }

    @ExceptionHandler(MisionNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleMisionNotFound(MisionNoEncontradaException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DonadorNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleDonadorNotFound(DonadorNoEncontradoException e) {
        return e.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(RuntimeException e) {
        return e.getMessage();
    }


}
