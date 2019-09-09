/**
 * Variable Neighborhood Descendent
 */
public class VND_old {
    OS os;
    private int FO;

    public VND_old(OS os) {
        this.os = os;
    }

    public int run(Sol sol) {
        boolean imp = false;
        FO = sol.FO();
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
                int d = sol.FO();
                if (d < FO) {
//                    System.out.println("LS1 "+d);
                    FO = d;
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
