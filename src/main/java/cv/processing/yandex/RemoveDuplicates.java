package cv.processing.yandex;

import java.io.*;

public class RemoveDuplicates {

    static final String input = "input.txt";
    static final String output = "output.txt";

    static BufferedReader r;
    static BufferedWriter w;

    public static void main(String[] args) throws Exception {

        r = new BufferedReader(new FileReader(input));
        w = new BufferedWriter(new FileWriter(output));

        int n = Integer.parseInt(r.readLine());
        if (n > 0) {
            int j0 = Integer.parseInt(r.readLine());
            String s = j0 + "\n";
            w.write(s);
            for (int j = 0; j < n - 1; j++) {
                int j1 = Integer.parseInt(r.readLine());
                if (j1 != j0) {
                    s = j1 + "\n";
                    w.write(s);
                }
                j0 = j1;
            }
        }
        w.flush();
        r.close();
        w.close();
    }
}
