package p100;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    Map<Integer, Integer> memory = new HashMap<>();

    public static void main(String[] args) {
        Main p100 = new Main();
        int i = 1;
        int j = 1_000_000;
        int m = 0;
        long start = System.currentTimeMillis();
        for (int p = Math.min(i, j); p <= Math.max(i, j); p++) {
            m = Math.max(m, p100.calculateCycle(p));
        }
        System.out.println(i + " " + j + " " + m);
        System.out.println(System.currentTimeMillis() - start);
    }

    int calculateCycle(int n) {
        int cnt = 1;
        List<Integer> lst = new ArrayList<>();
        while (n != 1) {
            if (memory.containsKey(n)) {
                cnt += memory.get(n) - 1;
                break;
            }
            lst.add(n);
            if (n % 2 == 1) {
                n = 3 * n + 1;
            } else {
                n /= 2;
            }
            cnt += 1;
        }
        memorize(lst, cnt);
        return cnt;
    }

    private void memorize(List<Integer> lst, int cnt) {
        for (Integer i : lst) {
            memory.put(i, cnt--);
        }
    }
}
