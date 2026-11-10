package br.infnet.doacoes.model;

import java.time.LocalDate;

/** Unidade catalogada no acervo (UC04). */
public class Equipamento {
    private String codigo;
    private String tipo;
    private String funcionalidade;
    private String marca;
    private EstadoConservacao estadoConservacao;
    private String classificacaoUso;
    private LocalDate dataCatalogacao;
    private StatusEquipamento status;
    private Long categoriaId;
    private String solicitacaoNumero; // preenchido quando reservado (A14)

    public Equipamento(String codigo, String tipo, String funcionalidade, String marca,
                       EstadoConservacao estadoConservacao, String classificacaoUso,
                       LocalDate dataCatalogacao, StatusEquipamento status,
                       Long categoriaId, String solicitacaoNumero) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.funcionalidade = funcionalidade;
        this.marca = marca;
        this.estadoConservacao = estadoConservacao;
        this.classificacaoUso = classificacaoUso;
        this.dataCatalogacao = dataCatalogacao;
        this.status = status;
        this.categoriaId = categoriaId;
        this.solicitacaoNumero = solicitacaoNumero;
    }

    /** RN03: a aprovação apenas reserva o equipamento. */
    public void reservar(String numeroSolicitacao) {
        if (status != StatusEquipamento.DISPONIVEL) {
            throw new IllegalStateException("Equipamento " + codigo + " não está disponível.");
        }
        this.status = StatusEquipamento.RESERVADO;
        this.solicitacaoNumero = numeroSolicitacao;
    }

    /** Devolve o equipamento ao estoque (UC09 - A2). */
    public void liberar() {
        this.status = StatusEquipamento.DISPONIVEL;
        this.solicitacaoNumero = null;
    }

    /** RN03: a baixa ocorre somente no registro da entrega. */
    public void darBaixa() {
        if (status != StatusEquipamento.RESERVADO) {
            throw new IllegalStateException("Só é possível entregar equipamento reservado.");
        }
        this.status = StatusEquipamento.ENTREGUE;
    }

    public boolean isDisponivel() { return status == StatusEquipamento.DISPONIVEL; }

    public String getCodigo() { return codigo; }
    public String getTipo() { return tipo; }
    public String getFuncionalidade() { return funcionalidade; }
    public String getMarca() { return marca; }
    public EstadoConservacao getEstadoConservacao() { return estadoConservacao; }
    public String getClassificacaoUso() { return classificacaoUso; }
    public LocalDate getDataCatalogacao() { return dataCatalogacao; }
    public StatusEquipamento getStatus() { return status; }
    public Long getCategoriaId() { return categoriaId; }
    public String getSolicitacaoNumero() { return solicitacaoNumero; }

    @Override
    public String toString() {
        return String.format("%-8s %-10s %-10s %-9s %-10s", codigo, tipo, marca, estadoConservacao, status);
    }
}
