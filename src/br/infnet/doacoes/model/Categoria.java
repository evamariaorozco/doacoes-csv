package br.infnet.doacoes.model;

/** Agrupamento usado para catalogar, filtrar e solicitar equipamentos. */
public class Categoria {
    private Long id;
    private String nome;
    private String descricao;

    public Categoria(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return String.format("Categoria[%d] %s - %s", id, nome, descricao);
    }
}
