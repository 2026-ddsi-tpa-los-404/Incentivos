package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoEncontradoException;
import ar.edu.utn.dds.k3003.exceptions.DonadorSinMisionException;
import ar.edu.utn.dds.k3003.exceptions.MisionNoCompletadaException;
import ar.edu.utn.dds.k3003.exceptions.ServicioExternoException;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.repositories.mappers.InsigniaMapper;
import ar.edu.utn.dds.k3003.repositories.mappers.MisionMapper;
import ar.edu.utn.dds.k3003.servicies.DonadorIncentivosService;
import ar.edu.utn.dds.k3003.servicies.InsigniaService;
import ar.edu.utn.dds.k3003.servicies.MisionService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Component
  public class Fachada implements FachadaIncentivos{

  private static final Logger log = LoggerFactory.getLogger(Fachada.class);

  private InsigniaService insigniaService;
  private MisionService misionService;
  private DonadorIncentivosService donadorIncentivosService;
  private FachadaDonaciones fachadaDonaciones;
  private FachadaDonadoresYEntidades fachadaDonadoresYEntidades;
  private InsigniaMapper insigniaMapper = new InsigniaMapper();

  private MeterRegistry registry;
  private Counter donadorProcesadoOkCounter;
  private Counter donadorProcesadoErrorCounter;
  private Timer procesamientoTimer;

  public Fachada(InsigniaService insigniaService,
                 MisionService misionService,
                 DonadorIncentivosService donadorIncentivosService,
                 FachadaDonaciones fachadaDonaciones,
                 FachadaDonadoresYEntidades fachadaDonadoresYEntidades,
                 MeterRegistry registry) {
    this.insigniaService = insigniaService;
    this.misionService = misionService;
    this.donadorIncentivosService = donadorIncentivosService;
    this.fachadaDonaciones = fachadaDonaciones;
    this.fachadaDonadoresYEntidades = fachadaDonadoresYEntidades;
    this.registry = registry;
    this.donadorProcesadoOkCounter = registry.counter("incentivos.donador.procesado", "status", "ok");
    this.donadorProcesadoErrorCounter = registry.counter("incentivos.donador.procesado", "status", "error");
    this.procesamientoTimer = Timer.builder("incentivos.donador.procesamiento.duracion")
            .description("Tiempo que tarda procesar un donador")
            .register(registry);
  }

  /*------------------------INSIGNIAS--------------------------------------*/
  @Override
  public List<InsigniaDTO> obtenerInsignias(){
    return insigniaService.obtenerInsignias();
  }

  @Override
  public InsigniaDTO obtenerInsigniaPorID(String insigniaID){
    return insigniaService.obtenerInsigniaPorID(insigniaID);
  }

  @Override
  public InsigniaDTO agregarInsignia(InsigniaDTO insignia) {
    InsigniaDTO creada = insigniaService.agregarInsignia(insignia);
    log.info("Insignia creada: id={} nombre='{}'", creada.id(), creada.nombre());
    return creada;
  }

  public void eliminarInsignia(String insigniaID) {
      insigniaService.eliminarInsignia(insigniaID);
      log.info("Insignia {} eliminada", insigniaID);
  }

  public void eliminarTodasLasInsignias() {
      insigniaService.eliminarTodasLasInsignias();
      log.info("Se eliminaron todas las insignias");
  }
  /*--------------------------------------------------------------------------*/

    /*------------------------MISIONES--------------------------------------*/

  @Override
  public List<MisionDTO> obtenerMisiones(){
    return misionService.obtenerMisiones();
  }

  @Override
  public MisionDTO obtenerMisionPorID(String misionID){
    return misionService.obtenerMisionPorID(misionID);
  }

  @Override
  public MisionDTO agregarMision(MisionDTO mision) {
    MisionDTO creada = misionService.agregarMision(mision);
    log.info("Misión creada: id={} nombre='{}' tipo={} insignia={}", creada.id(), creada.nombre(), creada.tipo(), creada.insigniaID());
    return creada;
  }

  public void eliminarMision(String misionID) {
      misionService.eliminarMision(misionID);
      log.info("Misión {} eliminada", misionID);
  }

  public void eliminarTodasLasMisiones(){
      misionService.eliminarTodasLasMisiones();
      log.info("Se eliminaron todas las misiones");
  }
  /*--------------------------------------------------------------------------*/

  /*--------------------------DONADOR INCENTIVOS----------------------------- */
  @Override
  public List<InsigniaDTO> getInsigniasDeDonador(String donadorID) throws NoSuchElementException {

    DonadorIncentivos donador = donadorIncentivosService.obtenerDonador(donadorID);

    List<Insignia> insigniasDonador = donador.getInsigniasDonador();

    return insigniasDonador.stream().map(i->insigniaMapper.toInsigniaDTO(i)).toList();
  }

  /*200 con la misión, 204 si no tiene misión en curso y 404 no existe en la base de datos.*/
  @Override
  public MisionDTO getMisionEnCursoDeDonador(String donadorID) throws NoSuchElementException {
    DonadorIncentivos donador = donadorIncentivosService.obtenerDonador(donadorID);
    Mision misionDonador = donador.getMisionActual();

    return misionDonador == null ? null : misionService.misionToDTO(misionDonador);
  }

  @Override
  public void asignarMisionADonador(String donadorID, MisionDTO misionDTO) throws NoSuchElementException {
    if (misionDTO == null) throw new RuntimeException("La misión no puede ser nula");
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (NoSuchElementException e) {
      throw new DonadorNoEncontradoException("No existe donador con ID: " + donadorID);
    }
    donadorIncentivosService.asignarMision(donadorID, misionDTO.id());
    log.info("Misión {} asignada al donador {}", misionDTO.id(), donadorID);
  }

  @Override
  public void asignarInsigniaADonador(String donadorID, InsigniaDTO insigniaDTO) throws NoSuchElementException {
    if (insigniaDTO == null) throw new RuntimeException("La insignia no puede ser nula");
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (NoSuchElementException e) {
      throw new DonadorNoEncontradoException("No existe donador con ID: " + donadorID);
    }
    donadorIncentivosService.agregarInsignia(donadorID, insigniaDTO.id());
    log.info("Insignia {} asignada manualmente al donador {}", insigniaDTO.id(), donadorID);
  }

  @Override
  public void procesarDonador(String donadorID) throws NoSuchElementException {
    Timer.Sample inicio = Timer.start(registry);
    try {
      evaluarMisionDelDonador(donadorID);
    } finally {
      inicio.stop(procesamientoTimer);
    }
  }

  private void evaluarMisionDelDonador(String donadorID) {
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (NoSuchElementException e) {
      donadorProcesadoErrorCounter.increment();
      log.warn("Procesamiento cancelado: el donador {} no existe en Donadores", donadorID);
      throw new DonadorNoEncontradoException("No existe donador con ID: " + donadorID);
    } catch (ServicioExternoException e) {
      donadorProcesadoErrorCounter.increment();
      throw e;
    }

    DonadorIncentivos donador = donadorIncentivosService.obtenerDonador(donadorID);
    Mision mision = donador.getMisionActual();

    if (mision == null) {
      donadorProcesadoErrorCounter.increment();
      log.warn("Procesamiento cancelado: el donador {} no tiene misión asignada", donadorID);
      throw new DonadorSinMisionException(donadorID);
    }

    List<DonacionDTO> donacionesDelDonador = fachadaDonaciones.buscarPorDonadorYFechaInicio(donadorID, LocalDate.parse("2025-01-01"));

    boolean completa = mision.estaCompleta(donacionesDelDonador, fachadaDonaciones);
    boolean tieneInsignia = donador.tieneInsignia(mision.getInsignia());
    String insigniaID = mision.getInsignia().getId().toString();

    log.debug("Donador {} - misión '{}': {} donaciones, completa={}, tieneInsignia={}",
            donadorID, mision.getNombre(), donacionesDelDonador.size(), completa, tieneInsignia);

    if (completa && !tieneInsignia) {
      donadorIncentivosService.agregarInsignia(donadorID, insigniaID);
      fachadaDonadoresYEntidades.modifcarCategoria(donadorID, mision.getCategoriaDonadorFin().toString());
      registrarResultadoMision("incentivos.misiones.completadas", mision, mision.getCategoriaDonadorFin().name(), "sube");
      log.info("Misión '{}' completada por el donador {}: se otorga la insignia {} y pasa a categoría {}",
              mision.getNombre(), donadorID, insigniaID, mision.getCategoriaDonadorFin());
    }
    else if (!completa && tieneInsignia) {
      donadorIncentivosService.quitarInsignia(donadorID, insigniaID);
      fachadaDonadoresYEntidades.modifcarCategoria(donadorID, mision.getCategoriaDonadorInicio().toString());
      registrarResultadoMision("incentivos.misiones.revertidas", mision, mision.getCategoriaDonadorInicio().name(), "baja");
      log.info("El donador {} dejó de cumplir la misión '{}': se revoca la insignia {} y vuelve a categoría {}",
              donadorID, mision.getNombre(), insigniaID, mision.getCategoriaDonadorInicio());
    }

    donadorProcesadoOkCounter.increment();
  }

  private void registrarResultadoMision(String metrica, Mision mision, String nuevaCategoria, String direccion) {
    registry.counter(metrica, "tipo", String.valueOf(mision.getTipoDeMision())).increment();
    registry.counter("incentivos.categoria.cambios", "categoria", nuevaCategoria, "direccion", direccion).increment();
  }

    public void eliminarDonadorIncentivos(String donadorID) {
      donadorIncentivosService.eliminarDonador(donadorID);
    }

    public void eliminarTodosLosDonadores() {
      donadorIncentivosService.eliminarTodos();
    }

  @Override
  public void setFachadaDonaciones(FachadaDonaciones fachadaDonaciones) {
    this.fachadaDonaciones = fachadaDonaciones;
  }

  @Override
  public void setFachadaDonadoresYEntidades(FachadaDonadoresYEntidades fachadaDonadoresYEntidades) {
    this.fachadaDonadoresYEntidades = fachadaDonadoresYEntidades;
  }
}