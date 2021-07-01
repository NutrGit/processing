package cv.processing.test;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.event.KeyEvent;

public class RotationGetData extends PApplet {
    private PMatrix3D m = new PMatrix3D();
    private PMatrix3D n = new PMatrix3D();

    private PShape a;
    private PShape b;

    @Override
    public void settings() {
        size(1024, 768, P3D);
    }

    @Override
    public void setup() {
        calculate();
    }

    @Override
    public void draw() {
        background(0xffffffff);
        ortho();
        directionalLight(255, 255, 255, 0, 0.6f, -0.8f);
        //calculate();
        shape(a);
        shape(b);
    }

    private float[] euler(PMatrix3D m) {
        return euler(m, new float[3]);
    }

    private float[] euler(PMatrix3D m, float[] out) {
        // y-axis is inverted in Processing.
        out[0] = atan2(-m.m12, m.m22);
        out[1] = atan2(m.m02, sqrt(pow(m.m12, 2.0f) + pow(m.m22, 2.0f)));
        out[2] = atan2(-m.m01, m.m00);
        return out;
    }

    private void calculate() {
        m = null;
        n = null;
        m = new PMatrix3D();
        n = new PMatrix3D();
        float[] original = {random(-PI, PI), random(-PI, PI), random(-PI, PI)};
        float[] extracted = new float[3];

        m.rotateX(original[0]);
        m.rotateY(original[1]);
        m.rotateZ(original[2]);

        println("ORIGINAL");
        m.print();
        println(original[0], original[1], original[2]);

        euler(m, extracted);

        n.rotateX(extracted[0]);
        n.rotateY(extracted[1]);
        n.rotateZ(extracted[2]);

        println("\nEXTRACTED");
        n.print();
        println(extracted[0], extracted[1], extracted[2]);

        a = createShape(BOX, 1);
        a.setStrokeWeight(0);
        a.setFill(0xffff7f00);
        a.applyMatrix(m);
        a.scale(64);
        a.translate(width * 0.25f, height * 0.5f);

        b = createShape(BOX, 1);
        b.setStrokeWeight(0);
        b.setFill(0xff007fff);
        b.applyMatrix(n);
        b.scale(64);
        b.translate(width * 0.75f, height * 0.5f);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 10) {
            calculate();
        }
    }

    public static void main(String[] args) {
        RotationGetData rotationGetData = new RotationGetData();
        rotationGetData.main(rotationGetData.getClass().getName());
    }

}
