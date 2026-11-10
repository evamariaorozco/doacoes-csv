package br.infnet.doacoes.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * Funções de apoio para o formato CSV usado no projeto:
 * separador ';' (padrão do Excel em português) e campos com ';', aspas
 * ou quebra de linha protegidos entre aspas duplas.
 */
public final class CsvUtil {
    public static final char SEPARADOR = ';';

    private CsvUtil() { }

    /** Converte um valor em campo CSV, escapando quando necessário. */
    public static String escapar(Object valor) {
        if (valor == null) return "";
        String s = valor.toString();
        boolean precisaAspas = s.indexOf(SEPARADOR) >= 0 || s.contains("\"") || s.contains("\n");
        if (precisaAspas) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    /** Monta uma linha CSV a partir dos valores. */
    public static String linha(Object... valores) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) sb.append(SEPARADOR);
            sb.append(escapar(valores[i]));
        }
        return sb.toString();
    }

    /** Separa uma linha CSV em campos, respeitando campos entre aspas. */
    public static List<String> separar(String linha) {
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean entreAspas = false;
        for (int i = 0; i < linha.length(); i++) {
            char c = linha.charAt(i);
            if (entreAspas) {
                if (c == '"' && i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                    atual.append('"');
                    i++;
                } else if (c == '"') {
                    entreAspas = false;
                } else {
                    atual.append(c);
                }
            } else if (c == '"') {
                entreAspas = true;
            } else if (c == SEPARADOR) {
                campos.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(c);
            }
        }
        campos.add(atual.toString());
        return campos;
    }

    /** Retorna null para campo vazio, para tratar valores opcionais. */
    public static String vazioComoNulo(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
