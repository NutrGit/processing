package cv.processing.pid.test;

import java.util.ArrayList;
import java.util.List;

public class DController {
    private float y;
    private float e;
    private float t0;
    private static final double DX = 0.0001;
    private int j = 0;
    private int dx;
    private int x;
    private List<Float> eList = new ArrayList<>();

    public DController(float t0) {
        this.e = 0;
        this.y = 0;
        this.t0 = t0;
    }

    public float getY(float t1, float kd) {
        e = t0 - t1;
        eList.add(e);
        if (eList.size() > 10) {
            eList.remove(eList.get(0));
        }
        if (eList.size() == 10) {
            y = kd * (eList.get(9) - eList.get(1)) / 8;
        }
        return y;
    }

    public float getE(){
        return e;
    }


}
