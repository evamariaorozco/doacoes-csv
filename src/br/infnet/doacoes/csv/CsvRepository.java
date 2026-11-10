package br.infnet.doacoes.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Leitura e escrita genérica de arquivos CSV.
 * Cada classe do modelo tem uma subclasse que sabe converter
 * o objeto em linha (paraCampos) e a linha em objeto (deCampos).
 */
public abstract class CsvRepository<T> {
    private final Path arquivo;

    protected CsvRepository(Path arquivo) {
        this.arquivo = arquivo;
    }

    /** Nomes das colunas, gravados na primeira linha do arquivo. */
    protected abstract String[] cabecalho();

    /** Converte o objeto nos valores de cada coluna. */
    protected abstract Object[] paraCampos(T objeto);

    /** Cria o objeto a partir dos campos lidos. */
    protected abstract T deCampos(List<String> campos);

    public List<T> lerTodos() throws IOException {
        List<T> lista = new ArrayList<>();
        if (!Files.exists(arquivo)) return lista;
        try (BufferedReader leitor = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            String linha = leitor.readLine(); // ignora o cabeçalho
            int numeroLinha = 1;
            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;
                if (linha.isBlank()) continue;
                List<String> campos = CsvUtil.separar(linha);
                if (campos.size() != cabecalho().length) {
                    throw new IOException(String.format("%s, linha %d: esperadas %d colunas, encontradas %d",
                            arquivo.getFileName(), numeroLinha, cabecalho().length, campos.size()));
                }
                lista.add(deCampos(campos));
            }
        }
        return lista;
    }

    public void gravarTodos(List<T> objetos) throws IOException {
        if (arquivo.getParent() != null) Files.createDirectories(arquivo.getParent());
        try (BufferedWriter escritor = Files.newBufferedWriter(arquivo, StandardCharsets.UTF_8)) {
            escritor.write(CsvUtil.linha((Object[]) cabecalho()));
            escritor.newLine();
            for (T objeto : objetos) {
                escritor.write(CsvUtil.linha(paraCampos(objeto)));
                escritor.newLine();
            }
        }
    }

    public Path getArquivo() { return arquivo; }
}
