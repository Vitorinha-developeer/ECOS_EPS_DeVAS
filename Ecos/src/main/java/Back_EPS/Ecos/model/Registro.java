package Back_EPS.Ecos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "registros")
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String campus;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    private LocalDateTime dataEnvio = LocalDateTime.now();
    private String situacao = "Recebida";

    private LocalDate dataOcorrido;
    private String categoria;
    private String local;
    private String adicionais;

    private String atendimento;
    private LocalDateTime dataPretendida;
    private String solicitante;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public LocalDate getDataOcorrido() { return dataOcorrido; }
    public void setDataOcorrido(LocalDate dataOcorrido) { this.dataOcorrido = dataOcorrido; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }
    public String getAdicionais() { return adicionais; }
    public void setAdicionais(String adicionais) { this.adicionais = adicionais; }
    public String getAtendimento() { return atendimento; }
    public void setAtendimento(String atendimento) { this.atendimento = atendimento; }
    public LocalDateTime getDataPretendida() { return dataPretendida; }
    public void setDataPretendida(LocalDateTime dataPretendida) { this.dataPretendida = dataPretendida; }
    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
