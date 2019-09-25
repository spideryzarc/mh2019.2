import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tabu Search
 */
public class TS implements Solver {
    private OS os;
    /**
     * tamanho da lista tabu
     */
    private int tenure;

    private ArrayList<Integer[]> list = new ArrayList<>();

    @Override
    public String toString() {
        return "Tabu{" +
                "ite=" + ite +
                '}';
    }

    /**
     * numero de iterações
     */
    int ite;


    Sol best;

    public TS(int tenure, int ite) {
        this.tenure = tenure;
        this.ite = ite;
    }

    public Sol getSol() {
        return best;
    }

    public int run(OS os) {
        this.os = os;
        best = new Sol(os);

        Sol current = new Sol(os);
        int bestFO = best.FO();
        System.out.println("TS: " + bestFO);
        VND vnd = new VND_tabu(os);
        for (int i = 0; i < ite; i++) {
            int x = vnd.run(current);
            if (x < bestFO) {
                bestFO = x;
                best.copy(current);
                System.out.println("TS: " + bestFO);

                list.clear();
            }

//            vnd.FO_update();
            tabuAdd(vnd.tardAcc);
            perturb(current, vnd);
//            System.out.println(x);

        }
        return bestFO;
    }

    private void tabuAdd(Integer vet[]) {
        if (list.size() >= tenure) {
            Integer[] x = list.remove(0);
            for (int i = 0; i < x.length; i++)
                x[i] = vet[i];
            list.add(x);
        } else {
            list.add(vet.clone());
        }
    }

    private boolean isTabu(Integer vet[]) {
        for (Integer[] s : list) {
            if (Arrays.equals(s, vet))
                return true;
        }
        return false;
    }

    private void perturb(Sol current, VND vnd) {
        do {
            int x = Utils.rd.nextInt(os.N);
            int y = Utils.rd.nextInt(os.N);
            while (x == y)
                y = Utils.rd.nextInt(os.N);
            int aux = current.order[x];
            current.order[x] = current.order[y];
            current.order[y] = aux;
            if (x < y)
                vnd.FO_update(x, y);
            else
                vnd.FO_update(y, x);
        } while (isTabu(vnd.tardAcc));

    }


    class VND_tabu extends VND {

        public VND_tabu(OS os) {
            super(os);
        }

        @Override
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

            } while (imp);
            return FO;
        }

        @Override
        boolean LS1(Sol sol) {
            boolean imp = false;
            for (int i = 0; i < os.N; i++) {
                for (int j = os.N - 1; j > i; j--) {
//                    for (int j = i + 1; j < os.N; j++) {
                    if (tardAcc[i] == tardAcc[j])
                        continue; // otimização para não testar trocas inúteis

                    sol.swap(i, j);
                    int z = FO(i, j);
                    if (z < FO) {
                        FO = FO_update(i, j);
                        if (!isTabu(tardAcc)) {
                            imp = true;
                            continue;
                        }
                        sol.swap(i, j);
                        FO = FO_update(i, j);
                    } else {
                        sol.swap(i, j);
                    }
                }
            }
            return imp;
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
                        FO = FO_update(i, j);
                        if (!isTabu(tardAcc)) {
                            imp = true;
                            continue;
                        }
                        //desfazer
                        aux = sol.order[j];
                        for (int k = j; k > i; k--) {
                            sol.order[k] = sol.order[k - 1];
                        }
                        sol.order[i] = aux;
                        FO = FO_update(i, j);
                    } else {
                        //desfazer
                        aux = sol.order[j];
                        for (int k = j; k > i; k--) {
                            sol.order[k] = sol.order[k - 1];
                        }
                        sol.order[i] = aux;
                    }
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
                        FO = FO_update(j, i);
                        if (!isTabu(tardAcc)) {
                            imp = true;
                            continue;
                        }
                        //desfazer
                        aux = sol.order[j];
                        for (int k = j; k < i; k++) {
                            sol.order[k] = sol.order[k + 1];
                        }
                        sol.order[i] = aux;
                        FO = FO_update(j, i);
                    } else {
                        //desfazer
                        aux = sol.order[j];
                        for (int k = j; k < i; k++) {
                            sol.order[k] = sol.order[k + 1];
                        }
                        sol.order[i] = aux;
                    }

                }

            }
            return imp;
        }

    }
}
