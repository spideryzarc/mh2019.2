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
    private int tardAcc[];

    public VND(OS os) {
        this.os = os;
        A = new int[os.M][os.N];
        tard = new int[os.N];
        tardAcc = new int[os.N];
    }

    /**
     * Calcula a função objetivo da solução corrente e atualiza
     * o vetor tardAcc e a matriz A
     */
    protected int FO_update() {
        fill(tard, 0);
        for (int i = 0; i < os.M; i++) {
            for (int j = 0; j < os.N; j++) {
                int k = current.order[j];
                int tmp = os.p[i][k];
                if (j > 0)
                    tmp += A[i][j - 1];
                A[i][j] = tmp;
                if (tmp > os.d[k]) {
                    int r = tmp - os.d[k];
                    if (r > tard[j])
                        tard[j] = r;
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
                int tmp = os.p[i][k];
                if (j > 0)
                    tmp += A[i][j - 1];
                A[i][j] = tmp;
                if (tmp > os.d[k]) {
                    int r = tmp - os.d[k];
                    if (r > tard[j])
                        tard[j] = r;
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
     * Calcula a função objetivo da solução corrente, levando em
     * consideração apenas mundanças ocorridas entre as posições a e b
     */
    int FO(int a, int b) {
        fill(tard, a, b + 1, 0);
        for (int i = 0; i < os.M; i++) {
            int t = (a > 0) ? A[i][a - 1] : 0;
            for (int j = a; j <= b; j++) {
                int k = current.order[j];
                t += os.p[i][k];
                if (t > os.d[k]) {
                    int r = t - os.d[k];
                    if (r > tard[j])
                        tard[j] = r;
                }
            }
        }
        int s = 0;
        for (int j = a; j <= b; j++) {
            s += tard[j];
        }
        int x = tardAcc[b] - ((a > 0) ? tardAcc[a - 1] : 0);

        return FO - x + s;
    }

    public int run(Sol sol) {
        current = sol;
        boolean imp = false;
        FO = FO_update();
        do {
            imp = LS1(sol);
            if (!imp)
                imp = LS2(sol);
            if (!imp)
                imp = LS3(sol);
//            if (!imp)
//                imp = LS4(sol);
//            //...
//            System.out.println(FO);
        } while (imp);
        return sol.FO();
    }


    boolean LS2(Sol sol) {
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = i + 2; j < os.N; j++) {
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
//        System.out.println("--");
        return imp;
    }

//    boolean LS4(Sol sol) {
//
//        for (int i = 0; i < os.N; i++) {
//            for (int j = i + 2; j < os.N; j++) {
//                for (int k = j + 1; k < os.N; k++) {
//                    int aux = sol.order[i];
//                    sol.order[i] = sol.order[j];
//                    sol.order[j] = sol.order[k];
//                    sol.order[k] = aux;
//                    int d = FO(i,k);
//                    if (d < FO) {
//                        FO = FO_update(i,k);
////                        System.out.println("LS4 "+d);
//                        return true;
//                    }
//                    aux = sol.order[k];
//                    sol.order[k] = sol.order[j];
//                    sol.order[j] = sol.order[i];
//                    sol.order[i] = aux;
//
//                    aux = sol.order[k];
//                    sol.order[k] = sol.order[j];
//                    sol.order[j] = sol.order[i];
//                    sol.order[i] = aux;
//                    d = FO(i,k);
//                    if (d < FO) {
//                        FO = FO_update(i,k);
////                        System.out.println("LS4 "+d);
//                        return true;
//                    }
//                    aux = sol.order[i];
//                    sol.order[i] = sol.order[j];
//                    sol.order[j] = sol.order[k];
//                    sol.order[k] = aux;
//                }
//            }
//        }
//        return false;
//    }
}
