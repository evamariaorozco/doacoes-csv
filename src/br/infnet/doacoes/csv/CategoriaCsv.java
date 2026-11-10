package br.infnet.doacoes.csv;

import br.infnet.doacoes.model.Categoria;

import java.nio.file.Path;
import java.util.List;

public class CategoriaCsv extends CsvRepository<Categoria> {
    public CategoriaCsv(Path arquivo) { super(arquivo); }

    @Override
    protected String[] cabecalho() {
        return new String[]{"id", "nome", "descricao"};
    }

    @Override
    protected Object[] paraCampos(Categoria c) {
        return new Object[]{c.getId(), c.getNome(), c.getDescricao()};
    }

    @Override
    protected Categoria deCampos(List<String> f) {
        return new Categoria(Long.parseLong(f.get(0)), f.get(1), f.get(2));
    }
}
