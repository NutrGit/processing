package cv.processing.threads;

public class App {

    public static void main(String[] args) {
        Digest digest = new Digest() {
            @Override
            protected byte[] doDigest(byte[] input) {
                System.out.println("input");
                for (byte b : input) {
                    System.out.println(b);
                }
                return input;
            }
        };


        byte[] a = {1, 2, 0};
        byte[] a1 = digest.digest(a);
        System.out.println("test");
        for (byte b : a1) {
            System.out.println(b);
        }
    }
}
