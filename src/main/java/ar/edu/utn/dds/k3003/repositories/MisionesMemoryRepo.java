package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Mision;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class MisionesMemoryRepo implements MisionesRepository{

    private final List<Mision> misiones = new ArrayList<>();
    private final AtomicLong idSecuencial = new AtomicLong(1);

    @Override
    public Optional<List<Mision>> findAll() {
        return Optional.ofNullable(this.misiones);
    }

    @Override
    public Mision save(Mision mision) {

        if(mision.getId() != null){
            this.misiones.add(mision);
        }
        else {
            mision.setId(String.valueOf(idSecuencial.getAndIncrement()));
            this.misiones.add(mision);
        }
        return mision;
    }

    @Override
    public Optional<Mision> findById(String id) {
        return misiones.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

}
