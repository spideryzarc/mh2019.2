import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

//        OS os = new OS("../instance_small/t_0.2_0.2_0001");
//        OS os = new OS("../instance_small/t_0.8_0.8_0300");
        OS os = new OS("../cos_tardiness_BIG/bt_0.2_0.2_0001");
        System.out.println(os);

        RMS rms = new RMS(100);
        ILS ils = new ILS(100,10,10);
        VNS vns = new VNS(100,new int[][]{{10,20,2},
                {20,30,2},
                {30,40,2},
                {40,50,20}});
        GRASP grasp = new GRASP(100,10);

        GLS gls = new GLS(100);

        SA sa = new SA(0.2,0.0001,0.0001,100);

        TS ts = new TS(100,100);
//        long t = System.currentTimeMillis();
////        rms.run();
//        System.out.println(System.currentTimeMillis()-t);
//
//        t = System.currentTimeMillis();
////        grasp.run();
//        ils.run(os);
//        System.out.println(System.currentTimeMillis()-t);

//        Solver[] s = new Solver[]{rms,ils,vns,grasp};
        Solver[] s = new Solver[]{ts,ils};
        Benchmark.run("../b0001/",s,"result.txt");

    }
}
