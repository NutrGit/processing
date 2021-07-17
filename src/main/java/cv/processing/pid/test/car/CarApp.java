package cv.processing.pid.test.car;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

import java.io.File;

public class CarApp extends PApplet {
    private float t;
    private PImage wheel;
    private int j = 0;
    private float wheelAngle = 0;

    @Override
    public void draw() {
        background(255);
        fill(0);
        line(t, 0, t, height);
        rotateWheelPImage();
    }

    private void rotateWheelPImage() {
        pushMatrix();
        translate(width / 2, height / 2);
        float rads = radians(wheelAngle);
        rotate(rads);
        image(wheel, 0, 0);
        popMatrix();
    }

    @Override
    public void setup() {
        imageMode(CENTER);
        background(255);
        t = width / 2;
        File file = new File("src/main/resources/data/car/wheel.png");
        wheel = loadImage(file.getAbsolutePath());
    }

    @Override
    public void keyPressed(KeyEvent event) {
        j++;
        if (event.getKeyCode() == 37) {
            //LEFT
            wheelAngle--;
        } else if (event.getKeyCode() == 39) {
            //RIGHT
            wheelAngle++;
        }
        surface.setTitle("" + wheelAngle);
    }

    @Override
    public void settings() {
        size(1024, 768);
    }

    public static void main(String[] args) {
        CarApp app = new CarApp();
        app.main(app.getClass().getName());
    }
}
