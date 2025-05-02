package br.edu.cs.poo.ac.seguro.testes;

import java.io.File;

public class FileUtils {

    private FileUtils() {}

    public static void limparDiretorio(String diretorio) {
        File dir = new File(diretorio);
        if (dir.exists()) { // Só entra aqui se o diretório existir
            File[] files = dir.listFiles(); // Tenta listar os arquivos
            // ERRO OCORRE AQUI: Se listFiles() retornar null, a linha abaixo causa NPE
            for (File file : files) {
                file.delete();
            }
        }
    }
}
