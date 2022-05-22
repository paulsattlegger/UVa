package p12010;

import java.io.*;
import java.util.*;

class Main {
    static final int NULL = 0;
    static final Node[] bst = new Node[80];
    static final NavigableMap<Integer, Node> balanced = new TreeMap<>();
    static final char[][] canvas = new char[160][80];
    static int size, breadth;

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
            canvas[depth][bst[bst[index].left].column] = '+';
            canvas[depth + 1][bst[bst[index].left].column] = '|';
            for (int i = bst[bst[index].left].column + 1; i < bst[index].column; i++) {
                canvas[depth][i] = '-';
            }
        }
        canvas[depth][bst[index].column] = 'o';
        if (bst[index].right != NULL) {
            for (int i = bst[bst[index].right].column - 1; i > bst[index].column; i--) {
                canvas[depth][i] = '-';
            }
            canvas[depth][bst[bst[index].right].column] = '+';
            canvas[depth + 1][bst[bst[index].right].column] = '|';
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
        for (int i = 0; i < 160; i++) {
            int last = -1;
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < 80; j++) {
                line.append(canvas[i][j]);
                if (canvas[i][j] != ' ') last = j;
            }
            if (last != -1) System.out.println(line.subSequence(0, last + 1));
        }
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
            draw();
            print();
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
