import java.util.Comparator;
import java.util.PriorityQueue;

import static java.util.Arrays.fill;

/**
 * Distribution Estimation Algorithm
 **/
public class DEA implements Solver {
    /**
     * número de iterações
     */
    int ite;
    /**
     * tamanho da populaçao
     */
    int popSize;
    /**
     * percentual de seleção
     */
    double popElite;
    /**
     * taxa de aprendizado
     */
    double alpha;

    Sol besSol;

    public DEA(int ite, int popSize, double popElite, double alpha) {
        this.ite = ite;
        this.popSize = popSize;
        this.popElite = popElite;
        this.alpha = alpha;
    }


    @Override
    public int run(OS os) {
        acc = new double[os.N];

        /**D[k][j] - prob. do pedido k cair na posição j */
        double D[][] = initDist(os);
        double E[][] = new double[os.N][os.N];

        int eliteSize = (int) Math.round(popSize * popElite);
        PriorityQueue<Sol> elite = new PriorityQueue<>(
                Comparator.comparingInt(s -> -s.fo));

        besSol = new Sol(os);
        int bestFO = besSol.FO();


        for (int i = 0; i < ite; i++) {
            amostra(elite, eliteSize, popSize, D, os);
            for(Sol s : elite)
                if(s.fo < bestFO){
                    bestFO = s.fo;
                    besSol.copy(s);
                    System.out.println("DEA "+bestFO);
                }

            distMarginal(E, elite);

            for (int j = 0; j < D.length; j++)
                for (int k = 0; k < D.length; k++)
                    D[j][k] = D[j][k] * (1 - alpha) + alpha * E[j][k];



        }


        return bestFO;
    }

    private void distMarginal(double[][] e, PriorityQueue<Sol> elite) {
        for (int i = 0; i < e.length; i++)
            fill(e[i], 0);
        double p = 1.0 / elite.size();
        for (Sol s : elite) {
            for (int i = 0; i < e.length; i++) {
                e[s.order[i]][i] += p;
            }
        }
    }

    private void amostra(PriorityQueue<Sol> elite, int eliteSize, int popSize, double D[][], OS os) {
        elite.clear();
        for (int i = 0; i < popSize; i++) {
            // gerar nova solução
            Sol sol = new Sol(os);
            fill(sol.order, -1);
            for (int k = 0; k < os.N; k++) {
                int x = roleta(D[k], sol.order);
                if (sol.order[x] != -1)
                    System.err.println("EPAAAAAAA roleta deu errado!!!");
                sol.order[x] = k;
            }
            sol.fo = sol.FO();
            elite.add(sol);
            if (elite.size() > eliteSize)
                elite.remove();
        }
    }

    private double acc[];

    private int roleta(double[] p, Integer[] order) {
        acc[0] = (order[0] == -1) ? p[0] : 0;
        for (int i = 1; i < acc.length; i++) {
            acc[i] = acc[i - 1] + ((order[i] == -1) ? p[i] : 0);
        }
        double x = Utils.rd.nextDouble() * acc[acc.length - 1];
        for (int i = 0; i < acc.length; i++) {
            if (acc[i] > x)
                return i;
        }
        return acc.length - 1;
    }

    private double[][] initDist(OS os) {
        double D[][] = new double[os.N][os.N];
        double p = 1.0 / os.N;
        for (int i = 0; i < os.N; i++) {
            fill(D[i], p);
        }
        return D;
    }


    @Override
    public Sol getSol() {
        return besSol;
    }
}
