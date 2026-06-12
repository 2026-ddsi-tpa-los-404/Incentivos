package ar.edu.utn.dds.k3003.servicies;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.exceptions.MisionNoEncontradaException;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.repositories.Jpa.InsigniasRepository;
import ar.edu.utn.dds.k3003.repositories.mappers.MisionMapper;
import ar.edu.utn.dds.k3003.repositories.Jpa.MisionesRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MisionService {
    InsigniasRepository insigniasRepository;
    MisionesRepository misionesRepository;
    MisionMapper misionMapper;
    private Counter misionesAgregadasCounter;

    public MisionService(MisionesRepository misionesRepository,
                         InsigniasRepository insigniasRepository,
                         MeterRegistry registry) {
        this.misionesRepository = misionesRepository;
        this.insigniasRepository = insigniasRepository;
        this.misionMapper = new MisionMapper(insigniasRepository);
        this.misionesAgregadasCounter = registry.counter("incentivos.misiones.agregadas");
    }

    public List<MisionDTO> obtenerMisiones(){
        return misionesRepository.findAll()
                .stream()
                .map(m->misionMapper.toDTO(m)).toList();
    }

    public MisionDTO obtenerMisionPorID(String misionID){
        return misionesRepository.findById(Long.parseLong(misionID))
                .map(m->misionMapper.toDTO(m))
                .orElseThrow(()-> new MisionNoEncontradaException(misionID));
    }

    public MisionDTO agregarMision(MisionDTO mision) {
        if (mision == null) {
            throw new RuntimeException("La misión no puede ser nula");
        }

        Mision misionAGuardar = misionMapper.toMision(mision);
        misionesRepository.save(misionAGuardar);
        misionesAgregadasCounter.increment();
        return misionMapper.toDTO(misionAGuardar);
    }

    public void eliminarMision(String misionID) {
        misionesRepository.deleteById(Long.parseLong(misionID));
    }

    public void eliminarTodasLasMisiones() {
        misionesRepository.deleteAll();
    }

    public MisionDTO misionToDTO(Mision mision) {
        return misionMapper.toDTO(mision);
    }
}
