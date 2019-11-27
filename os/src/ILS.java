/**
 * Iterated Local Search
 */
public class ILS implements Solver {
    private OS os;

    @Override
    public String toString() {
        return "ILS{" +
                "ite=" + ite +
                ", K=" + K +
                ", W=" + W +
                '}';
    }

    /**
     * numero de iterações
     */
    int ite;
    /**
     * quantidade de pares trocados por perturbação
     */
    double K;
    /**
     * maior distância entre elementos de uma troca
     */
    double W;

    Sol best;

    public ILS(int ite, double k, double w) {
        this.ite = ite;
        this.K = k;
        this.W = w;

    }

    public Sol getSol() {
        return best;
    }

    public int run(OS os) {
        this.os = os;
        best = new Sol(os);
        Sol current = new Sol(os);
        int bestFO = Integer.MAX_VALUE;
        VND vnd = new VND(os);
        for (int i = 0; i < ite; i++) {
            perturb(current.order);
            int x = vnd.run(current);
            if (x < bestFO) {
                bestFO = x;
                best.copy(current);
                System.out.println(i+" ILS: " + bestFO);
            }

        }
        return bestFO;
    }

    private final void perturb(Integer[] order) {
        int ik = (int)(os.N*K);
        int iw = (int)(os.N*W);
        for (int i = 0; i < ik; i++) {
            int x = Utils.rd.nextInt(os.N);
            int y = x + Utils.rd.nextInt(2 * iw) - iw;
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
