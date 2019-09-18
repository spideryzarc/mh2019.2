/**Simulated Annealing*/
public class SA implements Solver{

    double A = .01;
    double B = .10;

    int ite;

    public SA(int ite) {
        this.ite = ite;
    }

    Sol bestSol;

    OS os;

    @Override
    public int run(OS os) {
        this.os = os;
        bestSol = new Sol(os);
        Sol current = new Sol(os);
        Sol pertubed = new Sol(os);

        int bestFO = bestSol.FO();
        int currentFO = bestSol.FO();

        double Tini = bestFO*B/Math.log(1/A);
        System.out.println("Tini "+Tini);
        int pertPerIte = os.N*os.N;
        double lambda = .9;
        double T = Tini;


        for (int i = 0; i < ite; i++) {
            for (int j = 0; j < pertPerIte; j++) {
                pertube(current,pertubed);
                int pFO = pertubed.FO();
                int delta = pFO-currentFO;
                if(delta < 0){
                    current.copy(pertubed);
                    currentFO = pFO;
                    if(pFO < bestFO){
                        bestSol.copy(pertubed);
                        //System.out.println("SA: "+pFO);
                    }
                    System.out.println(T+"   "+pFO);
                }else if(Utils.rd.nextDouble() < P(delta,T)){
                    current.copy(pertubed);
                    System.out.println(T+" * "+pFO);
                }
            }
            T*= lambda;
        }




        return 0;
    }

    private double P(int delta, double t) {
        return 1/Math.exp(delta/t);
    }

    private void pertube(Sol current, Sol currentp) {
        int a = Utils.rd.nextInt(os.N);
        int b = Utils.rd.nextInt(os.N);
        while (b==a)
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
