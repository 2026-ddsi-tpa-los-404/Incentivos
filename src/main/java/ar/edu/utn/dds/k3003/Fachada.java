package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoEncontradoException;
import ar.edu.utn.dds.k3003.exceptions.MisionNoCompletadaException;
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
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Component
  public class Fachada implements FachadaIncentivos{

  private InsigniaService insigniaService;
  private MisionService misionService;
  private DonadorIncentivosService donadorIncentivosService;
  private FachadaDonaciones fachadaDonaciones;
  private FachadaDonadoresYEntidades fachadaDonadoresYEntidades;
  private InsigniaMapper insigniaMapper = new InsigniaMapper();

  private Counter donadorProcesadoOkCounter;
  private Counter donadorProcesadoErrorCounter;
  private Counter misionesCompletadasCounter;
  private Counter misionesRevertidasCounter;

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
    this.donadorProcesadoOkCounter = registry.counter("incentivos.donador.procesado", "status", "ok");
    this.donadorProcesadoErrorCounter = registry.counter("incentivos.donador.procesado", "status", "error");
    this.misionesCompletadasCounter = registry.counter("incentivos.misiones.completadas");
    this.misionesRevertidasCounter = registry.counter("incentivos.misiones.revertidas");
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
    return insigniaService.agregarInsignia(insignia);
  }

  public void eliminarInsignia(String insigniaID) {
      insigniaService.eliminarInsignia(insigniaID);
  }

  public void eliminarTodasLasInsignias() {
      insigniaService.eliminarTodasLasInsignias();
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
    return misionService.agregarMision(mision);
  }

  public void eliminarMision(String misionID) {
      misionService.eliminarMision(misionID);
  }

  public void eliminarTodasLasMisiones(){
      misionService.eliminarTodasLasMisiones();
  }
  /*--------------------------------------------------------------------------*/

  /*--------------------------DONADOR INCENTIVOS----------------------------- */
  @Override
  public List<InsigniaDTO> getInsigniasDeDonador(String donadorID) throws NoSuchElementException {

    DonadorIncentivos donador = donadorIncentivosService.obtenerDonador(donadorID);


    /* lo quito para mejorar cuando hacen consulta y no tiene nada asignado
    List<Insignia> insigniasDonador = donador.getInsigniasDonador();

    if(insigniasDonador.isEmpty())
      throw new RuntimeException("no existe insignias para ese ID");

    return insigniasDonador.stream().map(i->insigniaMapper.toInsigniaDTO(i)).toList();*/

    return donador.getInsigniasDonador().stream()
            .map(i -> insigniaMapper.toInsigniaDTO(i))
            .toList();
  }

  @Override
  public MisionDTO getMisionEnCursoDeDonador(String donadorID) throws NoSuchElementException {
    DonadorIncentivos donador = donadorIncentivosService.obtenerDonador(donadorID);
    Mision misionDonador = donador.getMisionActual();

    if (misionDonador == null){
      throw new RuntimeException("No existe mision para ese ID");
    }

    return misionService.misionToDTO(misionDonador);
  }

  @Override
  public void asignarMisionADonador(String donadorID, MisionDTO misionDTO) throws NoSuchElementException {
    if (misionDTO == null) throw new RuntimeException("La misión no puede ser nula");
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (RuntimeException e) {
      throw new DonadorNoEncontradoException("No existe donador con ID: " + donadorID);
    }
    donadorIncentivosService.asignarMision(donadorID, misionDTO.id());
  }

  @Override
  public void asignarInsigniaADonador(String donadorID, InsigniaDTO insigniaDTO) throws NoSuchElementException {
    if (insigniaDTO == null) throw new RuntimeException("La insignia no puede ser nula");
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (RuntimeException e) {
      throw new DonadorNoEncontradoException("No existe donador con ID: " + donadorID);
    }
    donadorIncentivosService.agregarInsignia(donadorID, insigniaDTO.id());
  }

  @Override
  public void procesarDonador(String donadorID) throws NoSuchElementException {
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (NoSuchElementException e) {
      donadorProcesadoErrorCounter.increment();
      throw new RuntimeException("No existe donador con ese ID");
    }

    List<DonacionDTO> donacionesDelDonador =
            fachadaDonaciones.buscarPorDonadorYFechaInicio(donadorID, LocalDate.parse("2025-01-01"));

    Mision misionActualDelDonador = donadorIncentivosService.obtenerDonador(donadorID).getMisionActual();

    if (misionActualDelDonador == null) {
      donadorProcesadoErrorCounter.increment();
      return;
    }

    boolean completa = misionActualDelDonador.estaCompleta(donacionesDelDonador, fachadaDonaciones);
    boolean tieneInsignia = this.donadorTieneInsignia(donadorID,misionActualDelDonador.getInsignia().getId().toString());

    if(completa && !tieneInsignia){
      donadorIncentivosService.agregarInsignia(donadorID, misionActualDelDonador.getInsignia().getId().toString());
      fachadaDonadoresYEntidades.modifcarCategoria(donadorID, misionActualDelDonador.getCategoriaDonadorFin().toString());
      misionActualDelDonador.setCompletada(true);
      misionesCompletadasCounter.increment();
    }
    else if (!completa && tieneInsignia) {
      donadorIncentivosService.quitarInsignia(donadorID, misionActualDelDonador.getInsignia().getId().toString());
      fachadaDonadoresYEntidades.modifcarCategoria(donadorID, misionActualDelDonador.getCategoriaDonadorInicio().toString());
      misionActualDelDonador.setCompletada(false);
      misionesRevertidasCounter.increment();
    }

    donadorProcesadoOkCounter.increment();
  }

    public void eliminarDonadorIncentivos(String donadorID) {
      donadorIncentivosService.eliminarDonador(donadorID);
    }

    public void eliminarTodosLosDonadores() {
      donadorIncentivosService.eliminarTodos();
    }

  private boolean donadorTieneInsignia(String donadorID, String insigniaID) {
    return donadorIncentivosService.obtenerDonador(donadorID)
            .getInsigniasDonador().stream()
            .anyMatch(i -> i.getId().toString().equals(insigniaID));
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