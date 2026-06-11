package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.repositories.mappers.InsigniaMapper;
import ar.edu.utn.dds.k3003.repositories.mappers.MisionMapper;
import ar.edu.utn.dds.k3003.servicies.DonadorIncentivosService;
import ar.edu.utn.dds.k3003.servicies.InsigniaService;
import ar.edu.utn.dds.k3003.servicies.MisionService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


  public class Fachada implements FachadaIncentivos{

  private InsigniaService insigniaService;
  private MisionService misionService;
  private DonadorIncentivosService donadorIncentivosService;

  private FachadaDonaciones fachadaDonaciones;
  private FachadaDonadoresYEntidades fachadaDonadoresYEntidades;

  private InsigniaMapper insigniaMapper = new InsigniaMapper();

  public Fachada(InsigniaService insigniaService,MisionService misionService ,DonadorIncentivosService donadorIncentivosService) {
    this.insigniaService = insigniaService;
    this.misionService = misionService;
    this.donadorIncentivosService = donadorIncentivosService;
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
    List<Insignia> insigniasDonador = donador.getInsigniasDonador();

    if(insigniasDonador.isEmpty())
      throw new RuntimeException("no existe insignias para ese ID");

    return insigniasDonador.stream().map(i->insigniaMapper.toInsigniaDTO(i)).toList();
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
      throw new RuntimeException("No existe donador con ese ID");
    }
    donadorIncentivosService.asignarMision(donadorID, misionDTO.id());
  }

  @Override
  public void asignarInsigniaADonador(String donadorID, InsigniaDTO insigniaDTO) throws NoSuchElementException {
    if (insigniaDTO == null) throw new RuntimeException("La insignia no puede ser nula");
    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (RuntimeException e) {
      throw new RuntimeException("Error al buscar donador: " + e.getMessage());
    }
    donadorIncentivosService.agregarInsignia(donadorID, insigniaDTO.id());
  }

  @Override
  public void procesarDonador(String donadorID) throws NoSuchElementException {

    try {
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    } catch (NoSuchElementException e) {
      throw new RuntimeException("No existe donador con ese ID");
    }

    List<DonacionDTO> donacionesDelDonador =
            fachadaDonaciones.buscarPorDonadorYFechaInicio(donadorID, LocalDate.parse("2025-01-01"));

    Mision misionActualDelDonador = donadorIncentivosService.obtenerDonador(donadorID).getMisionActual();

    if (misionActualDelDonador == null) throw new RuntimeException("El donador no tiene misión asignada");

    if (misionActualDelDonador.estaCompleta(donacionesDelDonador, fachadaDonaciones)) {
      donadorIncentivosService.agregarInsignia(donadorID, misionActualDelDonador.getInsignia().getId().toString());
      fachadaDonadoresYEntidades.modifcarCategoria(donadorID, misionActualDelDonador.getCategoriaDonadorFin().toString());
    }
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