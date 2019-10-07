import sun.text.UCompactIntArray;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import static java.util.Arrays.fill;

/**
 * Distribution Estimation Algorithm
 **/
public class DEA2 implements Solver {
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

    public DEA2(int ite, int popSize, double popElite, double alpha) {
        this.ite = ite;
        this.popSize = popSize;
        this.popElite = popElite;
        this.alpha = alpha;
    }


    @Override
    public int run(OS os) {
        acc = new double[os.N];
        score = new double[os.N];

        /**D[k][j] - prob. do pedido k cair na posição j */
        double D[][] = initDist(os);
        double E[][] = new double[os.N][2];

        int eliteSize = (int) Math.round(popSize * popElite);
        PriorityQueue<Sol> elite = new PriorityQueue<>(
                Comparator.comparingInt(s -> -s.fo));

        besSol = new Sol(os);
        int bestFO = besSol.FO();


        for (int i = 0; i < ite; i++) {
            amostra(elite, eliteSize, popSize, D, os);
            for (Sol s : elite)
                if (s.fo < bestFO) {
                    bestFO = s.fo;
                    besSol.copy(s);
                    System.out.println("DEA " + bestFO);
                }

            distMarginal(E, elite);

            for (int j = 0; j < D.length; j++)
                for (int k = 0; k < 2; k++)
                    D[j][k] = D[j][k] * (1 - alpha) + alpha * E[j][k];


        }


        return bestFO;
    }

    private void distMarginal(double[][] e, PriorityQueue<Sol> elite) {
        for (int i = 0; i < e.length; i++)
            fill(e[i], 0);

        for (Sol s : elite)
            for (int k = 0; k < e.length; k++)
                e[k][0] += s.compTime[k];

        for (int k = 0; k < e.length; k++)
            e[k][0] /= elite.size();


        for (Sol s : elite)
            for (int k = 0; k < e.length; k++)
                e[k][1] += Math.pow(s.compTime[k] - e[k][0], 2);

        for (int k = 0; k < e.length; k++) {
            e[k][1] = Math.sqrt(e[k][1] / elite.size());
        }

    }

    private double score[];

    private void amostra(PriorityQueue<Sol> elite, int eliteSize, int popSize, double D[][], OS os) {
        elite.clear();
        for (int i = 0; i < popSize; i++) {
            // gerar nova solução

            Sol sol = null;
            if (elite.size() > eliteSize)
                sol = elite.remove();
            else sol = new Sol(os);

            //amostrar
            for (int k = 0; k < os.N; k++) {
                score[k] = Utils.rd.nextGaussian() * D[k][1] + D[k][0];
            }
            Arrays.sort(sol.order, Comparator.comparingDouble(s -> score[s]));

            sol.fo = sol.FO();
            if (!elite.contains(sol))
                elite.add(sol);
//            else
//                System.out.println("opa");

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
        double D[][] = new double[os.N][2];
        double m = (double) os.getMaxComp() / 2;
        double d = (double) os.getMaxComp() / 4;
        for (int k = 0; k < os.N; k++) {
            D[k][0] = m;
            D[k][1] = d;
        }
        return D;
    }


    @Override
    public Sol getSol() {
        return besSol;
    }
}
