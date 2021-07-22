package cv.processing.yandex;

import java.util.Scanner;

public class BinaryVector {

    public static void main(String[] args) throws Exception {
//        Integer[] vec = {5, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0};

        Scanner scanner = new Scanner(System.in);
        Integer[] vec = new Integer[scanner.nextInt()];
        for (int i = 0; i < vec.length; i++) {
            vec[i] = scanner.nextInt();
        }

        for (int j : vec) {
            System.out.println(j);
        }


        int res = 0;
        int maxLength = 0;
        for (int j = 0; j < vec.length; j++) {
            if (vec[j] == 1) {
                res++;
                if (res > maxLength) {
                    maxLength = res;
                }
            } else {
                res = 0;
            }
        }
        System.out.println("maxLength = " + maxLength);
    }
}

