package br.infnet.doacoes.csv;

import br.infnet.doacoes.model.Equipamento;
import br.infnet.doacoes.model.EstadoConservacao;
import br.infnet.doacoes.model.StatusEquipamento;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

public class EquipamentoCsv extends CsvRepository<Equipamento> {
    public EquipamentoCsv(Path arquivo) { super(arquivo); }

    @Override
    protected String[] cabecalho() {
        return new String[]{"codigo", "tipo", "funcionalidade", "marca", "estadoConservacao",
                "classificacaoUso", "dataCatalogacao", "status", "categoriaId", "solicitacaoNumero"};
    }

    @Override
    protected Object[] paraCampos(Equipamento e) {
        return new Object[]{e.getCodigo(), e.getTipo(), e.getFuncionalidade(), e.getMarca(),
                e.getEstadoConservacao(), e.getClassificacaoUso(), e.getDataCatalogacao(),
                e.getStatus(), e.getCategoriaId(), e.getSolicitacaoNumero()};
    }

    @Override
    protected Equipamento deCampos(List<String> f) {
        return new Equipamento(f.get(0), f.get(1), f.get(2), f.get(3),
                EstadoConservacao.valueOf(f.get(4)), f.get(5), LocalDate.parse(f.get(6)),
                StatusEquipamento.valueOf(f.get(7)), Long.parseLong(f.get(8)),
                CsvUtil.vazioComoNulo(f.get(9)));
    }
}
