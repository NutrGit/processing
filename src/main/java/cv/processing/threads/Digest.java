package cv.processing.threads;

import java.util.HashMap;
import java.util.Map;

public abstract class Digest {
    private Map<byte[], byte[]> cache = new HashMap<byte[], byte[]>();

    public byte[] digest(byte[] input) {
        byte[] result = cache.get(input);
        if (result == null) {
            System.out.println("res 1");
            synchronized (cache) {
                result = cache.get(input);
                if (result == null) {
                    System.out.println("res 2");
                    result = doDigest(input);
                    cache.put(input, result);
                }
            }
        }
        return result;
    }

    protected abstract byte[] doDigest(byte[] input);

}
