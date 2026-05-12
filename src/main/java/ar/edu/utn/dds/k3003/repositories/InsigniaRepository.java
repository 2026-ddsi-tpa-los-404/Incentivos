package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Insignia;

import java.util.List;
import java.util.Optional;

public interface InsigniaRepository {
    Optional<List<Insignia>> findAll();
    Optional<Insignia> findById(String id);
    Insignia save(Insignia insignia);
    Insignia deleteById(String id);
}
