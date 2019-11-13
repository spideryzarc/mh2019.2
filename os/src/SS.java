import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * scatter search
 */
public class SS implements Solver {

    int ite;
    int popIniSize;
    int eliteSize;
    double step = 0.005;

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
        score = new double[os.N];
        ArrayList<Sol> pop = new ArrayList<>();
        init(pop);
        ArrayList<Sol> elite = new ArrayList<>();
        selectElite(pop, elite);

        VND vnd = new VND(os);
        for (Sol s : elite) {
            // vnd.run(s);
            System.out.println(s.fo);
        }


        best = elite.get(0);
        System.out.println("SS " + best.fo);

        int worstIdx = 0;
        int max = elite.get(0).fo;
        for (int i = 1; i < elite.size(); i++) {
            if (elite.get(i).fo > max) {
                max = elite.get(i).fo;
                worstIdx = i;
            }
            if (best.fo > elite.get(i).fo) {
                best.copy(elite.get(i));
                System.out.println("SS elite " + best.fo);
            }
        }

        boolean imp;
        do {
            imp = false;
            for (int i = 0, len = elite.size(); i < len; i++) {
                for (int j = 0; j < len; j++)
                    if (i != j) {
//                        for (int z = 0; z < 1000; z++)
                        {
                            //Sol x = pathRelinking(elite.get(i), elite.get(j), vnd);
                            Sol x = randomXCT(elite.get(i), elite.get(j));
                            vnd.run(x);
                            if (best.fo > x.fo) {
                                best.copy(x);
                                System.out.println("SS " + x.fo);
                            }
                            if (!elite.contains(x) && x.fo < max) {
                                elite.set(worstIdx, x);
                                worstIdx = 0;
                                max = elite.get(0).fo;
                                for (int k = 1; k < elite.size(); k++)
                                    if (elite.get(k).fo > max) {
                                        max = elite.get(k).fo;
                                        worstIdx = k;
                                    }
                                imp = true;
                                break;
                            }
                        }
                    }
            }
        } while (imp);

        return 0;
    }

    private double score[];

    private Sol pathRelinking(Sol s1, Sol s2, VND vnd) {
        Sol curr = new Sol(os);
        Sol best = new Sol(os);
        best.fo = Integer.MAX_VALUE;
        for (double alpha = step; alpha < 1; alpha += step) {
            for (int i = 0; i < score.length; i++) {
                score[i] = s1.startTime[i] * alpha + (1 - alpha) * s2.startTime[i];
            }
            Arrays.sort(curr.order, Comparator.comparingDouble(s -> score[s]));

            curr.FO();
            if (curr.fo < s1.fo || curr.fo < s2.fo){
                vnd.run(curr);
                System.out.println(curr.fo);
            }
//            curr.FO();
            if (curr.fo < best.fo) {
                best.copy(curr);
                if (best.fo < s1.fo && best.fo < s2.fo)
                    return best;
            }
        }
        return best;
    }

    private Sol randomXCT(Sol p1, Sol p2) {
        Sol son = new Sol(os);
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
            double d = sol1.startTime[i] - sol.startTime[i];
            s += d * d;
        }
        return Math.sqrt(s);
    }

    private void init(ArrayList<Sol> pop) {
        GRASP grasp = new GRASP(0, 1 + os.N / 300);
        for (int i = 0; i < popIniSize; i++) {
            grasp.run(os);
            pop.add(grasp.getSol());
        }
    }

    @Override
    public Sol getSol() {
        return best;
    }
}
