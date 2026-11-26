package Back_EPS.Ecos.repository;

import Back_EPS.Ecos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByMatriculaIgnoreCase(String matricula);
    boolean existsByMatriculaIgnoreCase(String matricula);
}
