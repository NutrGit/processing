package cv.processing.pid.test;

import processing.core.PApplet;

import java.util.function.DoubleFunction;

public class DController extends PApplet {
    private float y;
    private float e;
    private float t0;
    private static final double DX = 0.0001;
    private int j = 0;

    public static void main(String[] args) {
        DController app = new DController(200);
        app.main(app.getClass().getName());
    }

    @Override
    public void draw() {
        j++;
        float y0 = j * j * j;
        fill(0);
    }

    @Override
    public void settings() {
        size(1024, 768);
    }

    @Override
    public void setup() {
        background(255);
    }

    public DController(float t0) {
        this.e = 0;
        this.y = 0;
        this.t0 = t0;
    }

    public float getY(float t1, float kd) {
        DoubleFunction<Double> res = derive((x) -> x * x);
        return y;
    }

    private static DoubleFunction<Double> derive(DoubleFunction<Double> f) {
        return (x) -> (f.apply(x + DX) - f.apply(x)) / DX;
    }
}
