import java.util.Arrays;
import java.util.Comparator;

import static java.util.Arrays.fill;

/**
 * Variable Neighborhood Descendent
 */
public class VND {
    private OS os;
    /**
     * valor da função objetivo da solução corrente
     */
    protected int FO;
    /**
     * solução corrente
     */
    protected Sol current;
    /**
     * A[i][j] - tempo de processamento acumulado na máquina i até a posição j na solução corrente
     */
    private int A[][];
    /**
     * vetor auxiliar para calculo do retardo
     */
    private int tard[];
    /**
     * tardAcc[j] - retardo acumulado até a posição j na solução corrente
     */
    protected Integer tardAcc[];

    /**
     * tempo de completude do pedido k
     */
    protected Integer compTime[];


    private Integer[] macIdx;
    private int[] score;

    public VND(OS os) {
        this.os = os;
        A = new int[os.M][os.N];
        tard = new int[os.N];
        tardAcc = new Integer[os.N];
        compTime = new Integer[os.N];


        score = new int[os.M];
        macIdx = new Integer[os.M];
        for (int i = 0; i < os.M; i++) {
            macIdx[i] = i;
        }


    }

    /**
     * Calcula a função objetivo da solução corrente e atualiza
     * o vetor tardAcc e a matriz A
     */
    protected int FO_update() {
        fill(tard, 0);
        fill(compTime, 0);
        for (int i = 0; i < os.M; i++) {
            for (int j = 0; j < os.N; j++) {
                int k = current.order[j];
                int tmp = os.p[i][k];
                if (j > 0)
                    tmp += A[i][j - 1];
                A[i][j] = tmp;
                if (tmp > os.d[k]) {
                    int r = tmp - os.d[k];
                    if (r > tard[j]) {
                        tard[j] = r;
                    }
                }
                if (compTime[k] < tmp) {
                    compTime[k] = tmp;
                }
            }
        }
        int s = 0;
        for (int j = 0; j < os.N; j++) {
            s += tard[j];
            tardAcc[j] = s;
        }



        return FO = s;
    }

    private void sortMachines() {
        fill(score, 0);
        for (int j = 0; j < os.N; j++) {
            int max = A[0][j], maxi = 0;
            for (int i = 1; i < os.M; i++) {
                if (max < A[i][j]) {
                    max = A[i][j];
                    maxi = i;
                }
            }
            score[maxi]++;
        }
        Arrays.sort(macIdx, Comparator.comparingInt(x->-score[x]));
    }

    /**
     * Calcula a função objetivo da solução corrente e atualiza
     * o vetor tardAcc e a matriz A, considerando alterações entre
     * as posições a e b
     */
    protected int FO_update(int a, int b) {

        fill(tard, a, b + 1, 0);

        for (int i = 0; i < os.M; i++) {
            for (int j = a; j <= b; j++) {
                int k = current.order[j];
                if (i == 0)
                    compTime[k] = 0;
                int tmp = os.p[i][k];
                if (j > 0)
                    tmp += A[i][j - 1];
                A[i][j] = tmp;
                if (tmp > os.d[k]) {
                    int r = tmp - os.d[k];
                    if (r > tard[j])
                        tard[j] = r;
                }
                if (compTime[k] < tmp) {
                    compTime[k] = tmp;
                }
            }
        }
        if (a > 0)
            tard[0] = tardAcc[0];
        for (int j = 1; j < a; j++)
            tard[j] = tardAcc[j] - tardAcc[j - 1];
        for (int j = b + 1; j < os.N; j++)
            tard[j] = tardAcc[j] - tardAcc[j - 1];
        int s = 0;
        for (int j = 0; j < os.N; j++) {
            s += tard[j];
            tardAcc[j] = s;
        }
        return FO = s;
    }


    /**
     * Calcula um limite inferior para a função objetivo da solução corrente,
     * levando em consideração apenas mundanças ocorridas entre as posições a e b
     */
    final int FO(int a, int b) {
        int x = tardAcc[b] - ((a > 0) ? tardAcc[a - 1] : 0);
        int s = 0;
        fill(tard, a, b + 1, 0);
        for (int i : macIdx) {
            int t = (a > 0) ? A[i][a - 1] : 0;
            for (int j = a; j <= b; j++) {
                int k = current.order[j];
                t += os.p[i][k];
                if (t > os.d[k]) {
                    int r = t - os.d[k];
                    if (r > tard[j]) {
                        s += r - tard[j];
                        tard[j] = r;
                        if (s > x) {
                            //System.out.println("m "+i);
                            return FO - x + s;
                        }
                    }
                }
            }
        }

//        for (int j = a; j <= b; j++) {
//            s += tard[j];
//        }


        return FO - x + s;
    }

//    final int FO(int a, int b) {
//        int x = tardAcc[b] - ((a > 0) ? tardAcc[a - 1] : 0);
//        fill(tard, a, b + 1, 0);
//        for (int i = 0; i < os.M; i++) {
//            int t = (a > 0) ? A[i][a - 1] : 0;
//            for (int j = a; j <= b; j++) {
//                int k = current.order[j];
//                t += os.p[i][k];
//                if (t > os.d[k]) {
//                    int r = t - os.d[k];
//                    if (r > tard[j])
//                        tard[j] = r;
//                }
//            }
//        }
//        int s = 0;
//        for (int j = a; j <= b; j++) {
//            s += tard[j];
//        }
//
//
//        return FO - x + s;
//    }

