package ar.edu.utn.dds.k3003.repositories.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.model.Insignia;

public class InsigniaMapper {
    public Insignia toInsignia(InsigniaDTO insigniaDTO){
        Insignia insignia = new Insignia(insigniaDTO.nombre(), insigniaDTO.descripcion());
        if (insigniaDTO.id() != null) {
            insignia.setId(Long.parseLong(insigniaDTO.id()));
        }
        return insignia;
    }

    public InsigniaDTO toInsigniaDTO(Insignia insignia){
        return new InsigniaDTO(insignia.getId() !=  null ? insignia.getId().toString() : null,
                insignia.getNombre(),
                insignia.getDescripcion());
    }
}
