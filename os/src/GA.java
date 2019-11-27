import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static java.util.Arrays.fill;

/**
 * Genetic Algorithm
 */
public class GA implements Solver {
    /**
     * número de iterações / eras
     */
    int ite;
    /**
     * tamanho da população
     */
    int popSize;
    /**
     * número de indivíduos por torneio
     */
    int k;
    /**
     * percentual da população que se tornará pai
     */
    double crossRate;
    /**
     * percentual dos pais que soferá mutação
     */
    double mutateRate;

    ArrayList<Sol> pool = new ArrayList<>();

    public GA(int ite, int popSize, int k, double mutateRate, double crossRate) {
        this.ite = ite;
        this.popSize = popSize;
        this.k = k;
        this.mutateRate = mutateRate;
        this.crossRate = crossRate;

    }

    private OS os;
    private Sol best;

    private Sol newSol() {
        if (pool.isEmpty()) {
            return new Sol(os);
        } else
            return pool.remove(pool.size() - 1);
    }

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
        VND vnd = new VND(os);
        for (int k = 0; k < ite; k++) {
            Sol f;
            select(pop, parents, crossSize);
            for (int i = 0, len = parents.size(); i < len; i++) {

                if (Utils.rd.nextDouble() < mutateRate) {

                    f = mutate(parents.get(i));
//                    vnd.run(f);
                    if (!parents.contains(f) && !pop.contains(f))
                        parents.add(f);
                }
                for (int j = i + 1; j < len; j++) {

//                    f = OX(parents.get(i), parents.get(j));
//                    if(!parents.contains(f) && !pop.contains(f))
//                        parents.add(f);
//
//                    f = OX(parents.get(j), parents.get(i));
//                    if(!parents.contains(f) && !pop.contains(f))
//                        parents.add(f);

//                    f = twoPointsX(parents.get(i), parents.get(j));
//                    if(!parents.contains(f) && !pop.contains(f))
//                        parents.add(f);
////
//                    f = twoPointsX(parents.get(j), parents.get(i));
//                    if(!parents.contains(f) && !pop.contains(f))
//                        parents.add(f);

                    f = randomX(parents.get(i), parents.get(j),.5);
                    if(!parents.contains(f) && !pop.contains(f))
                        parents.add(f);

//                    f = randomX(parents.get(j), parents.get(i),.5);
//                    if(!parents.contains(f) && !pop.contains(f))
//                        parents.add(f);

                    f = randomXCT(parents.get(i), parents.get(j));
                    if (!parents.contains(f) && !pop.contains(f))
                        parents.add(f);


                }
            }

            parents.addAll(pop);
            Collections.sort(parents, Comparator.comparingInt(a -> a.fo));

            if (parents.get(0).fo < bestCost) {
                bestCost = parents.get(0).fo;
                best.copy(parents.get(0));
                System.out.println(k+" GA " + bestCost);
            }
            pop.clear();
            pop.addAll(parents.subList(0, popSize));
            pool.addAll(parents.subList(popSize, parents.size()));
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
        fill(son.order, -1);
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
        fill(son.order, -1);
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
//        if(son.fo < best.fo)
//            System.out.println("eba");
        return son;
    }

    private Sol randomX(Sol p1, Sol p2, double x) {
        Sol son = newSol();
        fill(in, false);
        fill(son.order, -1);
        int len = os.N;
        for (int i = 0; i < os.N; i++) {
            if (Utils.rd.nextDouble() < x) {
                son.order[i] = p1.order[i];
                in[p1.order[i]] = true;
                len--;
            }
        }

        for (int i = 0, j = 0, k = 0; i < len; i++) {
            while (son.order[j] != -1) j++;
            while (in[p2.order[k]]) k++;
            son.order[j++] = p2.order[k++];
        }

        son.FO();
//        if(son.fo < best.fo)
//            System.out.println("eba");
        return son;
    }


    private Sol randomXCT(Sol p1, Sol p2) {
        Sol son = newSol();
        for (int k = 0; k < os.N; k++) {
            if (Utils.rd.nextBoolean())
                son.startTime[k] = p1.startTime[k];
            else
                son.startTime[k] = p2.startTime[k];
        }
        Arrays.sort(son.order, Comparator.comparingInt(s -> son.startTime[s]));

        son.FO();
        return son;

    }

    private Sol mutate(Sol s) {
        Sol m = newSol();
        m.copy(s);
        for (int i = 0; i < 1; i++) {
            int a = Utils.rd.nextInt(os.N);
            int b = Utils.rd.nextInt(os.N);
            while (a == b)
                b = Utils.rd.nextInt(os.N);
            int aux = m.order[a];
            m.order[a] = m.order[b];
            m.order[b] = aux;
        }
        m.FO();
        return m;
    }

    /**
     * remove , segundo torneio, n indivíduos da coleção source
     * e os adiciona na coleção selected.
     */
    private void select(ArrayList<Sol> source, ArrayList<Sol> selected, int n) {
        selected.clear();
        for (int i = 0; i < n; i++) {
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
//        VND vd = new VND(os);
        for (int i = 0; i < popSize; i++) {
            Sol s = new Sol(os);
            Utils.shuffler(s.order);
//            if(Utils.rd.nextBoolean())
//                vd.runLS1(s);
            s.FO();
            System.out.println(i+" init GA "+s.fo);
            pop.add(s);
        }
    }

    @Override
    public Sol getSol() {
        return best;
    }

}
