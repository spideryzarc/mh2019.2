import java.util.Arrays;
import java.util.Objects;

import static java.util.Arrays.fill;

public class Sol {
    /**
     * tempo de completude do pedido
     */
    public int[] compTime;
    OS os;
    /**
     * order[j] o pedido na posição j
     */
    Integer order[];

    public Sol(OS os) {
        this.os = os;
        order = new Integer[os.N];
        for (int j = 0; j < order.length; j++)
            order[j] = j;

        tard = new int[os.N];
        compTime = new int[os.N];

    }

    int tard[];

    /**
     * calcula a função objetivo
     */
    public int FO() {
        fill(tard, 0);
        fill(compTime, 0);
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

                if (compTime[k] < t)
                    compTime[k] = t;
            }
        }
        int s = 0;
        for (int k = 0; k < os.N; k++) {
            s += tard[k];
        }
        return fo = s;
    }

    /**
     * última função objetivo calculada
     */
    public int fo;

    @Override
    public String toString() {
        return "Sol{" +

                "fo=" + fo +
                ", order=" + Arrays.toString(order) +
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
        fo = src.fo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sol sol = (Sol) o;
        return fo == sol.fo;
//        &&
//                Arrays.equals(order, sol.order);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(fo);
        result = 31 * result + Arrays.hashCode(order);
        return result;
    }
}
