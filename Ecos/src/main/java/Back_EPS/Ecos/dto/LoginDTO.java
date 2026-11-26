package Back_EPS.Ecos.dto;


import jakarta.validation.constraints.NotBlank;

public class LoginDTO {
    @NotBlank(message = "Informe a matrícula.")
    private String matricula;

    @NotBlank(message = "Informe a senha.")
    private String senha;

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

