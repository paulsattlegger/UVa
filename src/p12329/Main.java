package p12329;

import java.io.*;
import java.util.*;

class Main {
    static final int NULL = 0;
    static final int MAX_CANVAS_SIZE = 200;
    static final NavigableMap<Integer, Node> balanced = new TreeMap<>();
    static Node[] bst;
    static Fragment[] fragments;
    static int size, breadth;

    static void draw() {
        breadth = 0;
        draw(0, 0);
    }

    static void drawAll(int y, int x, char ch) {
        for (Fragment f : fragments) {
            f.draw(y, x, ch);
        }
    }

    static void draw(int index, int depth) {
        // in-order traversal to add column to node
        if (bst[index].left != NULL) {
            draw(bst[index].left, depth + 2);
        }
        bst[index].column = breadth++;
        if (bst[index].right != NULL) {
            draw(bst[index].right, depth + 2);
        }
        // in-order traversal to draw +---
        //                            |
        if (bst[index].left != NULL) {
            drawAll(depth, bst[bst[index].left].column, '+');
            drawAll(depth + 1, bst[bst[index].left].column, '|');
            for (int i = bst[bst[index].left].column + 1; i < bst[index].column; i++) {
                drawAll(depth, i, '-');
            }
        }
        drawAll(depth, bst[index].column, 'o');
        // in-order traversal to draw ---+
        //                               |
        if (bst[index].right != NULL) {
            for (int i = bst[bst[index].right].column - 1; bst[index].column < i; i--) {
                drawAll(depth, i, '-');
            }
            drawAll(depth, bst[bst[index].right].column, '+');
            drawAll(depth + 1, bst[bst[index].right].column, '|');
        }
    }

    static void insertNode(int key) {
        if (size > 0) {
            // either lowerEntry or higherEntry yields the key's parent node
            Map.Entry<Integer, Node> parent = balanced.lowerEntry(key);

            // connect parent node to (to be inserted) child node
            if (parent != null && parent.getValue().right == NULL) {
                parent.getValue().right = size;
            } else {
                parent = balanced.higherEntry(key);
                parent.getValue().left = size;
            }
        }
        // insert child node
        bst[size] = new Node(key);
        balanced.put(key, bst[size]);
        size++;
    }

    static void init(int n) {
        bst = new Node[n];
        balanced.clear();
        size = 0;
    }


    public static void main(String[] args) throws Exception {
        try (Scanner sc = new Scanner(System.in)) {
            int t = sc.nextInt();
            for (int i = 1; i <= t; i++) {
                System.out.println("Case #" + i + ":");
                int n = sc.nextInt();
                init(n);
                for (int j = 0; j < n; j++) {
                    int v = sc.nextInt();
                    insertNode(v);
                }
                int m = sc.nextInt();
                fragments = new Fragment[m];
                for (int j = 0; j < m; j++) {
                    fragments[j] = new Fragment(new char[MAX_CANVAS_SIZE][MAX_CANVAS_SIZE], sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
                }
                draw();
                for (Fragment f : fragments) {
                    f.print();
                }
            }
        }
    }

    static class Node {
        int key, left, right, column;

        Node(int key) {
            this.key = key;
        }
    }

    static class Fragment {
        final char[][] canvas;
        final int r, c, rI, cI;

        Fragment(char[][] canvas, int r, int c, int rI, int cI) {
            this.canvas = canvas;
            this.r = r;
            this.c = c;
            this.rI = rI;
            this.cI = cI;
        }

        void draw(int y, int x, char ch) {
            // draw conditionally, i.e. if in canvas "window"
            y -= r - 1;
            x -= c - 1;
            if (x < 0 || y < 0 || x > MAX_CANVAS_SIZE - 1 || y > MAX_CANVAS_SIZE - 1) return;
            canvas[y][x] = ch;
        }


        void print() {
            for (int i = 0; i < rI; i++) {
                boolean print = false;
                StringBuilder line = new StringBuilder(cI);
                for (int j = 0; j < cI; j++) {
                    if (canvas[i][j] == 0) {
                        line.append(' ');
                    } else {
                        print = true;
                        line.append(canvas[i][j]);
                    }
                }
                if (print) {
                    System.out.println(line);
                }
            }
            System.out.println();
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
