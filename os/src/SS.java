import java.util.ArrayList;

/**
 * scatter search
 */
public class SS implements Solver {

    int ite;
    int popIniSize;
    int eliteSize;

    Sol best;
    OS os;

    public SS(int ite, int popIniSize, int eliteSize) {
        this.ite = ite;
        this.popIniSize = popIniSize;
        this.eliteSize = eliteSize;

    }

    @Override
    public int run(OS os) {
        this.os = os;
        ArrayList<Sol> pop = new ArrayList<>();
        init(pop);
        ArrayList<Sol> elite = new ArrayList<>();
        selectElite(pop, elite);

        return 0;
    }

    private void selectElite(ArrayList<Sol> pop, ArrayList<Sol> elite) {
        int pivot = 0;
        int bestCost = pop.get(0).fo;
        for (int i = 1; i < pop.size(); i++) {
            if (pop.get(i).fo < bestCost) {
                pivot = i;
                bestCost = pop.get(i).fo;
            }
        }

        elite.add(pop.get(pivot));
        double d[] = new double[pop.size()];
        boolean in[] = new boolean[pop.size()];
        in[pivot] = true;
        for (int i = 0; i < pop.size(); i++) {
            d[i] = dist(pop.get(pivot), pop.get(i));
        }

        for (int k = 1; k < eliteSize; k++) {
            pivot = -1;
            double max = 0;
            for (int i = 0; i < os.N; i++)
                if (!in[i] && max < d[i]) {
                    pivot = i;
                    max = d[i];
                }
            elite.add(pop.get(pivot));
            in[pivot] = true;

            for (int i = 0; i < pop.size(); i++)
                if (!in[i]) {
                    d[i] = Math.min(dist(pop.get(pivot), pop.get(i)), d[i]);
                }

        }

    }

    private double dist(Sol sol, Sol sol1) {
        double s = 0;
        for (int i = 0; i < os.N; i++) {
            double d = sol1.compTime[i] - sol.compTime[i];
            s += d * d;
        }
        return Math.sqrt(s);
    }

    private void init(ArrayList<Sol> pop) {
        GRASP grasp = new GRASP(0, os.N / 10);
        for (int i = 0; i < popIniSize; i++) {
            grasp.run(os);
            pop.add(grasp.getSol());
        }
    }

    @Override
    public Sol getSol() {
        return null;
    }
}
