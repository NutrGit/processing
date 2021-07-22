package cv.processing.pid.test.car;

import org.jbox2d.common.Vec2;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import shiffman.box2d.Box2DProcessing;

import java.io.File;

public class CarApp extends PApplet {
    private PImage wheel;
    public Box2DProcessing box2d;
    private Info info;
    private Car car;
    private Car car2;
    private CarBoundary carBoundary;

    private int j = 0;

    private float t;
    private float wheelAngle = 0;

    @Override
    public void draw() {
        background(255);

        surface.setTitle("" + frameRate);


        fill(0);
        line(t, 0, t, height);
        car.attract(t);
        rotateWheelPImage();
        box2d.step();

        if (mousePressed) {
//            car.attract(mouseX, mouseY);
//            car2.attract(mouseX, mouseY);
            car.attract(mouseX);
//            car2.attract(mouseX);
        }

//        surface.setTitle("body angle = " + car.getBody().getAngle());

        info.updateText("wheel angle = " + wheelAngle +
                "\n car angle = " + car.getBody().getAngle() +
                "\n angular velosity = " + car.getBody().getAngularVelocity() +
                "\n t = " + t);
        info.draw();

        car.display();
//        car2.display();
        carBoundary.display();
    }

    @Override
    public void setup() {
        box2d = new Box2DProcessing(this);
        box2d.createWorld();
        box2d.setGravity(0, 0);

        carBoundary = new CarBoundary(width / 2, height, width, 10, this);

        car = new Car(width / 3, height - 200, this);
//        car2 = new Car(width / 3, height - 200, this);


        info = new Info("info", 0, 0, this);

        imageMode(CENTER);

        background(255);
        t = width / 2;
        File file = new File("src/main/resources/data/car/wheel.png");
        wheel = loadImage(file.getAbsolutePath());
    }

    private void rotateWheelPImage() {
        pushMatrix();
        translate(100, height - 100);
        float rads = radians(wheelAngle);
        rotate(rads);
        image(wheel, 0, 0);
        popMatrix();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        j++;
        if (event.getKeyCode() == 37) {
            //LEFT
            wheelLeft(1f);
        } else if (event.getKeyCode() == 39) {
            //RIGHT
            wheelRight(1f);
        } else if (event.getKeyCode() == 73) {
            //I
            info.toggleShowInfo();
        } else if (event.getKeyCode() == 38) {
            //UP
            moveForward(0);
        } else if (event.getKeyCode() == 40) {
            //DOWN
            moveBackward(0);
        } else if (event.getKey() == ',') {
            //<
            t -= 50;
        } else if (event.getKey() == '.') {
            t += 50;
        }


    }

    private void wheelRight(float y) {
        wheelAngle += y;
        Vec2 carPos = car.getBody().getPosition();
        float radians = radians(-wheelAngle);
        car.getBody().setTransform(carPos, radians);
    }

    private void wheelLeft(float y) {
        wheelAngle -= y;
        Vec2 carPos = car.getBody().getPosition();
        float radians = radians(-wheelAngle);
        car.getBody().setTransform(carPos, radians);
    }

    private void moveForward(float y) {
        car.attract(car.getDirVec().x, car.getDirVec().y);
    }

    private void moveBackward(float y) {
        car.attract(-car.getDirVec().x, -car.getDirVec().y);
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
