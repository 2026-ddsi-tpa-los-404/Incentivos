package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.model.Insignia;

public class InsigniaMapper {
    public Insignia toInsignia(InsigniaDTO insigniaDTO){
        return new Insignia(insigniaDTO.id(), insigniaDTO.nombre(), insigniaDTO.descripcion());
    }

    public InsigniaDTO toInsigniaDTO(Insignia insignia){
        return new InsigniaDTO(insignia.getId(),insignia.getNombre(), insignia.getDescripcion());
    }
}
