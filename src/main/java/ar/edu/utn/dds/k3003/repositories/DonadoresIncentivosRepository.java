package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.DonadorIncentivos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonadoresIncentivosRepository extends JpaRepository<DonadorIncentivos,Long> {
    Optional<DonadorIncentivos> findByDonadorID(String donadorID);
}
