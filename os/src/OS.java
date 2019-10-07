import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Order Scheduling
 */
public class OS {
    public int getMaxComp() {
        return maxComp;
    }

    /**
     * tempo máximo de completude
     */
    private int maxComp;
    /**
     * número de pedidos
     */
    int N;
    /**
     * número de máquinas
     */
    int M;
    /**
     * p[i][k]: tempo de processamento do pedido k na máquina i
     */
    int p[][];
    /**
     * d[k]: duo date (vencimento) do pedido k
     */
    int d[];

    public OS(String path) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path));
        M = sc.nextInt();
        N = sc.nextInt();
        p = new int[M][N];
        d = new int[N];

        for (int k = 0; k < N; k++) {
            for (int i = 0; i < M; i++) {
                p[i][k] = sc.nextInt();

            }
            d[k] = sc.nextInt();
        }

        maxComp = 0;
        for (int i = 0; i < M; i++) {
            int sum = 0;
            for (int k = 0; k < N; k++) {
                sum += p[i][k];
            }
            if (maxComp < sum)
                maxComp = sum;
        }
        sc.close();
    }

    @Override
    public String toString() {
        return "OS{" +
                "N=" + N +
                ", M=" + M +
                ", p=" + Arrays.deepToString(p) +
                ", d=" + Arrays.toString(d) +
                '}';
    }
}
