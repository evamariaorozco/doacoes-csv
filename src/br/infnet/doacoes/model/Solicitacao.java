package br.infnet.doacoes.model;

import java.time.LocalDateTime;

/** Pedido de equipamentos feito por um solicitante (UC06 e UC08). */
public class Solicitacao {
    private String numero;
    private Long solicitanteId;
    private Long categoriaId;
    private LocalDateTime dataSolicitacao;
    private int quantidadeSolicitada;
    private int quantidadeAprovada;
    private String justificativa;
    private StatusSolicitacao status;
    private LocalDateTime dataDecisao;
    private String motivoDecisao;

    public Solicitacao(String numero, Long solicitanteId, Long categoriaId, LocalDateTime dataSolicitacao,
                       int quantidadeSolicitada, int quantidadeAprovada, String justificativa,
                       StatusSolicitacao status, LocalDateTime dataDecisao, String motivoDecisao) {
        if (quantidadeSolicitada < 1) {
            throw new IllegalArgumentException("A quantidade solicitada deve ser no mínimo 1.");
        }
        this.numero = numero;
        this.solicitanteId = solicitanteId;
        this.categoriaId = categoriaId;
        this.dataSolicitacao = dataSolicitacao;
        this.quantidadeSolicitada = quantidadeSolicitada;
        this.quantidadeAprovada = quantidadeAprovada;
        this.justificativa = justificativa;
        this.status = status;
        this.dataDecisao = dataDecisao;
        this.motivoDecisao = motivoDecisao;
    }

    /** UC08: aprovação total ou parcial (A2). */
    public void aprovar(int quantidade, String motivo) {
        if (status != StatusSolicitacao.PENDENTE_APROVACAO) {
            throw new IllegalStateException("Solicitação " + numero + " não está pendente.");
        }
        this.quantidadeAprovada = quantidade;
        this.status = quantidade < quantidadeSolicitada
                ? StatusSolicitacao.APROVADA_PARCIAL : StatusSolicitacao.APROVADA;
        this.motivoDecisao = motivo;
        this.dataDecisao = LocalDateTime.now().withNano(0);
    }

    /** UC08 - A1: recusa com motivo obrigatório. */
    public void recusar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("O motivo da recusa é obrigatório.");
        }
        this.status = StatusSolicitacao.RECUSADA;
        this.motivoDecisao = motivo;
        this.dataDecisao = LocalDateTime.now().withNano(0);
    }

    public String getNumero() { return numero; }
    public Long getSolicitanteId() { return solicitanteId; }
    public Long getCategoriaId() { return categoriaId; }
    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public int getQuantidadeSolicitada() { return quantidadeSolicitada; }
    public int getQuantidadeAprovada() { return quantidadeAprovada; }
    public String getJustificativa() { return justificativa; }
    public StatusSolicitacao getStatus() { return status; }
    public LocalDateTime getDataDecisao() { return dataDecisao; }
    public String getMotivoDecisao() { return motivoDecisao; }

    @Override
    public String toString() {
        return String.format("%-9s categoria=%d qtd=%d aprovada=%d status=%s",
                numero, categoriaId, quantidadeSolicitada, quantidadeAprovada, status);
    }
}
