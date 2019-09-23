import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tabu Search
 */
public class TS implements Solver {
    private OS os;
    /**
     * tamanho da lista tabu
     */
    private int tenure;

    private ArrayList<Integer[]> list = new ArrayList<>();

    @Override
    public String toString() {
        return "Tabu{" +
                "ite=" + ite +
                '}';
    }

    /**
     * numero de iterações
     */
    int ite;


    Sol best;

    public TS(int tenure, int ite) {
        this.tenure = tenure;
        this.ite = ite;
    }

    public Sol getSol() {
        return best;
    }

    public int run(OS os) {
        this.os = os;
        best = new Sol(os);

        Sol current = new Sol(os);
        int bestFO = best.FO();
        System.out.println("TS: " + bestFO);
        VND vnd = new VND(os);
        for (int i = 0; i < ite; i++) {
            int x = vnd.run(current);
            tabuAdd(current);
            perturb(current);
            if (x < bestFO) {
                bestFO = x;
                best.copy(current);
                System.out.println("TS: " + bestFO);
            }

        }
        return bestFO;
    }

    private void tabuAdd(Sol current) {
        if (list.size() >= tenure) {
            Integer[] x = list.remove(0);
            for (int i = 0; i < x.length; i++)
                x[i] = current.order[i];
            list.add(x);
        } else {
            list.add(current.order.clone());
        }
    }

    private boolean isTabu(Sol current){
        for (Integer[] s : list){
            if(Arrays.equals(s,current.order))
                return true;
        }
        return false;
    }
    private void perturb(Sol current) {
        do {
            int x = Utils.rd.nextInt(os.N);
            int y = Utils.rd.nextInt(os.N);
            while (x == y)
                y = Utils.rd.nextInt(os.N);
            int aux = current.order[x];
            current.order[x] = current.order[y];
            current.order[y] = aux;
        }while (isTabu(current));

    }
}
