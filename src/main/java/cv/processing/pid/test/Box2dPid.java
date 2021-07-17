package cv.processing.pid.test;

import cv.processing.pid.test.fx.App;
import cv.processing.pid.test.fx.Controller;
import lejos.util.PIDController;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import shiffman.box2d.Box2DProcessing;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

// A reference to our box2d world

public class Box2dPid extends PApplet {

    static Box2DProcessing box2d;
    private ArrayList<Boundary> boundaries;
    private ArrayList<Box> boxes;

    //PID controller var
    public static float avgTemp;
    private PController pController;
    private IController iController;
    private PIDController pidController;
    public static float temp = 200;
    private float Kp;
    private float Ki;
    private float Kd;

    //chart
    private int j = 0;
    private float t0;
    private float x0;
    private float y0;

    @Override
    public void draw() {


        fill(255);
        rect(0, 0, width, height * 2);
        surface.setTitle("" + boxes.size() + " \t " + avgTemp);

        // We must always step through time!
        box2d.step();

        // When the mouse is clicked, add a new Box object

        // Display all the boundaries
        for (Boundary wall : boundaries) {
            wall.display();
        }

        if (boxes.size() > 0) {
            fill(255);
            rect(0, 0, 200, 400);
            tempUp(boxes.get(0));
        }

        if (mousePressed) {
            Box p = new Box(mouseX, mouseY, this, random(0, 19));
            boxes.add(p);
        }


        // Display all the boxes
        for (Box b : boxes) {
            b.display();
            setAvgTemp();
            b.setTemp(avgTemp);
        }

        // Boxes that leave the screen, we delete them
        // (note they have to be deleted from both the box2d world and our list
        for (int i = boxes.size() - 1; i >= 0; i--) {
            Box b = boxes.get(i);
            if (b.done()) {
                boxes.remove(i);
            }
        }

        //chart
        j += 2;
        if (j > width) {
            fill(255);
            rect(width, 0, width, height * 2);
            stroke(0, 0, 255, 100);
            line(width / 2, height - t0, width, height - t0);
            x0 = width / 2;
            j = width / 2;
        }
        float temp = Box2dPid.avgTemp;
        stroke(0);
        strokeWeight(1);
        line(x0, y0, j, height - temp);
        x0 = j;
        y0 = height - temp;


        fill(0);
        text(frameRate, 20, 20);

    }

    @Override
    public void settings() {
        size(1024, 768);
    }

    @Override
    public void setup() {
        surface.setResizable(true);

        pidController = new PIDController((int) temp);

        Kp = 1;
        Ki = 1;
        Kd = 1;

        pidController.setPIDParam(0, Kp); //P
        pidController.setPIDParam(1, Ki); //I
        pidController.setPIDParam(2, Kd); //D
//        pidController.setPIDParam(3, 0); //power

        background(255);
        stroke(0, 0, 255, 100);
        line(0, height - t0 * 2, width, height - t0 * 2);
        t0 = temp;
        j = width / 2;

        pController = new PController(temp);
        iController = new IController(temp);

        avgTemp = 0;

        box2d = new Box2DProcessing(this);
        box2d.createWorld();
        box2d.setGravity(0, -20);

        boxes = new ArrayList<Box>();
        boundaries = new ArrayList<Boundary>();

        // Add a bunch of fixed boundaries
        boundaries.add(new Boundary(0, height / 2, 10, height - 100, this));
        boundaries.add(new Boundary(width / 2, height / 2, 10, height - 100, this));
        boundaries.add(new Boundary(width / 4, height - 50, width / 2, 10, this));
    }

    private void setAvgTemp() {
        avgTemp = 0;
        for (Box box : boxes) {
            avgTemp += box.getTemp();
        }
        avgTemp = avgTemp / boxes.size();
    }

    private void tempUp(Box box) {
        float t = box.getTemp();
        float P = pController.getY(t, Kp);
        float I = iController.getY(t, Ki);
        t = P + I;
        fill(0);
        text("Ep = " + pController.getE(), 10, 40);
        text("Ei = " + iController.getE(), 10, 60);

        text("P = " + P, 10, 80);
        text("I = " + I, 10, 100);

        text("Kp = " + Kp, 10, 120);
        text("Ki = " + Ki, 10, 140);
//        t = pController.getY(t);
//        t = pidController.doPID((int) t);
        box.setTemp(t);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        Box p = new Box(mouseX, mouseY, this, random(0, 19));
        boxes.add(p);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKey() == 'd') {
            if (boxes.size() > 1) {
                Box box = boxes.get(0);
                box.killBody();
                boxes.remove(boxes.get(0));
            }
        } else if (event.getKey() == 'u') {
            for (int j = 0; j < 10; j++) {
                boxes.add(new Box(random(0, 100), 0, this, 0));
            }
        } else if (event.getKey() == '9') {
            Kp++;
        } else if (event.getKey() == '0') {
            Kp--;
        } else if (event.getKey() == '7') {
            Ki++;
        } else if (event.getKey() == '8') {
            Ki--;
        }


    }

    public static void main(String[] args) {
        Box2dPid app = new Box2dPid();
        app.main(app.getClass().getName());
    }
}

class Log {

}