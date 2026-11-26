package Back_EPS.Ecos.dto;


import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class DenunciaDTO {
    @NotBlank(message = "Matrícula do usuário é obrigatória.")
    private String matricula;

    @NotNull(message = "A data do ocorrido é obrigatória.")
    @PastOrPresent(message = "A data do ocorrido não pode ser no futuro.")
    private LocalDate dataOcorrido;

    @NotBlank(message = "Selecione o tipo de ocorrência.")
    private String categoria;

    @NotBlank(message = "Selecione o campus.")
    private String campus;

    @NotBlank(message = "Informe o local específico.")
    private String local;

    @NotBlank(message = "A descrição do ocorrido é obrigatória.")
    @Size(min = 10, message = "Descreva o ocorrido com pelo menos 10 caracteres.")
    private String descricao;

    private String adicionais;

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public LocalDate getDataOcorrido() { return dataOcorrido; }
    public void setDataOcorrido(LocalDate dataOcorrido) { this.dataOcorrido = dataOcorrido; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getAdicionais() { return adicionais; }
    public void setAdicionais(String adicionais) { this.adicionais = adicionais; }
}

