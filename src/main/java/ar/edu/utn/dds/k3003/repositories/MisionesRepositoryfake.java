package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Mision;

import java.util.List;
import java.util.Optional;

public interface MisionesRepositoryfake {

    Optional<Mision>  findById(String id);

    Optional<List<Mision>> findAll();

    Mision save(Mision mision);
}
