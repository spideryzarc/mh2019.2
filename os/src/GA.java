import java.util.ArrayList;

import static java.util.Arrays.fill;

public class GA implements Solver {
    int ite;
    int popSize;
    int k;
    double mutateRate;
    double crossRate;

    public GA(int ite, int popSize, int k, double mutateRate, double crossRate) {
        this.ite = ite;
        this.popSize = popSize;
        this.k = k;
        this.mutateRate = mutateRate;
        this.crossRate = crossRate;

    }

    private OS os;
    private Sol best;

    @Override
    public int run(OS os) {
        in = new boolean[os.N];
        this.os = os;
        best = new Sol(os);
        int bestCost = best.FO();
        int crossSize = (int) (crossRate * popSize);

        ArrayList<Sol> pop = new ArrayList<>();
        ArrayList<Sol> parents = new ArrayList<>();
        initPop(pop);
        for (int k = 0; k < ite; k++) {

            selection(pop, parents, crossSize);
            for (int i = 0, len = parents.size(); i < len; i++) {
                if (Utils.rd.nextDouble() < mutateRate) {
                    parents.add(mutate(parents.get(i)));
                }
                for (int j = i + 1; j < len; j++) {
//                    parents.add(OX(parents.get(i), parents.get(j)));
//                    parents.add(OX(parents.get(j), parents.get(i)));
                    parents.add(twoPointsX(parents.get(i), parents.get(j)));
                    parents.add(twoPointsX(parents.get(j), parents.get(i)));
                }
            }

            for (Sol s : parents)
                if (s.fo < bestCost) {
                    bestCost = s.fo;
                    best.copy(s);
                    System.out.println("GA " + bestCost);
                }
            parents.addAll(pop);
            selection(parents, pop, popSize);
            parents.add(best);

        }

        return bestCost;
    }

    private boolean in[];

    /**
     * order Crossover
     */
    private Sol OX(Sol p1, Sol p2) {
        Sol son = new Sol(os);
        final int metade = os.N / 2;
        int a = Utils.rd.nextInt(metade);
        int b = a + metade;
        fill(in, false);
        fill(son.order,-1);
        for (int i = a; i < b; i++) {
            son.order[i] = p1.order[i];
            in[p1.order[i]] = true;
        }
        for (int i = 0, p2i = b, si = b, len = os.N - metade; i < len; ) {
            if (!in[p2.order[p2i]]) {
                son.order[si] = p2.order[p2i];
                si++;
                if (si >= os.N)
                    si = 0;
                i++;
            }
            p2i++;
            if (p2i >= os.N)
                p2i = 0;
        }

        son.FO();
        return son;
    }


    private Sol twoPointsX(Sol p1, Sol p2) {
        Sol son = new Sol(os);
        final int metade = os.N / 2;
        int a = Utils.rd.nextInt(metade);
        int b = a + metade;
        fill(in, false);
        fill(son.order,-1);
        int len = os.N;
        for (int i = 0; i < a; i++) {
            son.order[i] = p1.order[i];
            in[p1.order[i]] = true;
            len--;
        }
        for (int i = b; i < os.N; i++) {
            son.order[i] = p1.order[i];
            in[p1.order[i]] = true;
            len--;
        }
        for (int i = 0, p2i = a, si = a; i < len; ) {
            if (!in[p2.order[p2i]]) {
                son.order[si] = p2.order[p2i];
                si++;
                if (si >= os.N)
                    si = 0;
                i++;
            }
            p2i++;
            if (p2i >= os.N)
                p2i = 0;
        }

        son.FO();
        return son;
    }

    private Sol mutate(Sol s) {
        int a = Utils.rd.nextInt(os.N);
        int b = Utils.rd.nextInt(os.N);
        while (a == b)
            b = Utils.rd.nextInt(os.N);
        Sol m = new Sol(os);
        m.copy(s);
        int aux = m.order[a];
        m.order[a] = m.order[b];
        m.order[b] = aux;
        m.FO();
        return m;
    }

    private void selection(ArrayList<Sol> source, ArrayList<Sol> selected, int size) {
        selected.clear();
        for (int i = 0; i < size; i++) {
            Sol c = source.get(Utils.rd.nextInt(source.size()));
            for (int j = 1; j < k; j++) {
                Sol x = source.get(Utils.rd.nextInt(source.size()));
                if (x.fo < c.fo)
                    c = x;
            }
            selected.add(c);
            source.remove(c);
        }

    }

    private void initPop(ArrayList<Sol> pop) {
        pop.clear();
        for (int i = 0; i < popSize; i++) {
            Sol s = new Sol(os);
            Utils.shuffler(s.order);
            s.FO();
            pop.add(s);
        }
    }

    @Override
    public Sol getSol() {
        return best;
    }

}
