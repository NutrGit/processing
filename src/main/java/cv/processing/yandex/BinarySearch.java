package cv.processing.yandex;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;

public class BinarySearch {
    public int search(int[] nums, int target) {
        int a = 0;
        int b = nums.length - 1;

        int k = (a + b) / 2;
        int n = nums[k];

        if (n < target) {
            a = k + 1;
            if (a <= b) {
                int res = search(Arrays.copyOfRange(nums, a, b + 1), target);
                if (res == -1) {
                    return -1;
                }
                return a + res;
            } else {
                return -1;
            }
        } else if (n > target) {
            b = k - 1;
            if (a <= b) {
                int res = search(Arrays.copyOfRange(nums, a, b + 1), target);
                if (res == -1) {
                    return -1;
                }
                return a + res;
            } else {
                return -1;
            }
        } else {
            return k;
        }

//        if (n < target) {
//            a = k + 1;
//            if (a <= b) {
//                int[] aCopy = Arrays.copyOfRange(nums, a, b + 1);
//                int res = search(aCopy, target);
//                if (res == -1) {
//                    return -1;
//                }
//                return a + res;
//            } else {
//                return -1;
//            }
//        } else if (n > target) {
//            b = k - 1;
//            if (a <= b) {
//                int[] aCopy = Arrays.copyOfRange(nums, a, b + 1);
//                int res = search(aCopy, target);
//                if (res == -1) {
//                    return -1;
//                }
//                return a + res;
//            } else {
//                return -1;
//            }
//        } else {
//            return k;
//        }
    }

    public static void main(String[] args) throws Exception {
        BinarySearch binarySearch = new BinarySearch();
        int[] a = {-4, -1, 0, 3, 5, 9, 12, 15, 16, 19};
        Integer key = 4;
        Integer[] a2 = {-4, -1, 0, 3, 5, 9, 12, 15, 16, 19};
//        System.out.println(binarySearch.search(a, 2));
        Integer res = Collections.binarySearch(Arrays.asList(a2.clone()), key);
        System.out.println("res = " + res);

        int res2 = binarySearch.search(a, key);
        System.out.println("res2 = " + res2);

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        String J = r.readLine();
        String S = r.readLine();

        int result = 0;
        for (int i = 0; i < S.length(); ++i) {
            char ch = S.charAt(i);
            if (J.indexOf(ch) >= 0) {
                ++result;
            }
        }

        System.out.println(result);


    }




}
