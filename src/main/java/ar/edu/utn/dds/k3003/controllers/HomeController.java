package ar.edu.utn.dds.k3003.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String bienvenida(){
        return "API de Modulo de incentivos de  DonaTrack funcionando";
    }
}
