package ar.edu.utn.dds.k3003.repositories.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.model.misiones.Completitud;
import ar.edu.utn.dds.k3003.model.misiones.DonacionesAscendentes;
import ar.edu.utn.dds.k3003.model.misiones.DonacionesExitosas;
import ar.edu.utn.dds.k3003.model.misiones.RevolucionDonadora;

public class MisionMapper {
    public MisionDTO toDTO(Mision mision) {

        return new MisionDTO(
                mision.getId()!= null ? mision.getId().toString() : null,
                mision.getNombre(),
                mision.getInsigniaId(),
                mision.getCategoriaDonadorInicio(),
                mision.getCategoriaDonadorFin(),
                mision.getTipoDeMision()
        );
    }

    public Mision toMision(MisionDTO misionDTO) {
        return switch (misionDTO.tipo()){
            case COMPLETITUD -> new Completitud(misionDTO.nombre(),misionDTO.insigniaID(),misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case DONACIONES_EXITOSAS -> new DonacionesExitosas(misionDTO.nombre(),misionDTO.insigniaID(),misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case REVOLUCION_DONADORA -> new RevolucionDonadora(misionDTO.nombre(),misionDTO.insigniaID(),misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo());
            case DONACIONES_ASCENDENTES -> new DonacionesAscendentes(misionDTO.nombre(),misionDTO.insigniaID(),misionDTO.categoriaFin(),misionDTO.categoriaInicio(),misionDTO.tipo()) ;
        };
    }
}
