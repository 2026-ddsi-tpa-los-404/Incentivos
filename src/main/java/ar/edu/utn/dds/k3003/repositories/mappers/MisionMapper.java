package ar.edu.utn.dds.k3003.repositories.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.exceptions.InsigniaNoEncontradaExpection;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.model.misiones.Completitud;
import ar.edu.utn.dds.k3003.model.misiones.DonacionesAscendentes;
import ar.edu.utn.dds.k3003.model.misiones.DonacionesExitosas;
import ar.edu.utn.dds.k3003.model.misiones.RevolucionDonadora;
import ar.edu.utn.dds.k3003.repositories.Jpa.InsigniasRepository;

public class MisionMapper {
    private InsigniasRepository insigniasRepository;

    public MisionMapper(InsigniasRepository insigniasRepository) {
        this.insigniasRepository = insigniasRepository;
    }

    public MisionDTO toDTO(Mision mision) {
        return new MisionDTO(
                mision.getId()!= null ? mision.getId().toString() : null,
                mision.getNombre(),
                mision.getInsignia().getId().toString(),
                mision.getCategoriaDonadorInicio(),
                mision.getCategoriaDonadorFin(),
                mision.getTipoDeMision()
        );
    }

    public Mision toMision(MisionDTO misionDTO) {
        Insignia insignia = insigniasRepository.findById(Long.parseLong(misionDTO.insigniaID()))
                .orElseThrow(() -> new InsigniaNoEncontradaExpection(misionDTO.insigniaID()));

        return switch (misionDTO.tipo()){
            case COMPLETITUD -> new Completitud(misionDTO.nombre(),insignia,misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case DONACIONES_EXITOSAS -> new DonacionesExitosas(misionDTO.nombre(),insignia,misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case REVOLUCION_DONADORA -> new RevolucionDonadora(misionDTO.nombre(),insignia,misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case DONACIONES_ASCENDENTES -> new DonacionesAscendentes(misionDTO.nombre(),insignia,misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo()) ;
        };
    }
}
