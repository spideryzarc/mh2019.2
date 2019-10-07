import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Simulated Annealing
 */
public class SA implements Solver {

    /**
     * probabilidade de se ter um pioramento de B na temperatura inicial
     */
    double probInicial = .001;
    /**
     * probabilidade de se ter um pioramento de B na temperatura final
     */
    double probFinal = .001;
    /**
     * pioramento permitido com probabilidade A
     */
    double pioramento = .01;

    /**
     * número de iterações
     */
    int ite;

    public SA(double probInicial, double probFinal, double pioramento, int ite) {
        this.probInicial = probInicial;
        this.probFinal = probFinal;
        this.pioramento = pioramento;
        this.ite = ite;
    }

    @Override
    public String toString() {
        return "SA{" +
                "A=" + probInicial +
                ", B=" + pioramento +
                ", ite=" + ite +
                '}';
    }

    Sol bestSol;

    OS os;

    @Override
    public int run(OS os) {
        this.os = os;
        bestSol = new Sol(os);

//        VND vnd = new VND(os);
//        vnd.run(bestSol);

        Sol current = new Sol(os);
        current.copy(bestSol);
        Sol pertubed = new Sol(os);

        int bestFO = bestSol.FO();
        int currentFO = bestSol.FO();

        double Tini = bestFO * pioramento / Math.log(1 / probInicial);
        double Tfin = bestFO * pioramento / Math.log(1 / probFinal);

        System.out.println("Tini " + Tini);
        System.out.println("Tfin " + Tfin);
        int pertPerIte = os.N * os.N / 2;
        double lambda = Math.pow(Tfin / Tini, 1.0 / ite);
        System.out.println("lambda " + lambda);
        double T = Tini;

        for (int i = 0; i < ite; i++) {
            for (int j = 0; j < pertPerIte; j++) {
                pertube(current, pertubed);
                int pFO = pertubed.FO();
                int delta = pFO - currentFO;
                if (delta < 0) {
                    current.copy(pertubed);
                    currentFO = pFO;
                    if (pFO < bestFO) {
                        bestSol.copy(pertubed);
                        bestFO = pFO;
                        System.out.println(T + " SA: " + pFO);
                    }
//                    System.out.println(T+"   "+pFO);
                } else if (Utils.rd.nextDouble() < P(delta, T)) {
                    current.copy(pertubed);
                    currentFO = pFO;
//                    if(delta!=0)
//                        System.out.println(T+" * "+pFO);
                }
            }
            T *= lambda;
            //System.out.println("T "+T);
        }
        VND vnd = new VND(os);
        bestFO = vnd.run(bestSol);
        return bestFO;
    }

    private double P(int delta, double t) {
        return 1 / Math.exp(delta / t);
    }

    private void pertube(Sol current, Sol currentp) {
        int a = Utils.rd.nextInt(os.N);
        int b = Utils.rd.nextInt(os.N);
        while (b == a)
            b = Utils.rd.nextInt(os.N);
        for (int i = 0; i < os.N; i++)
            currentp.order[i] = current.order[i];
        currentp.order[a] = current.order[b];
        currentp.order[b] = current.order[a];
    }

    @Override
    public Sol getSol() {
        return null;
    }
}
