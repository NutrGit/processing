package cv.processing.pid.test;

import java.util.ArrayList;
import java.util.List;

public class IController {

    private float e;
    private float y;
    private float t0;
    private List<Float> eSum = new ArrayList<>();

    public IController(float t0) {
        this.e = 0;
        this.y = 0;
        this.t0 = t0;
    }

    public float getY(float t1, float ki) {
        y = t1 + getESum(t0 - t1, ki);
        return y;
    }

    private float getESum(float e0, float ki) {
        e += ki * e0;
        return e;
    }

    public float getE() {
        return this.e;
    }
}