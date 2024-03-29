import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Benchmark {

    public static void run(String path, Solver solver[], String outpath) throws IOException {
        File dir = new File(path);
        File[] list = dir.listFiles();

        FileWriter fw = new FileWriter(new File(outpath),true);

        for(Solver s : solver) {
            int cost = 0;
            int time = 0;
            System.out.println(s);
            for (File f : list) {
                System.out.println(f);

                OS os = new OS(f.getPath());
                System.out.println(os);
                long t = System.currentTimeMillis();
                Utils.rd.setSeed(7);
                cost+= s.run(os);
                time += System.currentTimeMillis()-t;
            }
            double costAVG = (double)cost/list.length;
            double timeAVG = (double)time/list.length;
            System.out.println(costAVG+"\t"+timeAVG);
            fw.write(costAVG+"\t"+timeAVG+"\t"+s+"\n");
            fw.flush();

        }
        fw.close();

    }


}
