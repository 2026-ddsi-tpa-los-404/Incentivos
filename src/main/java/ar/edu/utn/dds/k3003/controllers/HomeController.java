package ar.edu.utn.dds.k3003.controllers;


import ar.edu.utn.dds.k3003.Fachada;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;

@RestController
public class HomeController {

    Fachada fachada;

    public HomeController(Fachada fachada) {
        this.fachada = fachada;
    }

    @GetMapping("/")
    public String bienvenida(){
        return "API de Modulo de incentivos de  DonaTrack funcionando";
    }

    @DeleteMapping("/reset")
    public ResponseEntity<String> borrarBaseDeDatos(){
        fachada.eliminarTodosLosDonadores();
        fachada.eliminarTodasLasMisiones();
        fachada.eliminarTodasLasInsignias();
        return ResponseEntity.ok("Base de datos borrada correctamente");
    }
}
