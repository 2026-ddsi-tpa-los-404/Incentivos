package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.model.*;

public class MisionMapper {
    public MisionDTO toDTO(Mision mision) {

        return new MisionDTO(
                mision.getId(),
                mision.getNombre(),
                mision.getInsigniaId(),
                mision.getCategoriaDonadorInicio(),
                mision.getCategoriaDonadorFin(),
                mision.getTipoDeMision()
        );
    }

    public Mision toMision(MisionDTO misionDTO) {
        return switch (misionDTO.tipo()){
            case COMPLETITUD -> new Completitud(misionDTO.id(),misionDTO.nombre(),misionDTO.insigniaID(),misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case DONACIONES_EXITOSAS -> new DonacionesExitosas(misionDTO.id(),misionDTO.nombre(),misionDTO.insigniaID(),misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case REVOLUCION_DONADORA -> new RevolucionDonadora(misionDTO.id(),misionDTO.nombre(),misionDTO.insigniaID(),misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case DONACIONES_ASCENDENTES -> new DonacionesAscendentes(misionDTO.id(),misionDTO.nombre(),misionDTO.insigniaID(),misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo()) ;
        };
    }
}
