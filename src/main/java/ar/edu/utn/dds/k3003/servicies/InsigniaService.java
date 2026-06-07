package ar.edu.utn.dds.k3003.servicies;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.repositories.InsigniaMapper;
import ar.edu.utn.dds.k3003.repositories.InsigniasRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class InsigniaService {

    InsigniasRepository insigniasRepository;
    InsigniaMapper insigniaMapper;

    public InsigniaService(InsigniasRepository insigniasRepository) {
        this.insigniasRepository = insigniasRepository;
        this.insigniaMapper = new InsigniaMapper();
    }

    public List<InsigniaDTO> obtenerInsignias(){
        return insigniasRepository.findAll().stream()
                .map(i->insigniaMapper.toInsigniaDTO(i)).toList();
    }

    public InsigniaDTO obtenerInsigniaPorID(String insigniaID){
        return insigniasRepository.findById(Long.parseLong(insigniaID))
                .map(i->insigniaMapper.toInsigniaDTO(i))
                .orElseThrow(()-> new NoSuchElementException("no existe insignia con ese ID"));
    }


    public InsigniaDTO agregarInsignia(InsigniaDTO insignia) {
        if (insignia == null){
            throw new RuntimeException("La insignia no puede ser nula");
        }

        Insignia insigniaAGuardar = insigniaMapper.toInsignia(insignia);
        insigniasRepository.save(insigniaAGuardar);
        return insigniaMapper.toInsigniaDTO(insigniaAGuardar);
    }

    public void eliminarInsignia(String insigniaID) {
        insigniasRepository.deleteById(Long.parseLong(insigniaID));
    }

    public void eliminarTodasLasInsignias() {
        insigniasRepository.deleteAll();
    }
}
