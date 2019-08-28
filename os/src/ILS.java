/**
 * Iterated Local Search
 */
public class ILS {
    OS os;
    /**
     * numero de iterações
     */
    int ite;
    /**
     * quantidade de pares trocados por perturbação
     */
    int K;
    /**
     * maior distância entre elementos de uma troca
     */
    int W;

    Sol best;

    public ILS(OS os, int ite, int k, int w) {
        this.os = os;
        this.ite = ite;
        this.K = k;
        this.W = w;
        best = new Sol(os);
    }

    public Sol getSol() {
        return best;
    }

    public int run() {
        Sol current = new Sol(os);
        int bestFO = best.FO();
        System.out.println("ILS: " + bestFO);
        VND vnd = new VND(os);
        for (int i = 0; i < ite; i++) {
            perturb(current.order);
            int x = vnd.run(current);
            if (x < bestFO) {
                bestFO = x;
                best.copy(current);
                System.out.println("ILS: " + bestFO);
            }

        }
        return bestFO;
    }

    private void perturb(int[] order) {
        for (int i = 0; i < K; i++) {
            int x = Utils.rd.nextInt(os.N);
            int y = x + Utils.rd.nextInt(2 * W) - W;
            if (y >= os.N)
                y = os.N - 1;
            else if (y < 0)
                y = 0;
            int aux = order[x];
            order[x] = order[y];
            order[y] = aux;

        }
    }
}
