import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

//        OS os = new OS("../instance_small/t_0.2_0.2_0001");
//        OS os = new OS("../instance_small/t_0.8_0.8_0300");
        OS os = new OS("../cos_tardiness_BIG/bt_0.2_0.2_0001");
        System.out.println(os);

        RMS rms = new RMS(30);
        ILS ils = new ILS(30,10,100);
        VNS vns = new VNS(30,new int[][]{{10,20,2},
                {20,30,2},
                {30,40,2},
                {40,50,20}});
        GRASP grasp = new GRASP(30,100);
//        long t = System.currentTimeMillis();
////        rms.run();
//        System.out.println(System.currentTimeMillis()-t);
//
//        t = System.currentTimeMillis();
////        grasp.run();
//        ils.run(os);
//        System.out.println(System.currentTimeMillis()-t);

        Solver[] s = new Solver[]{rms,ils,vns,grasp};

        Benchmark.run("../b0001/",s,"result.txt");

    }
}
