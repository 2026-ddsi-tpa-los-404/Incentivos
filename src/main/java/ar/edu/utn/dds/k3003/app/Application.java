package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.repositories.InsigniasRepository;
import ar.edu.utn.dds.k3003.servicies.DonadorIncentivosService;
import ar.edu.utn.dds.k3003.servicies.InsigniaService;
import ar.edu.utn.dds.k3003.servicies.MisionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootApplication(scanBasePackages = "ar.edu.utn.dds.k3003")
@EnableJpaRepositories(basePackages = "ar.edu.utn.dds.k3003.repositories")
@EntityScan(basePackages = "ar.edu.utn.dds.k3003.model")
public class Application{
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Fachada fachada(InsigniaService insigniaService ,
                           MisionService misionService,
                           DonadorIncentivosService donadorIncentivosService,
                           FachadaDonadoresYEntidades fachadaDonadoresYEntidades) {
        Fachada fachada = new Fachada(insigniaService,misionService,donadorIncentivosService);
        fachada.setFachadaDonadoresYEntidades(fachadaDonadoresYEntidades);
        return fachada;
    }

    @Bean
    public FachadaDonadoresYEntidades fachadaDonadoresYEntidades() {
        return new FachadaDonadoresYEntidades() {

            @Override
            public DonadorDTO agregarDonador(DonadorDTO donadorDTO) {
                return null;
            }

            @Override
            public DonadorDTO buscarDonadorPorID(String donadorID) {
                return new DonadorDTO(donadorID, "nombre", "apellido", 25,
                        "email@test.com", "12345678", "domicilio",
                        EstadoDonadorEnum.VERIFICADO, "OCASIONAL");
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
            public DonadorDTO modifcarCategoria(String donadorID, String categoria) throws NoSuchElementException {
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
        };
    }
}
