package p12329;

import java.io.*;
import java.util.*;

class Main {
    static final int NULL = 0;
    static final Node[] bst = new Node[100_000];
    static final NavigableMap<Integer, Node> balanced = new TreeMap<>();
    static final char[][] canvas = new char[200][200];
    static int size, breadth, r, c, rI, cI;

    static void draw(int y, int x, char ch) {
        // draw conditionally, i.e. if in out[] "window"
        y -= r - 1;
        x -= c - 1;
        if (x < 0 || y < 0 || x > 199 || y > 199) return;
        canvas[y][x] = ch;
    }

    static void draw() {
        breadth = 0;
        for (char[] line : canvas) Arrays.fill(line, ' ');
        draw(0, 0);
    }

    static void draw(int index, int depth) {
        if (bst[index].left != NULL) {
            draw(bst[index].left, depth + 2);
        }
        bst[index].column = breadth++;
        if (bst[index].right != NULL) {
            draw(bst[index].right, depth + 2);
        }
        if (bst[index].left != NULL) {
            draw(depth, bst[bst[index].left].column, '+');
            draw(depth + 1, bst[bst[index].left].column, '|');
            for (int i = bst[bst[index].left].column + 1; i < bst[index].column; i++) {
                draw(depth, i, '-');
            }
        }
        draw(depth, bst[index].column, 'o');
        if (bst[index].right != NULL) {
            for (int i = bst[bst[index].right].column - 1; bst[index].column < i; i--) {
                draw(depth, i, '-');
            }
            draw(depth, bst[bst[index].right].column, '+');
            draw(depth + 1, bst[bst[index].right].column, '|');
        }
    }

    static void insertNode(int key) {
        if (size > 0) {
            // either lowerEntry or higherEntry yields the key's parent node
            Map.Entry<Integer, Node> node = balanced.lowerEntry(key);
            boolean added = false;
            if (node != null) added = insertNode(key, node.getValue());
            if (!added) {
                node = balanced.higherEntry(key);
                insertNode(key, node.getValue());
            }
        }
        // insert child node
        bst[size] = new Node(key);
        balanced.put(key, bst[size]);
        size++;
    }

    static boolean insertNode(int key, Node parent) {
        // connect parent node to (to be inserted) child node
        if (key < parent.key && parent.left == NULL) {
            parent.left = size;
            return true;
        } else if (parent.right == NULL) {
            parent.right = size;
            return true;
        }
        return false;
    }

    static void init() {
        Arrays.fill(bst, null);
        balanced.clear();
        size = 0;
    }

    static void print() {
        for (int i = 0; i < rI; i++) {
            boolean print = false;
            StringBuilder line = new StringBuilder(cI);
            for (int j = 0; j < cI; j++) {
                if (canvas[i][j] != ' ') print = true;
                line.append(canvas[i][j]);
            }
            if (print) {
                System.out.println(line);
            }
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for (int i = 1; i <= t; i++) {
            init();
            System.out.println("Case #" + i + ":");
            int n = sc.nextInt();
            for (int j = 1; j <= n; j++) {
                int v = sc.nextInt();
                insertNode(v);
            }
            int m = sc.nextInt();
            for (int j = 1; j <= m; j++) {
                r = sc.nextInt();
                c = sc.nextInt();
                rI = sc.nextInt();
                cI = sc.nextInt();
                draw();
                print();
            }
        }
        sc.close();
    }

    static class Node {
        int key, left, right, column;

        Node(int key) {
            this.key = key;
        }
    }

    static class Scanner implements AutoCloseable {
        private final BufferedReader reader;
        private StringTokenizer tokenizer;

        public Scanner(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream));
        }

        String next() {
            while (tokenizer == null || !tokenizer.hasMoreElements()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
            return tokenizer.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        @Override
        public void close() throws Exception {
            reader.close();
        }
    }
}
