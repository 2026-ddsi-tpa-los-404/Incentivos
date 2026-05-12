package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Insignia;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InsigniasMemoryRepo implements InsigniaRepository{
    private List<Insignia> insignias;
    private AtomicLong idSecuencial = new AtomicLong(1);

    public InsigniasMemoryRepo() {
        this.insignias = new ArrayList<>();
    }


    @Override
    public Optional<List<Insignia>> findAll() {
        return Optional.ofNullable(this.insignias);
    }

    @Override
    public Optional<Insignia> findById(String id) {
        return this.insignias.stream().filter(d -> d.getId().equals(id)).findFirst();
    }

    @Override
    public Insignia save(Insignia insignia) {

        if(insignia.getId() != null){
            this.insignias.add(insignia);
        }
        else {
            insignia.setId(String.valueOf(idSecuencial.getAndIncrement()));
            this.insignias.add(insignia);
        }
        return insignia;
    }

    @Override
    public Insignia deleteById(String id) {
        val insigniaEncontrada = this.findById(id)
                .orElseThrow(() -> new RuntimeException("Insignia no encontrada"));
        this.insignias.remove(insigniaEncontrada);
        return insigniaEncontrada;
    }
}