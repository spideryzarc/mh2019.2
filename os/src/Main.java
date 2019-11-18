import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

//        OS os = new OS("../instance_small/t_0.2_0.2_0001");
//        OS os = new OS("../instance_small/t_0.8_0.8_0300");
        //OS os = new OS("../cos_tardiness_BIG/bt_0.2_0.2_0001");
        //System.out.println(os);

        RMS rms = new RMS(10);
        ILS ils = new ILS(10, 30, 50);
        VNS vns = new VNS(10, new int[][]{{10, 20, 2},
                {20, 30, 2},
                {30, 40, 2},
                {40, 50, 20}});
        GRASP grasp = new GRASP(10, 10);

        GLS gls = new GLS(10);

        SA sa = new SA(0.2, 0.0001, 0.0001, 30);

        TS ts = new TS(10, 30);

        DEA dea = new DEA(1000, 1000, .1, 0.3);
        DEA2 dea2 = new DEA2(500, 400, .2, 0.1);
        GA ga = new GA(2000, 200, 60, .8, .25);

        SS ss = new SS(100, 1000, 20);
//        long t = System.currentTimeMillis();
////        rms.run();
//        System.out.println(System.currentTimeMillis()-t);
//
//        t = System.currentTimeMillis();
////        grasp.run();
//        ils.run(os);
//        System.out.println(System.currentTimeMillis()-t);

        Solver[] s = new Solver[]{ga};
//        Solver[] s = new Solver[]{rms, ils, vns, grasp, gls, sa, ts, dea2, ga, ss};
        Benchmark.run("../0160/", s, "result.txt");
//          Benchmark.run("../0001/", s, "result.txt");
//        Benchmark.run("../benchmark/", s, "result.txt");

    }
}
