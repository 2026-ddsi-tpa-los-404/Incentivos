package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.model.Insignia;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/insignias")
public class InsigniaController {
    private Fachada fachada;

    public InsigniaController(Fachada unaFachada) {
        this.fachada = unaFachada;
    }

    @GetMapping()
    public List<InsigniaDTO> getInsignias() {
        return fachada.obtenerInsignias();
    }

    @GetMapping("/{insigniaID}")
    public InsigniaDTO getInsigniaById(@PathVariable String insigniaID){
        return fachada.obtenerInsigniaPorID(insigniaID);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public InsigniaDTO postInsignia(@RequestBody InsigniaDTO insigniaDTO){
        return fachada.agregarInsignia(insigniaDTO);
    }


}


