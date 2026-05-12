package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;

import java.util.List;

public interface DonadorIncentivosRepo {

    void agregarInsignia(String donadorId, Insignia insignia);
    void agregarMision(String donadorId, Mision mision);
    List<Insignia> insigniasDelDonador(String donadorId);
    Mision misionDelDonador(String donadorId);
}