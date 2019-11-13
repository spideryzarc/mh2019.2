/**Random Multi Start*/
public class RMS implements Solver {
    private OS os;
    /**número de iterações (restarts)*/
    private int ite;
    private Sol best;
    public RMS(int ite){
        this.ite = ite;
    }

    @Override
    public String toString() {
        return "RMS{" +
                "ite=" + ite +
                '}';
    }

    public Sol getSol(){
        return best;
    }

    public int run(OS os){
        this.os = os;
        best = new Sol(os);
        Sol current = new Sol(os);
        int bestFO = best.FO();
        VND vnd = new VND(os);
        for (int i = 0; i < ite; i++) {
            Utils.shuffler(current.order);
            int x = vnd.run(current);
            if(x < bestFO){
                bestFO = x;
                best.copy(current);
                System.out.println("RMS: "+bestFO);
            }
        }
        return bestFO;
    }
}
