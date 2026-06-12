package ar.edu.utn.dds.k3003.apisexternas;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.IdentificadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.ProductoDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaLogistica;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class DonacionesHTTP implements FachadaDonaciones {
    private DonacionesClient donacionesClient;
    private Counter donacionesOkCounter;
    private Counter donacionesErrorCounter;

    public DonacionesHTTP(DonacionesClient donacionesClient, MeterRegistry registry) {
        this.donacionesClient = donacionesClient;
        this.donacionesOkCounter = registry.counter("incentivos.http.donaciones", "status", "ok");
        this.donacionesErrorCounter = registry.counter("incentivos.http.donaciones", "status", "error");
    }

    @Override
    public List<DonacionDTO> buscarPorDonadorYFechaInicio(String donadorID, LocalDate fecha) {
        try {
            List<DonacionDTO> resultado = donacionesClient.buscarPorDonadorYFecha(donadorID, fecha.toString());
            donacionesOkCounter.increment();
            return resultado;
        } catch (Exception e) {
            donacionesErrorCounter.increment();
            throw e;
        }
    }

    @Override
    public ProductoDTO buscarProductoPorID(String productoID) throws NoSuchElementException {
        try {
            ProductoDTO resultado = donacionesClient.buscarProductoPorID(productoID);
            donacionesOkCounter.increment();
            return resultado;
        } catch (Exception e) {
            donacionesErrorCounter.increment();
            throw e;
        }
    }


    @Override
    public DonacionDTO registrarDonacion(DonacionDTO donacionDTO) {
        return null;
    }

    @Override
    public DonacionDTO buscarDonacionPorID(String donacionID) throws NoSuchElementException {
        return null;
    }

    @Override
    public DonacionDTO cambiarEstadoDeDonacion(String donacionID, EstadoDonacionEnum estado) throws NoSuchElementException {
        return null;
    }

    @Override
    public DonacionDTO registrarQuejaEnDonacion(String donacionID, String descripcion) {
        return null;
    }

    @Override
    public ProductoDTO agregarProducto(ProductoDTO productoDTO) {
        return null;
    }


    @Override
    public IdentificadorDTO agregarIdentificador(IdentificadorDTO identificadorDTO) {
        return null;
    }

    @Override
    public IdentificadorDTO buscarIdentificadorPorID(String identificadorID) throws NoSuchElementException {
        return null;
    }

    @Override
    public void setFachadaDonadoresYEntidades(FachadaDonadoresYEntidades fachadaDonadoresYEntidades) {

    }

    @Override
    public void setFachadaLogistica(FachadaLogistica fachadaLogistica) {

    }
}
