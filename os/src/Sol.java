import java.util.Arrays;

import static java.util.Arrays.fill;

public class Sol {
    OS os;
    /**
     * order[j] o pedido na posição j
     */
    int order[];

    public Sol(OS os) {
        this.os = os;
        order = new int[os.N];
        for (int j = 0; j < order.length; j++)
            order[j] = j;

        tard = new int[os.N];

    }

    private int tard[];

    /**
     * calcula a função objetivo
     */
    public int FO() {
        fill(tard, 0);
        for (int i = 0; i < os.M; i++) {
            int t = 0;
            for (int j = 0; j < os.N; j++) {
                int k = order[j];
                t += os.p[i][k];
                if (t > os.d[k]) {
                    int r = t - os.d[k];
                    if (r > tard[k])
                        tard[k] = r;
                }
            }
        }
        int s = 0;
        for (int k = 0; k < os.N; k++) {
            s += tard[k];
        }
        return s;
    }

    @Override
    public String toString() {
        return "Sol{" +
                "order=" + Arrays.toString(order) +
                '}';
    }

    public void swap(int i, int j) {
        int aux = order[i];
        order[i] = order[j];
        order[j] = aux;
    }

    public void copy(Sol src) {
        for (int i = 0; i < os.N; i++) {
            order[i] = src.order[i];
            tard[i] = src.tard[i];
        }
    }
}
