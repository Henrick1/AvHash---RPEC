import java.util.Random;

public class Hashes {
    public static int comparation;

    public static int hashingDivisao(int chave, int tamanhoTabela) {
        return chave % tamanhoTabela;
    }

    public static int hashingMultiplicacao(int chave, int tamanhoTabela) {
        double A = 0.6180339887;
        double produto = A * chave;
        double fracao = produto - (int) produto;
        return (int) (tamanhoTabela * fracao);
    }

    public static int hashingDobramento(int chave, int tamanhoTabela) {
        int soma = 0;
        while (chave > 0) {
            soma += chave % 1000;
            chave /= 1000;
        }
        return soma % tamanhoTabela;
    }

    public static Registro busca(int chave, Registro[] tabelaHash, int tamanhoTabela, int idFunc) {
        int indice = 0;
        if (idFunc == 1) {
            indice = hashingDivisao(chave, tamanhoTabela);
        } else if (idFunc == 2) {
            indice = hashingMultiplicacao(chave, tamanhoTabela);
        } else if (indice == 3) {
            indice = hashingDobramento(chave, tamanhoTabela);
        }

        Registro atual = tabelaHash[indice];
        while (atual != null) {
            comparation++;
            if (atual.getChave() == chave) {
                return atual; // Encontrou a chave na tabela hash
            }
            atual = atual.getProximo();
        }

        return null; // Chave não encontrada na tabela hash
    }

    public static void main(String[] args) {
        int[] tamanhosTabela = {10, 100, 1000, 10000, 100000};
        int[] conjuntoDeDados = {20000, 100000, 500000, 1000000};

        for (int tamanhoTabela : tamanhosTabela) {
            for (int tamanhoConjunto : conjuntoDeDados) {
                for (int seed = 1; seed <= 5; seed++) {
                    System.out.println("Seed: " + seed);
                    Random random = new Random(seed);

                    Registro[] tabelaHash = new Registro[tamanhoTabela];

                    // O conjunto de dados
                    int[] conjuntoDados = new int[tamanhoConjunto];
                    for (int i = 0; i < tamanhoConjunto; i++) {
                        conjuntoDados[i] = random.nextInt(900000000) + 100000000;
                    }

                    // Insira cada elemento na tabela hash usando a função de hash escolhida
                    for (int escolhaFuncao = 1; escolhaFuncao <= 3; escolhaFuncao++) {
                        int colisoes = 0;
                        String nomeFuncao = (escolhaFuncao == 1) ? "Divisao" : (escolhaFuncao == 2) ? "Multiplicacao" : "Dobramento";
                        System.out.println("\nFuncao de hash: " + nomeFuncao);

                        long startTime = System.currentTimeMillis();

                        // Inserimos os elementos na tabela hash
                        for (int elemento : conjuntoDados) {
                            // Calcula o índice usando a função de hash escolhida
                            int indice;
                            if (escolhaFuncao == 1) {
                                indice = hashingDivisao(elemento, tamanhoTabela);
                            } else if (escolhaFuncao == 2) {
                                indice = hashingMultiplicacao(elemento, tamanhoTabela);
                            } else {
                                indice = hashingDobramento(elemento, tamanhoTabela);
                            }
                            // Cria um novo registro com o elemento
                            Registro novoRegistro = new Registro(elemento);

                            // Verifica se a posição na tabela está vazia
                            if (tabelaHash[indice] == null) {
                                tabelaHash[indice] = novoRegistro;
                            } else {
                                // Caso contrário, há uma colisão
                                colisoes++;
                                Registro atual = tabelaHash[indice];
                                // Percorre a hashtable para encontrar a posição correta
                                while (atual.getProximo() != null && novoRegistro.getChave() > atual.getProximo().getChave()) {
                                    atual = atual.getProximo();
                                }
                                // Insere o novo registro na posição correta
                                novoRegistro.setProximo(atual.getProximo());
                                atual.setProximo(novoRegistro);
                            }
                        }


                        long endTime = System.currentTimeMillis();
                        long tempoInsercao = endTime - startTime;

                        // Aqui mostramos os tempos e o numero de colisões
                        System.out.println("Tamanho da tabela: " + tamanhoTabela);
                        System.out.println("Tamanho do conjunto de dados: " + tamanhoConjunto);
                        System.out.println("Tempo de inserção (milisegundos): " + tempoInsercao);
                        System.out.println("Número de colisões: " + colisoes);
                        System.out.println();

                        // Aqui realizamos as buscas
                        long totalTimeDivisao = 0;
                        long totalTimeMultiplicacao = 0;
                        long totalTimeDobramento = 0;
                        int totalComparisonsDivisao = 0;
                        int totalComparisonsMultiplicacao = 0;
                        int totalComparisonsDobramento = 0;

                        for (int i = 0; i < 5; i++) {
                            int chaveDesejada = conjuntoDados[i];

                            // Busca com função de hash de Divisão
                            comparation = 0;
                            long startTimeDivisao = System.nanoTime();
                            Registro resultadoDivisao = busca(chaveDesejada, tabelaHash, tamanhoTabela, 1);
                            long endTimeDivisao = System.nanoTime();
                            totalTimeDivisao += endTimeDivisao - startTimeDivisao;
                            totalComparisonsDivisao += comparation;

                            // Busca com função de hash de Multiplicação
                            comparation = 0;
                            long startTimeMultiplicacao = System.nanoTime();
                            Registro resultadoMultiplicacao = busca(chaveDesejada, tabelaHash, tamanhoTabela, 2);
                            long endTimeMultiplicacao = System.nanoTime();
                            totalTimeMultiplicacao += endTimeMultiplicacao - startTimeMultiplicacao;
                            totalComparisonsMultiplicacao += comparation;

                            // Busca com função de hash de Dobramento
                            comparation = 0;
                            long startTimeDobramento = System.nanoTime();
                            Registro resultadoDobramento = busca(chaveDesejada, tabelaHash, tamanhoTabela, 3);
                            long endTimeDobramento = System.nanoTime();
                            totalTimeDobramento += endTimeDobramento - startTimeDobramento;
                            totalComparisonsDobramento += comparation;
                        }

                        long averageTimeDivisao = totalTimeDivisao / 5;
                        long averageTimeMultiplicacao = totalTimeMultiplicacao / 5;
                        long averageTimeDobramento = totalTimeDobramento / 5;

                        System.out.println("Média de tempo para a busca (Divisão): " + averageTimeDivisao);
                        System.out.println("Comparações feitas (Divisão): " + totalComparisonsDivisao);
                        System.out.println("Média de tempo para a busca (Multiplicação): " + averageTimeMultiplicacao);
                        System.out.println("Comparações feitas (Multiplicação): " + totalComparisonsMultiplicacao);
                        System.out.println("Média de tempo para a busca (Dobramento): " + averageTimeDobramento);
                        System.out.println("Comparações feitas (Dobramento): " + totalComparisonsDobramento);
                        System.out.println();
                        System.out.println();

                    }
                }
            }
        }
    }
}