package Back_EPS.Ecos.controller;

import Back_EPS.Ecos.service.EcosService;
import Back_EPS.Ecos.dto.*;
import Back_EPS.Ecos.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final EcosService service;

    public AuthController(EcosService service) {
        this.service = service;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@Valid @RequestBody CadastroDTO dto) {
        service.cadastrar(dto);
        return ResponseEntity.ok(Map.of("mensagem", "Conta criada com sucesso!"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        Usuario u = service.login(dto);
        return ResponseEntity.ok(Map.of(
                "nome", u.getNome(),
                "matricula", u.getMatricula(),
                "campus", u.getCampus(),
                "perfil", u.getPerfil()
        ));
    }
}
