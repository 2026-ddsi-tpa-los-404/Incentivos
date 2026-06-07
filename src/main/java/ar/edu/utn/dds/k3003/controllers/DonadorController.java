package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/donadores")
public class DonadorController {

  private Fachada fachada;

  public DonadorController(Fachada fachada) {
    this.fachada = fachada;
  }

    @GetMapping("/{donadorID}/insignias")
    public List<InsigniaDTO> getInsigniasDeDonadorByID(@PathVariable String donadorID){
        return fachada.getInsigniasDeDonador(donadorID);
    }

    @PostMapping("/{donadorID}/insignias")
    public ResponseEntity<String> postInsigniaADonador(@PathVariable String donadorID,
                                     @RequestBody Map<String, String> body){
        InsigniaDTO insignia = fachada.obtenerInsigniaPorID(body.get("insigniaID"));
        fachada.asignarInsigniaADonador(donadorID,insignia);
        return ResponseEntity.ok("Insignia asignada correctamente al donador " + donadorID);
  }

    @GetMapping("/{donadorID}/mision")
    public MisionDTO getMisionDeDonadorByID(@PathVariable String donadorID){
        return fachada.getMisionEnCursoDeDonador(donadorID);
    }

    @PostMapping("/{donadorID}/mision")
    public ResponseEntity<String> postMisionADonador(@PathVariable String donadorID,
                                   @RequestBody Map<String, String> body){
        MisionDTO mision = fachada.obtenerMisionPorID(body.get("misionID"));
        fachada.asignarMisionADonador(donadorID,mision);
        return ResponseEntity.ok("Mision asignada correctamente al donador " + donadorID);
    }

  @PostMapping("/{donadorID}/procesamiento")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void procesarmientoDeDonadorPorID(@PathVariable String donadorID){
        fachada.procesarDonador(donadorID);
    }

  @DeleteMapping("/limpiar")
  public ResponseEntity<String> limpiarTodo() {
      fachada.eliminarTodosLosDonadores();
      return ResponseEntity.ok("Base de datos de donadores limpiada");
  }
}
