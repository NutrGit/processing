package cv.processing.pid.test;

public class PController {

    private float e;
    private float y;
    private float Kp;
    private float res;
    private float t0;

    public PController(float t0) {
        this.e = 0;
        this.y = 0;
        this.t0 = t0;
    }

    public float getY(float t1, float kp) {
        e = t0 - t1;
        y = t1 + kp * e;
        return y;
    }

    public float getE() {
        return this.e;
    }
}