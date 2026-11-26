package Back_EPS.Ecos.dto;

import jakarta.validation.constraints.*;

public class CadastroDTO {
    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres.")
    private String nome;

    @NotBlank(message = "A matrícula é obrigatória.")
    @Pattern(regexp = "^[0-9]{6,15}$", message = "A matrícula deve conter apenas números (6 a 15 dígitos).")
    private String matricula;

    @NotBlank(message = "Selecione um campus.")
    private String campus;

    @NotBlank(message = "Selecione o vínculo com o IFPB.")
    @Pattern(regexp = "Discente|Docente|Servidor", message = "Vínculo inválido.")
    private String perfil;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres.")
    private String senha;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }
    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

