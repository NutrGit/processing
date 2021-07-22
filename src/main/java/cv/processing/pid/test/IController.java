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
        float e0 = t0 - t1;
        float e1 = e;
        e = ki * (e1 + e0);
        y = t1 + e;
        return y;
    }

    public float getE() {
        return this.e;
    }
}