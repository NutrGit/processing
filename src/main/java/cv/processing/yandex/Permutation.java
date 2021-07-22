package cv.processing.yandex;

import java.util.LinkedHashSet;
import java.util.Set;

public class Permutation {

    static Set<String> stringSet = new LinkedHashSet<>();

    static void permute(String s, String answer) {
        if (s.length() == 0) {
//            System.out.println(answer);
            stringSet.add(answer);
            return;
        }

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            String left_substr = s.substring(0, i);
            String right_substr = s.substring(i + 1);
            String rest = left_substr + right_substr;
            permute(rest, answer + ch);
        }
    }

    // Driver code
    public static void main(String args[]) {

        String s;
        String answer = "";

        s = "(())";

        permute(s, answer);

        int j = 0;
        for (String s1 : stringSet) {
            j++;
            System.out.println(j + " " + s1);
        }
    }
}

