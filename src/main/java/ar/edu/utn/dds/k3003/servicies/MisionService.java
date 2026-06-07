package ar.edu.utn.dds.k3003.servicies;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.repositories.MisionMapper;
import ar.edu.utn.dds.k3003.repositories.MisionesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MisionService {
    MisionesRepository misionesRepository;
    MisionMapper misionMapper;

    public MisionService(MisionesRepository misionesRepository) {
        this.misionMapper = new MisionMapper();
        this.misionesRepository = misionesRepository;
    }

    public List<MisionDTO> obtenerMisiones(){
        return misionesRepository.findAll()
                .stream()
                .map(m->misionMapper.toDTO(m)).toList();
    }

    public MisionDTO obtenerMisionPorID(String misionID){
        return misionesRepository.findById(Long.parseLong(misionID))
                .map(m->misionMapper.toDTO(m))
                .orElseThrow(()-> new NoSuchElementException("no existe mision con ese ID"));
    }

    public MisionDTO agregarMision(MisionDTO mision) {
        if (mision == null) {
            throw new RuntimeException("La misión no puede ser nula");
        }

        Mision misionAGuardar = misionMapper.toMision(mision);
        misionesRepository.save(misionAGuardar);
        return misionMapper.toDTO(misionAGuardar);
    }

    public void eliminarMision(String misionID) {
        misionesRepository.deleteById(Long.parseLong(misionID));
    }

    public void eliminarTodasLasMisiones() {
        misionesRepository.deleteAll();
    }
}
