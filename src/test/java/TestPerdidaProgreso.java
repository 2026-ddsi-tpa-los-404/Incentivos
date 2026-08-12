import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.model.misiones.DonacionesExitosas;
import ar.edu.utn.dds.k3003.servicies.DonadorIncentivosService;
import ar.edu.utn.dds.k3003.servicies.InsigniaService;
import ar.edu.utn.dds.k3003.servicies.MisionService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class TestPerdidaProgreso {

    // Las dependencias de la Fachada. Las que son de otros módulos van mockeadas.
    private FachadaDonaciones fachadaDonaciones;
    private FachadaDonadoresYEntidades fachadaDonadoresYEntidades;
    private DonadorIncentivosService donadorIncentivosService;
    private InsigniaService insigniaService;
    private MisionService misionService;
    private MeterRegistry registry;

    private Fachada fachada;

    private final String DONADOR_ID = "donador-1";
    private final Long INSIGNIA_ID = 99L;

    @BeforeEach
    void setUp() {
        fachadaDonaciones = mock(FachadaDonaciones.class);
        fachadaDonadoresYEntidades = mock(FachadaDonadoresYEntidades.class);
        donadorIncentivosService = mock(DonadorIncentivosService.class);
        insigniaService = mock(InsigniaService.class);
        misionService = mock(MisionService.class);
        registry = new SimpleMeterRegistry(); // registry real de verdad, no mock: los counters funcionan

        fachada = new Fachada(insigniaService, misionService, donadorIncentivosService,
                fachadaDonaciones, fachadaDonadoresYEntidades, registry);
    }

    // ---------- helpers para armar el escenario ----------

    private DonacionDTO donacionAceptada() {
        return new DonacionDTO("d-" + Math.random(), DONADOR_ID, "dep-1",
                "desc", "prod-1", 1, EstadoDonacionEnum.ACEPTADA);
    }

    private List<DonacionDTO> nDonacionesAceptadas(int n) {
        List<DonacionDTO> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) lista.add(donacionAceptada());
        return lista;
    }

    // Arma la insignia de la misión
    private Insignia insigniaDeLaMision() {
        Insignia insignia = new Insignia();
        insignia.setId(INSIGNIA_ID);
        return insignia;
    }

    // Arma la misión "Donaciones Exitosas" con su insignia y sus categorías
    private DonacionesExitosas misionDonacionesExitosas() {
        return new DonacionesExitosas("Donaciones Exitosas", insigniaDeLaMision(),
                CategoriaDonadorEnum.TRANSFORMADOR,  // categoría fin (cuando gana)
                CategoriaDonadorEnum.COLABORADOR,    // categoría inicio (a la que vuelve al perder)
                TipoMisionEnum.DONACIONES_EXITOSAS);
    }

    // Arma el DonadorIncentivos que devuelve el service, con o sin la insignia ya puesta
    private DonadorIncentivos donadorCon(Mision mision, boolean conInsignia) {
        DonadorIncentivos donador = new DonadorIncentivos(DONADOR_ID);
        donador.setMisionActual(mision);
        if (conInsignia) {
            donador.getInsigniasDonador().add(insigniaDeLaMision());
        }
        return donador;
    }

    // ---------- los tres escenarios ----------

    @Test
    void donadorQueBajaDe20Pierde() {
        // Donador que YA tenía la insignia (la había ganado antes)
        DonadorIncentivos donador = donadorCon(misionDonacionesExitosas(), true);
        when(donadorIncentivosService.obtenerDonador(DONADOR_ID)).thenReturn(donador);

        // Pero ahora solo tiene 19 ACEPTADA → ya no cumple
        when(fachadaDonaciones.buscarPorDonadorYFechaInicio(eq(DONADOR_ID), any()))
                .thenReturn(nDonacionesAceptadas(19));

        fachada.procesarDonador(DONADOR_ID);

        // Debe QUITAR la insignia y volver a la categoría de inicio
        verify(donadorIncentivosService).quitarInsignia(DONADOR_ID, INSIGNIA_ID.toString());
        verify(fachadaDonadoresYEntidades).modifcarCategoria(DONADOR_ID, "COLABORADOR");
        // Y NO debe asignar nada
        verify(donadorIncentivosService, never()).agregarInsignia(anyString(), anyString());
    }

    @Test
    void donadorConMenosDe20SinInsigniaNoCambiaNada() {
        // Donador sin insignia y con 19 → no gana ni pierde
        DonadorIncentivos donador = donadorCon(misionDonacionesExitosas(), false);
        when(donadorIncentivosService.obtenerDonador(DONADOR_ID)).thenReturn(donador);
        when(fachadaDonaciones.buscarPorDonadorYFechaInicio(eq(DONADOR_ID), any()))
                .thenReturn(nDonacionesAceptadas(19));

        fachada.procesarDonador(DONADOR_ID);

        verify(donadorIncentivosService, never()).agregarInsignia(anyString(), anyString());
        verify(donadorIncentivosService, never()).quitarInsignia(anyString(), anyString());
    }

    @Test
    void donadorQueLlegaA20Gana() {
        // Donador sin insignia todavía, con 20 → gana
        DonadorIncentivos donador = donadorCon(misionDonacionesExitosas(), false);
        when(donadorIncentivosService.obtenerDonador(DONADOR_ID)).thenReturn(donador);
        when(fachadaDonaciones.buscarPorDonadorYFechaInicio(eq(DONADOR_ID), any()))
                .thenReturn(nDonacionesAceptadas(20));

        fachada.procesarDonador(DONADOR_ID);

        verify(donadorIncentivosService).agregarInsignia(DONADOR_ID, INSIGNIA_ID.toString());
        verify(fachadaDonadoresYEntidades).modifcarCategoria(DONADOR_ID, "TRANSFORMADOR");
        verify(donadorIncentivosService, never()).quitarInsignia(anyString(), anyString());
    }
}