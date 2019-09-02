/**
 * Variable Neighborhood Search
 */
public class VNS {
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




    Sol best;

    public VNS(OS os, int ite, int kwt[][]) {
        this.os = os;
        this.ite = ite;
        this.kwt = kwt;
        best = new Sol(os);
    }

    public Sol getSol() {
        return best;
    }

    public int run() {
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
