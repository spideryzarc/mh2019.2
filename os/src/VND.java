import java.util.Arrays;
import java.util.Comparator;

import static java.util.Arrays.fill;

/**
 * Variable Neighborhood Descendent
 */
public class VND {
    OS os;
    private int FO;
    private Sol current;
    private int A[][];
    private int tard[], tardAcc[];

    public VND(OS os) {
        this.os = os;
        A = new int[os.M][os.N];
        tard = new int[os.N];
        tardAcc = new int[os.N];
    }

    private int FO() {
        fill(tard, 0);
        for (int i = 0; i < os.M; i++) {
            for (int j = 0; j < os.N; j++) {
                int k = current.order[j];
                A[i][j] = os.p[i][k];
                if (j > 0)
                    A[i][j] += A[i][j - 1];
                if (A[i][j] > os.d[k]) {
                    int r = A[i][j] - os.d[k];
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
        return s;
    }

    private int FO(int a, int b) {
        fill(tard, 0);
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
        FO = FO();
        do {
            imp = LS1(sol);
//            if (!imp)
//                imp = LS2(sol);
//            if (!imp)
//                imp = LS3(sol);
//            if (!imp)
//                imp = LS4(sol);
//            //...
//            System.out.println(FO);
        } while (imp);
        return sol.FO();
    }


    private boolean LS2(Sol sol) {
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = i + 2; j < os.N; j++) {
                int aux = sol.order[i];
                for (int k = i; k < j; k++) {
                    sol.order[k] = sol.order[k + 1];
                }
                sol.order[j] = aux;

                int d = sol.FO();
                if (d < FO) {
//                    System.out.println("LS2 " + d);
                    FO = d;
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

            }


        }
        return imp;
    }

    private boolean LS3(Sol sol) {
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = 0; j < i - 1; j++) {
                int aux = sol.order[i];
                for (int k = i; k > j; k--) {
                    sol.order[k] = sol.order[k - 1];
                }
                sol.order[j] = aux;

                int d = sol.FO();
                if (d < FO) {
//                    System.out.println("LS3 " + d);
                    FO = d;
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

    private boolean LS1(Sol sol) {
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = i + 1; j < os.N; j++) {
                sol.swap(i, j);
                int z = FO(i,j);
//                int d = sol.FO();
//                if(z!=d)
//                    System.out.println(z+"  "+d);
                if (z < FO) {
//                    System.out.println("LS1 "+d);

                    FO = FO();//corrigir isso aqui
                    imp = true;
                    continue;
                }
                sol.swap(i, j);
            }
        }
//        System.out.println("--");
        return imp;
    }

    boolean LS4(Sol sol) {

        for (int i = 0; i < os.N; i++) {
            for (int j = i + 2; j < os.N; j++) {
                for (int k = j + 1; k < os.N; k++) {
                    int aux = sol.order[i];
                    sol.order[i] = sol.order[j];
                    sol.order[j] = sol.order[k];
                    sol.order[k] = aux;
                    int d = sol.FO();
                    if (d < FO) {
                        FO = d;
//                        System.out.println("LS4 "+d);
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
                    d = sol.FO();
                    if (d < FO) {
                        FO = d;
//                        System.out.println("LS4 "+d);
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
