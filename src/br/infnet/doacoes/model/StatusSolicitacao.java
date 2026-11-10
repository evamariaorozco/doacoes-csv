package br.infnet.doacoes.model;

/** Ciclo de vida de uma solicitação (UC06, UC08 e UC09). */
public enum StatusSolicitacao {
    PENDENTE_APROVACAO, APROVADA, APROVADA_PARCIAL, RECUSADA,
    PARCIALMENTE_ENTREGUE, ENCERRADA, CANCELADA
}
