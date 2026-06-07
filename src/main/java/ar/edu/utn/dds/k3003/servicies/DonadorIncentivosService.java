package ar.edu.utn.dds.k3003.servicies;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;
import ar.edu.utn.dds.k3003.repositories.DonadoresIncentivosRepository;
import ar.edu.utn.dds.k3003.repositories.InsigniasRepository;
import ar.edu.utn.dds.k3003.repositories.MisionesRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class DonadorIncentivosService {
    DonadoresIncentivosRepository donadoresIncentivosRepository;
    InsigniasRepository insigniasRepository;
    MisionesRepository misionesRepository;

    public DonadorIncentivosService(DonadoresIncentivosRepository donadoresIncentivosRepository,
        InsigniasRepository insigniasRepository,
        MisionesRepository misionesRepository){
        this.donadoresIncentivosRepository = donadoresIncentivosRepository;
        this.insigniasRepository = insigniasRepository;
        this.misionesRepository = misionesRepository;
    }

    public DonadorIncentivos buscarOCrearDonador(String donadorId) {
        return donadoresIncentivosRepository.findByDonadorID(donadorId)
                .orElseGet(() -> donadoresIncentivosRepository.save(new DonadorIncentivos(donadorId)));
    }

    public void agregarInsignia(String donadorId, String insigniaId) {
        Insignia insigniabuscada = insigniasRepository.findById(Long.parseLong(insigniaId))
                .orElseThrow(() -> new NoSuchElementException("no existe insignia con ese ID"));

        DonadorIncentivos donador = buscarOCrearDonador(donadorId);
        donador.getInsigniasDonador().add(insigniabuscada);
        donadoresIncentivosRepository.save(donador);
    }

    public void asignarMision(String donadorId, String misionId) {
        Mision mision = misionesRepository.findById(Long.parseLong(misionId))
                .orElseThrow(() -> new NoSuchElementException("no existe mision con ese ID"));
        DonadorIncentivos donador = buscarOCrearDonador(donadorId);
        donador.setMisionActual(mision);
        donadoresIncentivosRepository.save(donador);
    }

    public DonadorIncentivos obtenerDonador(String donadorId) {
        return donadoresIncentivosRepository.findByDonadorID(donadorId)
                .orElseThrow(() -> new NoSuchElementException("No existe donador con ese ID"));
    }

    public void eliminarDonador(String donadorId) {
        DonadorIncentivos donador = buscarOCrearDonador(donadorId);
        donador.getInsigniasDonador().clear(); // limpia la tabla intermedia
        donadoresIncentivosRepository.save(donador);
        donadoresIncentivosRepository.delete(donador);
    }

    public void eliminarTodos() {
        donadoresIncentivosRepository.findAll().forEach(d -> {
            d.getInsigniasDonador().clear();
            donadoresIncentivosRepository.save(d);
        });
        donadoresIncentivosRepository.deleteAll();
    }
}
