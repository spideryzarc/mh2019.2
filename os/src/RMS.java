/**Random Multi Start*/
public class RMS {
    OS os;
    int ite;
    Sol best;
    public RMS(OS os, int ite){
        this.os = os;
        this.ite = ite;
        best = new Sol(os);
    }

    public Sol getSol(){
        return best;
    }

    public int run(){
        Sol current = new Sol(os);
        int bestFO = best.FO();
        HC hc = new HC(os);
        for (int i = 0; i < ite; i++) {
            Utils.shuffler(current.order);
//            int x = current.FO();
            int x = hc.run(current);
            if(x < bestFO){
                bestFO = x;
                best.copy(current);
                System.out.println("RMS: "+bestFO);
            }

        }
        return bestFO;
    }
}
