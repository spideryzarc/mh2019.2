/**Variable Neighborhood Descendent*/
public class VND {
    OS os;

    public VND(OS os) {
        this.os = os;
    }

    public int run(Sol sol){
        boolean imp = false;
        do{
            imp = LS1(sol);
            if(!imp)
                imp = LS2(sol);
            if(!imp)
                imp = LS3(sol);
            //...
        }while (imp);
        return sol.FO();
    }

    private boolean LS3(Sol sol) {
        return false;
    }

    private boolean LS2(Sol sol) {
        int fo = sol.FO();
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = 0; j < os.N; j++)
            if(j-i > 2 ){
                int aux = sol.order[i];
                for (int k = i; k < j; k++) {
                    sol.order[k] = sol.order[k+1];
                }
                sol.order[j] = aux;

                int d = sol.FO();
                if( d < fo){
                    System.out.println("LS2 _ 1");
                    fo =d;
                    imp = true;
                    continue;
                }
                //desfazer
                aux = sol.order[j];
                for (int k = j; k > i; k--) {
                    sol.order[k] = sol.order[k-1];
                }
                sol.order[i] = aux;

            }else if(i-j > 2 ){
                int aux = sol.order[i];
                for (int k = i; k > j; k--) {
                    sol.order[k] = sol.order[k-1];
                }
                sol.order[j] = aux;

                int d = sol.FO();
                if( d < fo){
                    System.out.println("LS2 _ 2");
                    fo =d;
                    imp = true;
                    continue;
                }
                //desfazer
                aux = sol.order[j];
                for (int k = j; k < i; k++) {
                    sol.order[k] = sol.order[k+1];
                }
                sol.order[i] = aux;

            }

        }
        return imp;
    }

    private boolean LS1(Sol sol) {
        int fo = sol.FO();
        boolean imp = false;
        for (int i = 0; i < os.N; i++) {
            for (int j = i+1; j < os.N; j++) {
                sol.swap(i,j);
                int d = sol.FO();
                if( d < fo){
                    fo =d;
                    imp = true;
                    continue;
                }
                sol.swap(i,j);
            }
        }
        return imp;
    }
}
