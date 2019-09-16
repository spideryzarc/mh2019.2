import static java.util.Arrays.fill;

/**
 * Guided Local Search
 */
public class GLS implements Solver {
    private OS os;

    /**
     * matriz de penalidades
     */
    private int P[][];

    @Override
    public String toString() {
        return "GLS{" +
                "ite=" + ite +
                '}';
    }

    /**
     * numero de iterações
     */
    int ite;

    Sol best;
    int bestFO;

    public GLS(int ite) {
        this.ite = ite;
    }

    public Sol getSol() {
        return best;
    }

    public int run(OS os) {

        this.os = os;
        best = new Sol(os);
        P = new int[os.N][os.N];

        Sol current = new Sol(os);
        bestFO = best.FO();
        System.out.println("GLS: " + bestFO);
        VND vnd = new VND_P(os);
        for (int i = 0; i < ite; i++) {
            penalize(current.order);
            vnd.run(current);
        }
        return bestFO;
    }

    /**
     * adicionas penalidades à matriz P correspondentes à solução corrente
     */
    private void penalize(Integer[] order) {
        for (int i = 0; i < os.N; i++)
            P[order[i]][i]++;
    }


    /**
     * Versão alterado do VND para assumir a matriz de penalidades como guia da
     * descida
     */
    class VND_P extends VND {

        public VND_P(OS os) {
            super(os);
        }

        /**
         * Função de avaliação da solução corrente (FO + penalidades)
         */
        int FA;

        @Override
        public int run(Sol sol) {
            current = sol;
            boolean imp = false;
            FO = FO_update();
            FA = FO + penalties();
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
                for (int j = i + 1; j < os.N; j++) {
                    sol.swap(i, j);

                    int foij = FO(i, j);
                    if (foij < bestFO) {
                        updateBest(foij);
                        System.out.println("GLS LS1 " + bestFO);
                    }

                    int z = foij + penalties();
                    if (z < FA) {
                        FA = z;
                        FO = FO_update(i, j);
                        // System.out.println("LS1 P FO:" + FO + " FA:" + FA);
                        imp = true;
                        continue;
                    }

                    sol.swap(i, j);
                }
            }
            return imp;
        }

        private void updateBest(int foij) {
            best.copy(current);
            bestFO = foij;
            for (int i = 0; i < os.N; i++) {
                fill(P[i], 0);
            }
        }

        @Override
        boolean LS2(Sol sol) {
            boolean imp = false;
            for (int i = 0; i < os.N; i++) {
                for (int j = i + 2; j < os.N; j++) {
                    int aux = sol.order[i];
                    for (int k = i; k < j; k++) {
                        sol.order[k] = sol.order[k + 1];
                    }
                    sol.order[j] = aux;

                    int foij = FO(i, j);
                    if (foij < bestFO) {
                        updateBest(foij);
                        System.out.println("GLS LS2 " + bestFO);
                    }

                    int d = foij + penalties();
                    if (d < FA) {
                        //System.out.println("LS2 " + d);
                        FA = d;
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

        @Override
        boolean LS3(Sol sol) {
            boolean imp = false;
            for (int i = 0; i < os.N; i++) {
                for (int j = 0; j < i - 1; j++) {
                    int aux = sol.order[i];
                    for (int k = i; k > j; k--) {
                        sol.order[k] = sol.order[k - 1];
                    }
                    sol.order[j] = aux;

                    int foij = FO(j, i);
                    if (foij < bestFO) {
                        updateBest(foij);
                        System.out.println("GLS LS3 " + bestFO);
                    }


                    int d = foij + penalties();
                    if (d < FA) {
                        FA = d;
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

        private int penalties() {
            int p = 0;
            for (int i = 0; i < os.N; i++)
                if (Utils.rd.nextBoolean()) {
                    p += P[current.order[i]][i];
                }
            return p;
        }
    }

}
