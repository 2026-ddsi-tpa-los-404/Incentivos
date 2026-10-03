package ar.edu.utn.dds.k3003.apisexternas;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.exceptions.ServicioExternoException;
import feign.FeignException;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class DonadorYEntidadesHTTP implements FachadaDonadoresYEntidades {

    private static final Logger log = LoggerFactory.getLogger(DonadorYEntidadesHTTP.class);

    private DonadorClient donadorClient;
    private Counter donadoresOkCounter;
    private Counter donadoresErrorCounter;

    public DonadorYEntidadesHTTP(DonadorClient donadorClient, MeterRegistry registry) {
        this.donadorClient = donadorClient;
        this.donadoresOkCounter = registry.counter("incentivos.http.donadores", "status", "ok");
        this.donadoresErrorCounter = registry.counter("incentivos.http.donadores", "status", "error");
    }

    @Override
    public DonadorDTO buscarDonadorPorID(String donadorID) throws NoSuchElementException {
        try {
            DonadorDTO resultado = donadorClient.buscarPorID(donadorID);
            donadoresOkCounter.increment();
            return resultado;
        } catch (FeignException.NotFound e) {
            donadoresErrorCounter.increment();
            log.warn("Donadores respondió 404 para el donador {}", donadorID);
            throw new NoSuchElementException("No existe donador con ID: " + donadorID);
        } catch (FeignException e) {
            donadoresErrorCounter.increment();
            log.error("Error llamando a Donadores (status={}) al buscar el donador {}: {}", e.status(), donadorID, e.getMessage());
            throw new ServicioExternoException("Donadores", e);
        }
    }

    @Override
    public DonadorDTO modifcarCategoria(String donadorID, String categoria) throws NoSuchElementException {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("categoria", categoria);
            DonadorDTO resultado = donadorClient.modifcarCategoria(donadorID, body);
            donadoresOkCounter.increment();
            log.info("Categoría del donador {} actualizada a {} en Donadores", donadorID, categoria);
            return resultado;
        } catch (FeignException.NotFound e) {
            donadoresErrorCounter.increment();
            log.warn("Donadores respondió 404 al modificar la categoría del donador {}", donadorID);
            throw new NoSuchElementException("No existe donador con ID: " + donadorID);
        } catch (FeignException e) {
            donadoresErrorCounter.increment();
            log.error("Error llamando a Donadores (status={}) al modificar la categoría del donador {}: {}", e.status(), donadorID, e.getMessage());
            throw new ServicioExternoException("Donadores", e);
        }
    }

    @Override
    public DonadorDTO agregarDonador(DonadorDTO donadorDTO) {
        return null;
    }

    @Override
    public EntidadBeneficaDTO agregarEntidad(EntidadBeneficaDTO entidadBeneficaDTO) {
        return null;
    }

    @Override
    public EntidadBeneficaDTO buscarEntidadPorID(String entidadID) throws NoSuchElementException {
        return null;
    }

    @Override
    public NecesidadMaterialDTO registrarNecesidad(NecesidadMaterialDTO necesidadMaterialDTO) {
        return null;
    }

    @Override
    public QuejaDTO agregarQueja(QuejaDTO quejaDTO) throws NoSuchElementException {
        return null;
    }

    @Override
    public Boolean puedeDonar(String donadorID) throws NoSuchElementException {
        return null;
    }

    @Override
    public List<QuejaDTO> obtenerQuejasDe(String donadorID) throws NoSuchElementException {
        return List.of();
    }

    @Override
    public DonadorDTO modificarEstado(String donadorID, EstadoDonadorEnum estado) throws NoSuchElementException {
        return null;
    }

    @Override
    public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechasDe(String productoSolicitadoID) {
        return List.of();
    }

    @Override
    public NecesidadMaterialDTO satisfacerNecesidad(String necesidadID, Integer cantidad) throws NoSuchElementException {
        return null;
    }

    @Override
    public DonadorStatsDTO estadisticasDonador(String donadorID) {
        return null;
    }

    @Override
    public void setFachadaIncentivos(FachadaIncentivos fachadaIncentivos) {

    }
}
