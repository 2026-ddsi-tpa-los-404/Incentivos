package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonadorEIncentivosMemoryRepo implements DonadorIncentivosRepo{

    /*el primer string es el donador, el otro parametro es la lista de sus insignias*/
    private Map<String,List<Insignia>> insignasDeCadaDonador;

    /*el primer string es el donador,  el otro parametro la mision actual que tiene*/
    private Map<String,Mision> misionesDeCadaDonador ;

    public DonadorEIncentivosMemoryRepo() {
        this.insignasDeCadaDonador = new HashMap<>();
        this.misionesDeCadaDonador = new HashMap<>();
    }

    @Override
    public void agregarInsignia(String donadorId, Insignia insignia) {
        /* busco que no se encuentre ya existente*/
        if (!insignasDeCadaDonador.containsKey(donadorId)) {
            insignasDeCadaDonador.put(donadorId, new ArrayList<>());
        }

        /* busco por ID y luego agrego a la lista la insignia*/
        insignasDeCadaDonador.get(donadorId).add(insignia);
    }

    @Override
    public List<Insignia> insigniasDelDonador(String donadorId) {

        return insignasDeCadaDonador.getOrDefault(donadorId, new ArrayList<>());

        /*el getordefault, es en el caso donde la lista este vacia, osea no se haya cargado
        todavia ningun donador , llama a la funcion y devuelve una lista vacia*/
    }


    @Override
    public void agregarMision(String donadorId, Mision  mision) {
        misionesDeCadaDonador.put(donadorId,mision);
    }


    @Override
    public Mision misionDelDonador(String donadorId) {
        return misionesDeCadaDonador.getOrDefault(donadorId,null);
    }
}
