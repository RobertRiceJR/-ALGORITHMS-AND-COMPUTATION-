package hw2;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    private static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;

        FastScanner(InputStream is) {
            in = is;
        }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        Integer nextIntNullable() throws IOException {
            int c;
            do {
                c = read();
                if (c == -1) return null; // EOF
            } while (c <= ' ');

            int sign = 1;
            if (c == '-') {
                sign = -1;
                c = read();
            }

            int val = 0;
            while (c > ' ') {
                val = val * 10 + (c - '0');
                c = read();
            }
            return val * sign;
        }

        int nextInt() throws IOException {
            Integer v = nextIntNullable();
            if (v == null) throw new IOException("Unexpected EOF");
            return v;
        }
    }
    //Get-Content input.txt | java -cp . Main
    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(new BufferedInputStream(System.in));

        while (true) {
            Integer nObj = fs.nextIntNullable();
            if (nObj == null) break; 
            int N = nObj;
            int T = fs.nextInt();

            int[] C = new int[N];
            int[] V = new int[N];
            for (int i = 0; i < N; i++) {
                C[i] = fs.nextInt();  // length
                V[i] = fs.nextInt();  // value
            }

            long[] dp = new long[T + 1];

            for (int i = 0; i < N; i++) {
                int len = C[i];
                int val = V[i];
                for (int t = len; t <= T; t++) {
                    long candidate = dp[t - len] + val;
                    if (candidate > dp[t]) {
                        dp[t] = candidate;
                    }
                }
            }

            System.out.println(dp[T]);
        }
    }
}