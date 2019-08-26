import java.io.FileNotFoundException;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

//        OS os = new OS("../instance_small/t_0.2_0.2_0001");
        OS os = new OS("../instance_small/t_0.8_0.8_0300");
        System.out.println(os);

        RMS rms = new RMS(os,1000);
        rms.run();

    }
}
