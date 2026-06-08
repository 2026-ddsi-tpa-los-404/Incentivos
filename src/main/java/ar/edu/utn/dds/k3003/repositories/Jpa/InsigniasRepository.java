package ar.edu.utn.dds.k3003.repositories.Jpa;

import ar.edu.utn.dds.k3003.model.Insignia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsigniasRepository extends JpaRepository<Insignia,Long> {
}
