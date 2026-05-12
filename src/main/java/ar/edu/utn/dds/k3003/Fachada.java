package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonaciones;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.repositories.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;


public class Fachada implements FachadaIncentivos {
  private InsigniasMemoryRepo insigniasMemoryRepo;
  private MisionesMemoryRepo misionesMemoryRepo;
  private DonadorEIncentivosMemoryRepo donadorEIncentivosMemoryRepo;
  private FachadaDonaciones fachadaDonaciones;
  private FachadaDonadoresYEntidades fachadaDonadoresYEntidades;

  private MisionMapper misionMapper = new MisionMapper();
  private InsigniaMapper insigniaMapper = new InsigniaMapper();

  public Fachada() {
    this.insigniasMemoryRepo = new InsigniasMemoryRepo();
    this.misionesMemoryRepo = new MisionesMemoryRepo();
    this.donadorEIncentivosMemoryRepo = new DonadorEIncentivosMemoryRepo();
  }

  @Override
  public List<InsigniaDTO> obtenerInsignias(){
    return insigniasMemoryRepo.findAll()
            .orElse(List.of()).stream()
            .map(i->insigniaMapper.toInsigniaDTO(i)).toList();
  }

  @Override
  public InsigniaDTO obtenerInsigniaPorID(String insigniaID){
    return insigniasMemoryRepo.findById(insigniaID)
            .map(i->insigniaMapper.toInsigniaDTO(i))
            .orElseThrow(()-> new NoSuchElementException("no existe insignia con ese ID"));
  }

  @Override
  public InsigniaDTO agregarInsignia(InsigniaDTO insignia) {
    if (insignia == null){
      throw new RuntimeException("La insignia no puede ser nula");
    }

    if (insignia.id()!=null && insigniasMemoryRepo.findById(insignia.id()).isPresent()){
      throw new RuntimeException("La insignia con ese ID ya esta cargada en sistema");
    }

    Insignia insigniaAGuardar = insigniaMapper.toInsignia(insignia);
    insigniasMemoryRepo.save(insigniaAGuardar);
    return insigniaMapper.toInsigniaDTO(insigniaAGuardar);
  }

  @Override
  public List<MisionDTO> obtenerMisiones(){
    return misionesMemoryRepo.findAll()
            .orElse(List.of()).stream()
            .map(m->misionMapper.toDTO(m)).toList();
  }

  @Override
  public MisionDTO obtenerMisionPorID(String misionID){
    return misionesMemoryRepo.findById(misionID)
            .map(m->misionMapper.toDTO(m))
            .orElseThrow(()-> new NoSuchElementException("no existe mision con ese ID"));
  }

  @Override
  public MisionDTO agregarMision(MisionDTO mision) {
    if (mision == null) {
      throw new RuntimeException("La misión no puede ser nula");
    }
    if (mision.id() != null && misionesMemoryRepo.findById(mision.id()).isPresent()) {
      throw new RuntimeException("La misión ya existe en el sistema");
    }

    Mision misionAGuardar = misionMapper.toMision(mision);
    misionesMemoryRepo.save(misionAGuardar);
    return misionMapper.toDTO(misionAGuardar);
  }

  @Override
  public List<InsigniaDTO> getInsigniasDeDonador(String donadorID) throws NoSuchElementException {

    List<Insignia> insigniasDelDonador = donadorEIncentivosMemoryRepo.insigniasDelDonador(donadorID);

    if(insigniasDelDonador.isEmpty())
      throw new RuntimeException("no existe insignias para ese ID");

    return insigniasDelDonador.stream().map(i->insigniaMapper.toInsigniaDTO(i)).toList();
  }

  @Override
  public MisionDTO getMisionEnCursoDeDonador(String donadorID) throws NoSuchElementException {
    Mision misionActualDelDonador = donadorEIncentivosMemoryRepo.misionDelDonador(donadorID);

    if (misionActualDelDonador == null){
      throw new RuntimeException("No existe para ese ID");
    }
    return misionMapper.toDTO(misionActualDelDonador);
  }

  @Override
  public void asignarMisionADonador(String donadorID, MisionDTO misionDTO) throws NoSuchElementException {

    //verifo que no sea una mision nula
    if (misionDTO == null) throw new RuntimeException("La misión no puede ser nula");

    //verifico que exista el donador
    try{
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    }
    catch(RuntimeException e){
      throw new RuntimeException("No existe donador con ese ID");
    }

    donadorEIncentivosMemoryRepo.agregarMision(donadorID,misionMapper.toMision(misionDTO));

  }

  @Override
  public void asignarInsigniaADonador(String donadorID, InsigniaDTO insigniaDTO) throws NoSuchElementException {

    if (insigniaDTO == null) throw new RuntimeException("La insignia no puede ser nula");
    try{
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    }
    catch (RuntimeException e){
      throw new RuntimeException("No existe donador con ese ID");
    }

    Insignia insigniaNueva = insigniaMapper.toInsignia(insigniaDTO);
    donadorEIncentivosMemoryRepo.agregarInsignia(donadorID,insigniaNueva);

  }

  @Override
  public void procesarDonador(String donadorID) throws NoSuchElementException {

    try{
      //me aseguro que exista el donador
      fachadaDonadoresYEntidades.buscarDonadorPorID(donadorID);
    }
    catch (NoSuchElementException e){
      throw new RuntimeException("No existe donador con ese ID");
    }

    //me traigo sus donaciones con su ID
    List<DonacionDTO> donacionesDelDonador =
            fachadaDonaciones.buscarPorDonadorYFechaInicio(donadorID, LocalDate.parse("2025-01-01"));

    //me traigo tambien la mision actual
    Mision misionActualDelDonador = misionMapper.toMision(this.getMisionEnCursoDeDonador(donadorID));


    if(misionActualDelDonador.estaCompleta(donacionesDelDonador,fachadaDonaciones)){

      //busco la insignia en el repo con el id de la misionactual, y la asigno al donador
      insigniasMemoryRepo.findById(misionActualDelDonador.getInsigniaId()).
              ifPresent(i->donadorEIncentivosMemoryRepo.agregarInsignia(donadorID,i));

      //cambio la categoria del donador al completarse la mision puntual
      fachadaDonadoresYEntidades.modifcarCategoria(donadorID,misionActualDelDonador.getCategoriaDonadorFin().toString());
    }

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