package br.infnet.doacoes;

import br.infnet.doacoes.csv.CategoriaCsv;
import br.infnet.doacoes.csv.EquipamentoCsv;
import br.infnet.doacoes.csv.SolicitacaoCsv;
import br.infnet.doacoes.model.*;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Demonstração da leitura e escrita dos arquivos CSV do
 * Sistema de Gestão de Doação de Equipamentos Eletrônicos.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Path pasta = Path.of(args.length > 0 ? args[0] : "dados");
        CategoriaCsv categoriaCsv = new CategoriaCsv(pasta.resolve("categorias.csv"));
        EquipamentoCsv equipamentoCsv = new EquipamentoCsv(pasta.resolve("equipamentos.csv"));
        SolicitacaoCsv solicitacaoCsv = new SolicitacaoCsv(pasta.resolve("solicitacoes.csv"));

        // 1. Leitura
        List<Categoria> categorias = categoriaCsv.lerTodos();
        List<Equipamento> equipamentos = equipamentoCsv.lerTodos();
        List<Solicitacao> solicitacoes = solicitacaoCsv.lerTodos();
        titulo("1. Leitura dos arquivos CSV");
        System.out.printf("%d categorias, %d equipamentos e %d solicitações carregados.%n",
                categorias.size(), equipamentos.size(), solicitacoes.size());
        categorias.forEach(c -> System.out.println("  " + c));

        // 2. Consulta ao estoque disponível (UC05 / RN02)
        titulo("2. Estoque disponível (UC05 - somente status DISPONIVEL)");
        listarDisponiveis(equipamentos);

        // 3. Catalogação de um novo equipamento (UC04)
        titulo("3. Catalogação de novo equipamento (UC04)");
        String codigo = proximoCodigo(equipamentos);
        Equipamento novo = new Equipamento(codigo, "Teclado", "Entrada de dados", "Logitech",
                EstadoConservacao.BOM, "Estudo", LocalDate.now(), StatusEquipamento.DISPONIVEL, 1L, null);
        equipamentos.add(novo);
        System.out.println("  Catalogado: " + novo);

        // 4. Análise de solicitação pendente com reserva (UC08 / RN03)
        titulo("4. Análise de solicitação pendente (UC08)");
        for (Solicitacao s : solicitacoes) {
            if (s.getStatus() != StatusSolicitacao.PENDENTE_APROVACAO) continue;
            List<Equipamento> livres = equipamentos.stream()
                    .filter(Equipamento::isDisponivel)
                    .filter(e -> e.getCategoriaId().equals(s.getCategoriaId()))
                    .collect(Collectors.toList());
            int qtd = Math.min(livres.size(), s.getQuantidadeSolicitada());
            if (qtd == 0) {
                s.recusar("Sem equipamentos disponíveis na categoria.");
            } else {
                s.aprovar(qtd, qtd < s.getQuantidadeSolicitada() ? "Estoque insuficiente" : null);
                for (int i = 0; i < qtd; i++) livres.get(i).reservar(s.getNumero());
            }
            System.out.println("  " + s);
        }

        // 5. Gravação
        titulo("5. Gravação dos arquivos CSV");
        categoriaCsv.gravarTodos(categorias);
        equipamentoCsv.gravarTodos(equipamentos);
        solicitacaoCsv.gravarTodos(solicitacoes);
        System.out.println("  Arquivos atualizados em: " + pasta.toAbsolutePath());

        titulo("6. Estoque disponível após a análise");
        listarDisponiveis(equipamentoCsv.lerTodos());
    }

    private static void listarDisponiveis(List<Equipamento> equipamentos) {
        System.out.printf("  %-8s %-10s %-10s %-9s %-10s%n", "CODIGO", "TIPO", "MARCA", "ESTADO", "STATUS");
        equipamentos.stream().filter(Equipamento::isDisponivel)
                .forEach(e -> System.out.println("  " + e));
    }

    private static String proximoCodigo(List<Equipamento> equipamentos) {
        int maior = equipamentos.stream()
                .mapToInt(e -> Integer.parseInt(e.getCodigo().substring(3)))
                .max().orElse(0);
        return String.format("EQ-%04d", maior + 1);
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("== " + texto + " ==");
    }
}
