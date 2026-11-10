package br.infnet.doacoes.csv;

import br.infnet.doacoes.model.Solicitacao;
import br.infnet.doacoes.model.StatusSolicitacao;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public class SolicitacaoCsv extends CsvRepository<Solicitacao> {
    public SolicitacaoCsv(Path arquivo) { super(arquivo); }

    @Override
    protected String[] cabecalho() {
        return new String[]{"numero", "solicitanteId", "categoriaId", "dataSolicitacao",
                "quantidadeSolicitada", "quantidadeAprovada", "justificativa", "status",
                "dataDecisao", "motivoDecisao"};
    }

    @Override
    protected Object[] paraCampos(Solicitacao s) {
        return new Object[]{s.getNumero(), s.getSolicitanteId(), s.getCategoriaId(),
                s.getDataSolicitacao(), s.getQuantidadeSolicitada(), s.getQuantidadeAprovada(),
                s.getJustificativa(), s.getStatus(), s.getDataDecisao(), s.getMotivoDecisao()};
    }

    @Override
    protected Solicitacao deCampos(List<String> f) {
        String dataDecisao = CsvUtil.vazioComoNulo(f.get(8));
        return new Solicitacao(f.get(0), Long.parseLong(f.get(1)), Long.parseLong(f.get(2)),
                LocalDateTime.parse(f.get(3)), Integer.parseInt(f.get(4)), Integer.parseInt(f.get(5)),
                f.get(6), StatusSolicitacao.valueOf(f.get(7)),
                dataDecisao == null ? null : LocalDateTime.parse(dataDecisao),
                CsvUtil.vazioComoNulo(f.get(9)));
    }
}
