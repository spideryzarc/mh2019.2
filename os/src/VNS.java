import java.util.Arrays;

/**
 * Variable Neighborhood Search
 */
public class VNS implements Solver {
    OS os;
    /**
     * numero de iterações
     */
    int ite;

    /**
     * quantidade de pares trocados por perturbação
     */
    private int K;
    /**
     * maior distância entre elementos de uma troca
     */
    private int W;


    /**pares de valores kw  e número de tentativas de cada vizinhança */
    private int kwt[][];

    @Override
    public String toString() {
        return "VNS{" +
                "ite=" + ite +
                ", kwt=" + Arrays.deepToString(kwt) +
                '}';
    }

    Sol best;

    public VNS(int ite, int kwt[][]) {
        this.ite = ite;
        this.kwt = kwt;
    }

    public Sol getSol() {
        return best;
    }

    public int run(OS os) {
        best = new Sol(os);
        this.os = os;

        Sol current = new Sol(os);
        int bestFO = best.FO();
        System.out.println("VNS: " + bestFO);
        VND vnd = new VND(os);
        int v = 0;
        K = kwt[v][0];
        W = kwt[v][1];
        int tries = kwt[v][2];
        for (int i = 0; i < ite; i++) {
            tries--;
            if(tries == 0){
                v++;
                if(v == kwt.length)
                    v--;
                K = kwt[v][0];
                W = kwt[v][1];
                tries = kwt[v][2];
            }
            perturb(current.order);
            int x = vnd.run(current);
            if (x < bestFO) {
                bestFO = x;
                best.copy(current);
                System.out.println("VNS: "+v+ " - " + bestFO);
                v = 0;
                K = kwt[v][0];
                W = kwt[v][1];
                tries = kwt[v][2];
            }else{
                current.copy(best);
            }

        }
        return bestFO;
    }

    private void perturb(Integer[] order) {
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
