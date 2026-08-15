package ar.edu.utn.dds.k3003.jobs;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.servicies.DonadorIncentivosService;
import io.micrometer.core.instrument.binder.logging.LogbackMetrics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProcesadorDonadoresJob {
    private static final Logger log = LoggerFactory.getLogger(ProcesadorDonadoresJob.class);

    private final Fachada fachada;
    private final DonadorIncentivosService donadorIncentivosService;

    public ProcesadorDonadoresJob(Fachada fachada,
                                  DonadorIncentivosService donadorIncentivosService) {
        this.fachada = fachada;
        this.donadorIncentivosService = donadorIncentivosService;
    }

    @Scheduled(fixedRate = 15000)  // cada 15 segundos
    @Transactional
    public void procesarDonadoresPeriodicamente() {
        log.info("[JOB] Iniciando procesamiento de donadores");

        var donadores = donadorIncentivosService.obtenerTodos();

        for (DonadorIncentivos donador : donadores) {
            try {
                fachada.procesarDonador(donador.getDonadorID());
                log.info("✅ Donador procesado correctamente - id={}", donador.getDonadorID());
            } catch (Exception e) {
                log.error("❌ Error procesando donador {} : {}", donador.getDonadorID(), e.getMessage());
            }
        }

        log.info("🏁 [JOB] Procesamiento finalizado - {} donadores", donadores.size());
    }
}
