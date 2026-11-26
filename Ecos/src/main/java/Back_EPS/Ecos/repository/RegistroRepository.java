package Back_EPS.Ecos.repository;

import Back_EPS.Ecos.model.Registro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroRepository extends JpaRepository<Registro, String> {
    List<Registro> findByUsuarioMatriculaIgnoreCaseOrderByDataEnvioDesc(String matricula);
}

