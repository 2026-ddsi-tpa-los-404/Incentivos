package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/misiones")
public class MisionController {
    private Fachada fachada;

    public MisionController(Fachada fachada) {
        this.fachada = fachada;
    }

    @GetMapping()
    public List<MisionDTO> getMisiones() {
        return fachada.obtenerMisiones();
    }

    @GetMapping("/{misionID}")
    public MisionDTO getMisionById(@PathVariable String misionID){
        return fachada.obtenerMisionPorID(misionID);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MisionDTO postMision(@RequestBody MisionDTO misionDTO){
        return fachada.agregarMision(misionDTO);
    }


}
