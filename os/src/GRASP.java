import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static java.util.Arrays.fill;

/**Random Multi Start*/
public class GRASP {
    OS os;
    int ite;
    Sol best;
    int K;
    public GRASP(OS os, int ite, int K){
        this.os = os;
        this.ite = ite;
        best = new Sol(os);
        this.K = K;
        A = new int[os.M];
        fval = new int[os.N];

    }

    public Sol getSol(){
        return best;
    }

    public int run(){
        Sol current = new Sol(os);
        int bestFO = best.FO();
        VND vnd = new VND(os);
        for (int i = 0; i < ite; i++) {
            randomGreedy1(current);
//            randomGreedyEDD(current);
            //System.out.println("ini "+current.FO());
            int x = current.FO();
//            int x = vnd.run(current);
            if(x < bestFO){
                bestFO = x;
                best.copy(current);
                System.out.println("GRASP: "+bestFO);
            }

        }
        return bestFO;
    }

    private ArrayList<Integer> vet = new ArrayList<>();
    private void randomGreedyEDD(Sol current) {
        vet.clear();
        for (int i = 0; i < os.N; i++)
            vet.add(i);
        vet.sort(Comparator.comparingInt(a->os.d[a]));
        for (int i = 0; i < os.N; i++) {
            int x = Utils.rd.nextInt(Math.min(K,vet.size()));
            current.order[i] = vet.remove(x);
        }

    }

    private int f(int k, int A[]){
        int max = 0;
        for (int i = 0; i < os.M; i++) {
            if(max < A[i]+os.p[i][k]){
                max = A[i]+os.p[i][k];
            }
        }
//        return os.d[k] -  max;
//        return os.d[k]+  max;
        return Math.max(os.d[k],  max);
//        return os.d[k] +  max;
    }

    private int A[], fval[];
    private void randomGreedy1(Sol current) {
        vet.clear();
        for (int i = 0; i < os.N; i++)
            vet.add(i);

        fill(A,0);
        for (int k = 0; k < os.N; k++)
            fval[k] = f(k,A);
        vet.sort(Comparator.comparingInt(k->fval[k]));

        for (int j = 0; j < os.N; j++) {
            int x = Utils.rd.nextInt(Math.min(K,vet.size()));
            current.order[j] = vet.remove(x);


            for (int i = 0; i < os.M; i++)
                A[i]+= os.p[i][current.order[j]];
            for (int k : vet)
                fval[k] = f(k,A);
            vet.sort(Comparator.comparingInt(k->fval[k]));
        }

    }
}
