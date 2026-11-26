package Back_EPS.Ecos.controller;

import Back_EPS.Ecos.dto.*;
import Back_EPS.Ecos.model.Registro;
import Back_EPS.Ecos.service.EcosService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registros")
public class RegistroController {

    private final EcosService service;

    public RegistroController(EcosService service) {
        this.service = service;
    }

    @PostMapping("/denuncia")
    public ResponseEntity<Registro> criarDenuncia(@Valid @RequestBody DenunciaDTO dto) {
        return ResponseEntity.ok(service.salvarDenuncia(dto));
    }

    @PostMapping("/atendimento")
    public ResponseEntity<Registro> criarAtendimento(@Valid @RequestBody AtendimentoDTO dto) {
        return ResponseEntity.ok(service.salvarAtendimento(dto));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<List<Registro>> listar(@PathVariable String matricula) {
        return ResponseEntity.ok(service.listarPorMatricula(matricula));
    }
}

