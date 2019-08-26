/**HillClinbing*/
public class HC {
    OS os;
    public  HC(OS os){
        this.os = os;
    }


    public int run(Sol sol){
        while(LS(sol));
        return sol.FO();
    }

    private boolean LS(Sol sol) {
        int fo = sol.FO();
        for (int i = 0; i < os.N; i++) {
            for (int j = i+1; j < os.N; j++) {
                sol.swap(i,j);
                if( sol.FO() < fo){
                    return true;
                }
                sol.swap(i,j);
            }
        }
        return false;
    }


}
