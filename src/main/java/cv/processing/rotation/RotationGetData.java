package cv.processing.rotation;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.core.PShape;
import processing.event.KeyEvent;

import java.io.File;

public class RotationGetData extends PApplet {
    private PMatrix3D originalMatrix = new PMatrix3D();
    private PMatrix3D extractedMatrix = new PMatrix3D();
    private PShape rocket;

    private PShape originalShape;
    private PShape extractedShape;

    @Override
    public void settings() {
        size(1024, 768, P3D);
    }

    @Override
    public void setup() {
        rocket = loadShape(new File("src\\main\\resources\\shapes\\rocket\\rocket.obj").getAbsolutePath());
        calculate(2, 3);
    }

    @Override
    public void draw() {
        background(0xffffffff);


        ortho();
        directionalLight(255, 255, 255, 0, 0.6f, -0.8f);
        //calculate();


        if (mousePressed) {
            calculate(-(float) mouseY / 100, (float) mouseX / 100);
            surface.setTitle("x = " + (float) mouseX / 100);
        }
        drawgrid();

        shape(originalShape);
        shape(extractedShape);

        pushMatrix();
        rocket.applyMatrix(originalMatrix);
        translate(width / 2, height / 2 + 100, -200);
//        rotateZ(PI);
        shape(rocket);
        popMatrix();

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

    private void calculate(float x, float y) {
        originalMatrix = null;
        extractedMatrix = null;
        originalMatrix = new PMatrix3D();
        extractedMatrix = new PMatrix3D();

//        float[] originalFloats = {random(-PI, PI), random(-PI, PI), random(-PI, PI)};
        float[] originalFloats = {x, y, 0};
        float[] extractedFloats = new float[3];

        rotateMatrix(originalMatrix, originalFloats);
//        println("ORIGINAL");
//        originalMatrix.print();
//        println(originalFloats[0], originalFloats[1], originalFloats[2]);

        euler(originalMatrix, extractedFloats);

        rotateMatrix(extractedMatrix, extractedFloats);

//        println("\nEXTRACTED");
//        extractedMatrix.print();
//        println(extractedFloats[0], extractedFloats[1], extractedFloats[2]);


        originalShape = createShape(BOX, 1);
        originalShape.setStrokeWeight(0);
        originalShape.setFill(0xffff7f00);
        originalShape.applyMatrix(originalMatrix);
        originalShape.scale(64);
        originalShape.translate(width * 0.25f, height * 0.5f);

        extractedShape = createShape(BOX, 1);
        extractedShape.setStrokeWeight(0);
        extractedShape.setFill(0xff007fff);
        extractedShape.applyMatrix(extractedMatrix);
        extractedShape.scale(64);
        extractedShape.translate(width * 0.75f, height * 0.5f);

//        rocket.applyMatrix(extractedMatrix);


    }

    void drawgrid() {
        float[] floats = new float[3];

        euler(extractedMatrix, floats);

        pushMatrix();
        translate(width / 2, height / 2, 0);
        scale(2);

        rotateX(floats[0]);
        rotateY(floats[1]);
        rotateZ(floats[2]);

        strokeWeight(9);

        fill(255, 0, 0);
        stroke(255, 0, 0);
        line(0, 0, 0, 100, 0, 0);
        //textFont(font, 20.0);
        text("X", 100, 0, 0);

        fill(0, 255, 0);
        stroke(0, 255, 0);
        line(0, 0, 0, 0, 100, 0);
        //textFont(font, 20.0);
        text("Y", 0, 100, 0);

        fill(0, 0, 255);
        stroke(0, 0, 255);
        line(0, 0, 0, 0, 0, 100);
        //textFont(font, 20.0);
        text("Z", 0, 0, 100);
        popMatrix();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 10) {
            calculate(2, 3);
        }
    }

    private void rotateMatrix(PMatrix3D matrix3D, float[] array) {
        matrix3D.rotateX(array[0]);
        matrix3D.rotateY(array[1]);
        matrix3D.rotateZ(array[2]);
    }

    public static void main(String[] args) {
        RotationGetData rotationGetData = new RotationGetData();
        rotationGetData.main(rotationGetData.getClass().getName());
    }

}
