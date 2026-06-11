package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class DonadorYEntidadesHTTP implements FachadaDonadoresYEntidades {

    private RestTemplate restTemplate;
    private String url;

    public DonadorYEntidadesHTTP(@Value("${url.donadores}") String url) {
        this.restTemplate = new RestTemplate();
        this.url = url;
    }

    @Override
    public DonadorDTO agregarDonador(DonadorDTO donadorDTO) {
        return null;
    }

    @Override
    public DonadorDTO buscarDonadorPorID(String donadorID) throws NoSuchElementException {
        return restTemplate.getForObject(url + "/donadores/" + donadorID, DonadorDTO.class);
    }

    @Override
    public DonadorDTO modifcarCategoria(String donadorID, String categoria) throws NoSuchElementException {
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