    public int run(Sol sol) {
        current = sol;
        boolean imp = false;
        FO = FO_update();
        do {
            sortMachines();

            imp = LS1(sol);
            if (!imp)
                imp = LS2(sol);
            if (!imp)
                imp = LS3(sol);
        } while (imp);
        return sol.FO();
    }

    public int runLS1(Sol sol) {
        current = sol;
        boolean imp = false;
        FO = FO_update();
        while (LS1(sol)) ;
        return FO;
    }


    boolean LS2(Sol sol) {
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = i + 2; j < os.N; j++) {
                if (tardAcc[i] == tardAcc[j])
                    continue; // otimização para não testar trocas inúteis

                int aux = sol.order[i];
                for (int k = i; k < j; k++) {
                    sol.order[k] = sol.order[k + 1];
                }
                sol.order[j] = aux;
                int d = FO(i, j);
                if (d < FO) {
                    //System.out.println("LS2 " + d);
                    FO = FO_update(i, j);
                    imp = true;
                    continue;
//                    return true;
                }
                //desfazer
                aux = sol.order[j];
                for (int k = j; k > i; k--) {
                    sol.order[k] = sol.order[k - 1];
                }
                sol.order[i] = aux;

//                if(sol.FO() != FO)
//                    System.out.println("EPA!!!!!!!!!!!!!!!!!!!!!!");

            }


        }
        return imp;
    }

    boolean LS3(Sol sol) {
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = 0; j < i - 1; j++) {
                if (tardAcc[i] == tardAcc[j])
                    continue; // otimização para não testar trocas inúteis

                int aux = sol.order[i];
                for (int k = i; k > j; k--) {
                    sol.order[k] = sol.order[k - 1];
                }
                sol.order[j] = aux;

                int d = FO(j, i);
                if (d < FO) {
//                    System.out.println("LS3 " + d);
                    FO = FO_update(j, i);
                    imp = true;
                    continue;
                }
                //desfazer
                aux = sol.order[j];
                for (int k = j; k < i; k++) {
                    sol.order[k] = sol.order[k + 1];
                }
                sol.order[i] = aux;

            }

        }
        return imp;
    }

    boolean LS1(Sol sol) {
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = i + 1; j < os.N; j++) {
                if (tardAcc[i] == tardAcc[j])
                    continue; // otimização para não testar trocas inúteis
                sol.swap(i, j);
                int z = FO(i, j);
                if (z < FO) {
                    FO = FO_update(i, j);
//                    System.out.println(FO+" "+z+" "+current.FO());
//                    System.out.println("LS1 "+FO+" "+i+" "+j);
                    imp = true;
                    continue;
                }
                sol.swap(i, j);
            }
        }
        return imp;
    }

    /**
     * variação do custo da troca de i por j , desconsiderando o que ocorre
     * entre i e j, para i > j
     */
//    private final int deltaSwap(int i, int j) {
//        int pedidoEmI = current.order[i];
//        int pedidoEmJ = current.order[j];
//        int tardi = (i > 0) ? tardAcc[i] - tardAcc[i - 1] : tardAcc[i]; // tard do pedido na posição i
//        int novoTardi = (compTime[pedidoEmJ] > os.d[pedidoEmI]) ? compTime[pedidoEmJ] - os.d[pedidoEmI] : 0;
//        int tardj = tardAcc[j] - tardAcc[j - 1]; // tard do pedido na posição i
//        int compTimeIm1 = (i > 0) ? compTime[current.order[i - 1]] : 0;
//        int novoTardj = (compTimeIm1 + os.minP[pedidoEmJ] > os.d[pedidoEmJ]) ? compTimeIm1 + os.minP[pedidoEmJ] - os.d[pedidoEmJ] : 0; // valor mínimo, pode ser calculado com maior precisão em O(M)
//
//        return novoTardi - tardi + novoTardj - tardj;
//
//    }

    boolean LS4(Sol sol) {

        for (int i = 0; i < os.N; i++) {
            if (tardAcc[i] == 0)
                continue; // otimização para não testar trocas inúteis
            for (int j = i + 1; j < os.N; j++) {
                if (tardAcc[i] == tardAcc[j])
                    continue; // otimização para não testar trocas inúteis
                for (int k = j + 1; k < os.N; k++) {
                    if (tardAcc[j] == tardAcc[k])
                        continue; // otimização para não testar trocas inúteis
                    int aux = sol.order[i];
                    sol.order[i] = sol.order[j];
                    sol.order[j] = sol.order[k];
                    sol.order[k] = aux;
                    int d = FO(i, k);
                    if (d < FO) {
                        FO = FO_update(i, k);
                        System.out.println("LS4 " + d);
                        return true;
                    }
                    aux = sol.order[k];
                    sol.order[k] = sol.order[j];
                    sol.order[j] = sol.order[i];
                    sol.order[i] = aux;

                    aux = sol.order[k];
                    sol.order[k] = sol.order[j];
                    sol.order[j] = sol.order[i];
                    sol.order[i] = aux;
                    d = FO(i, k);
                    if (d < FO) {
                        FO = FO_update(i, k);
                        System.out.println("LS4 " + d);
                        return true;
                    }
                    aux = sol.order[i];
                    sol.order[i] = sol.order[j];
                    sol.order[j] = sol.order[k];
                    sol.order[k] = aux;
                }
            }
        }
        return false;
    }
}
