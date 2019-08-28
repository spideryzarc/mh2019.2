import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

//        OS os = new OS("../instance_small/t_0.2_0.2_0001");
//        OS os = new OS("../instance_small/t_0.8_0.8_0300");
        OS os = new OS("../cos_tardiness_BIG/bt_0.8_0.8_0160");
        System.out.println(os);

        RMS rms = new RMS(os,100);
        ILS ils = new ILS(os,100,10,20);
        long t = System.currentTimeMillis();
//        rms.run();
        System.out.println(System.currentTimeMillis()-t);

        t = System.currentTimeMillis();
        ils.run();
        System.out.println(System.currentTimeMillis()-t);

    }
}
