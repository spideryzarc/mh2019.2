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
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = i+1; j < os.N; j++) {
                sol.swap(i,j);
                int d = sol.FO();
                if( d < fo){
//                    return true;
                    fo =d;
                    imp = true;
                    continue;
                }
                sol.swap(i,j);
            }
        }
//        System.out.println(fo);
        return imp;
    }


}
