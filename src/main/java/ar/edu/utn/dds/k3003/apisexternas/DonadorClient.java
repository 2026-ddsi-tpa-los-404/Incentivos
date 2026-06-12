package ar.edu.utn.dds.k3003.apisexternas;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "donadores", url = "${url.donadores}")

public interface DonadorClient {
    @GetMapping("/donadores/{id}")
    DonadorDTO buscarPorID(@PathVariable String id);

    @PatchMapping("/donadores/{id}/categoria")
    DonadorDTO modifcarCategoria(@PathVariable String id, @RequestBody Map<String, String> body);
}
