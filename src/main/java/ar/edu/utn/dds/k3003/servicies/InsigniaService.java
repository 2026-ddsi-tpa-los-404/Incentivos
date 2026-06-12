package ar.edu.utn.dds.k3003.servicies;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.exceptions.InsigniaNoEncontradaExpection;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.repositories.mappers.InsigniaMapper;
import ar.edu.utn.dds.k3003.repositories.Jpa.InsigniasRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import io.micrometer.core.instrument.Counter;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class InsigniaService {

    InsigniasRepository insigniasRepository;
    InsigniaMapper insigniaMapper;

    private Counter insigniasConsultadasCounter;
    private Counter insigniasAgregadasCounter;

    public InsigniaService(InsigniasRepository insigniasRepository,MeterRegistry registry) {
        this.insigniasRepository = insigniasRepository;
        this.insigniaMapper = new InsigniaMapper();
        this.insigniasAgregadasCounter = registry.counter("incentivos.insignias.consultadas");
        this.insigniasAgregadasCounter = registry.counter("incentivos.insignias.agregadas");

    }

    public List<InsigniaDTO> obtenerInsignias(){
        insigniasConsultadasCounter.increment();
        return insigniasRepository.findAll().stream()
                .map(i->insigniaMapper.toInsigniaDTO(i)).toList();
    }

    public InsigniaDTO obtenerInsigniaPorID(String insigniaID){
        insigniasConsultadasCounter.increment();
        return insigniasRepository.findById(Long.parseLong(insigniaID))
                .map(i->insigniaMapper.toInsigniaDTO(i))
                .orElseThrow(()-> new InsigniaNoEncontradaExpection(insigniaID));
    }


    public InsigniaDTO agregarInsignia(InsigniaDTO insignia) {
        if (insignia == null){
            throw new RuntimeException("La insignia no puede ser nula");
        }

        Insignia insigniaAGuardar = insigniaMapper.toInsignia(insignia);
        insigniasRepository.save(insigniaAGuardar);
        insigniasAgregadasCounter.increment();
        return insigniaMapper.toInsigniaDTO(insigniaAGuardar);
    }

    public void eliminarInsignia(String insigniaID) {
        insigniasRepository.deleteById(Long.parseLong(insigniaID));
    }

    public void eliminarTodasLasInsignias() {
        insigniasRepository.deleteAll();
    }
}
