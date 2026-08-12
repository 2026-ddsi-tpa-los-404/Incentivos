package ar.edu.utn.dds.k3003.jobs;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.servicies.DonadorIncentivosService;
import org.springframework.scheduling.annotation.Scheduled;

public class ProcesadorDonadoresJob {

    private final Fachada fachada;
    private final DonadorIncentivosService donadorIncentivosService;

    public ProcesadorDonadoresJob(Fachada fachada,
                                  DonadorIncentivosService donadorIncentivosService) {
        this.fachada = fachada;
        this.donadorIncentivosService = donadorIncentivosService;
    }

    @Scheduled(fixedRate = 60000)  // cada 60 segundos
    public void procesarDonadoresPeriodicamente() {
        for (DonadorIncentivos donador : donadorIncentivosService.obtenerTodos()) {
            try {
                fachada.procesarDonador(donador.getDonadorID());
            } catch (Exception e) {
                System.err.println("Error procesando donador " + donador.getDonadorID() + ": " + e.getMessage());
            }
        }
    }
}
