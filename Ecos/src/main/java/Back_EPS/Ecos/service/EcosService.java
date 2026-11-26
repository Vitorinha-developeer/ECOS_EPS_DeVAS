package Back_EPS.Ecos.service;

import Back_EPS.Ecos.dto.*;
import Back_EPS.Ecos.model.*;
import Back_EPS.Ecos.repository.*;
import Back_EPS.Ecos.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EcosService {

    private final UsuarioRepository usuarioRepo;
    private final RegistroRepository registroRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public EcosService(UsuarioRepository usuarioRepo, RegistroRepository registroRepo) {
        this.usuarioRepo = usuarioRepo;
        this.registroRepo = registroRepo;
    }

    private static final List<String> CAMPI_VALIDOS = List.of(
            "Areia", "Cabedelo", "Cajazeiras", "Campina Grande", "Catolé do Rocha",
            "Esperança", "Guarabira", "Itabaiana", "Itaporanga", "João Pessoa",
            "Mangabeira", "Monteiro", "Patos", "Pedras de Fogo", "Picuí",
            "Princesa Isabel", "Santa Luzia", "Santa Rita", "Soledade", "Sousa"
    );

    private void validarCampus(String campus) {
        if (!CAMPI_VALIDOS.contains(campus)) {
            throw new IllegalArgumentException("Campus selecionado é inválido.");
        }
    }

    public Usuario cadastrar(CadastroDTO dto) {
        validarCampus(dto.getCampus());
        if (usuarioRepo.existsByMatriculaIgnoreCase(dto.getMatricula().trim())) {
            throw new IllegalArgumentException("Já existe uma conta cadastrada com essa matrícula.");
        }

        Usuario u = new Usuario();
        u.setNome(dto.getNome().trim());
        u.setMatricula(dto.getMatricula().trim());
        u.setCampus(dto.getCampus());
        u.setPerfil(dto.getPerfil());
        u.setSenhaHash(encoder.encode(dto.getSenha()));
        return usuarioRepo.save(u);
    }

    public Usuario login(LoginDTO dto) {
        Usuario u = usuarioRepo.findByMatriculaIgnoreCase(dto.getMatricula().trim())
                .orElseThrow(() -> new IllegalArgumentException("Matrícula não encontrada."));

        if (!encoder.matches(dto.getSenha(), u.getSenhaHash())) {
            throw new IllegalArgumentException("Senha incorreta.");
        }
        return u;
    }

    public Registro salvarDenuncia(DenunciaDTO dto) {
        validarCampus(dto.getCampus());
        Usuario u = usuarioRepo.findByMatriculaIgnoreCase(dto.getMatricula().trim())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Registro r = new Registro();
        r.setTipo("Denúncia");
        r.setUsuario(u);
        r.setDataOcorrido(dto.getDataOcorrido());
        r.setCategoria(dto.getCategoria());
        r.setCampus(dto.getCampus());
        r.setLocal(dto.getLocal().trim());
        r.setDescricao(dto.getDescricao().trim());
        r.setAdicionais(dto.getAdicionais() != null ? dto.getAdicionais().trim() : null);
        return registroRepo.save(r);
    }

    public Registro salvarAtendimento(AtendimentoDTO dto) {
        validarCampus(dto.getCampus());
        Usuario u = usuarioRepo.findByMatriculaIgnoreCase(dto.getMatricula().trim())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado."));

        Registro r = new Registro();
        r.setTipo("Solicitação de atendimento");
        r.setUsuario(u);
        r.setCampus(dto.getCampus());
        r.setAtendimento(dto.getAtendimento());
        r.setDataPretendida(dto.getDataPretendida());
        r.setSolicitante(dto.getSolicitante().trim());
        r.setDescricao(dto.getDescricao().trim());
        return registroRepo.save(r);
    }

    public List<Registro> listarPorMatricula(String matricula) {
        return registroRepo.findByUsuarioMatriculaIgnoreCaseOrderByDataEnvioDesc(matricula.trim());
    }
}
