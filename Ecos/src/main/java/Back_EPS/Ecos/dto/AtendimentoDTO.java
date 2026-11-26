package Back_EPS.Ecos.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class AtendimentoDTO {
    @NotBlank(message = "Matrícula do usuário é obrigatória.")
    private String matricula;

    @NotBlank(message = "Selecione o campus.")
    private String campus;

    @NotBlank(message = "Selecione o atendimento desejado.")
    private String atendimento;

    @NotNull(message = "Informe a data e horário pretendidos.")
    @Future(message = "A data pretendida deve ser no futuro.")
    private LocalDateTime dataPretendida;

    @NotBlank(message = "O nome do solicitante é obrigatório.")
    private String solicitante;

    @NotBlank(message = "Informe como podemos ajudar.")
    @Size(min = 10, message = "Detalhe sua solicitação com pelo menos 10 caracteres.")
    private String descricao;

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }
    public String getAtendimento() { return atendimento; }
    public void setAtendimento(String atendimento) { this.atendimento = atendimento; }
    public LocalDateTime getDataPretendida() { return dataPretendida; }
    public void setDataPretendida(LocalDateTime dataPretendida) { this.dataPretendida = dataPretendida; }
    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}

