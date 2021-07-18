package cv.processing.pid.test.car;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import processing.core.PImage;

import java.io.File;

import static java.lang.Math.PI;


public class Car {

    private float width;
    private float height;
    private Body body;
    private CarApp app;
    private PImage carImage;
    private float pi2 = (float) (3 * PI / 2);
    private Vec2 dirVec = new Vec2();

    public Car(float x, float y, CarApp app) {
        this.width = 60;
        this.height = 140;
        this.app = app;
        File file = new File("src/main/resources/data/car/car.png");
        carImage = app.loadImage(file.getAbsolutePath());
        makeBody(new Vec2(x, y), width, height);
    }

    void makeBody(Vec2 center, float w_, float h_) {

        // Define a polygon (this is what we use for a rectangle)
        PolygonShape sd = new PolygonShape();
        float box2dW = app.box2d.scalarPixelsToWorld(w_ / 2);
        float box2dH = app.box2d.scalarPixelsToWorld(h_ / 2);
        sd.setAsBox(box2dW, box2dH);

        // Define a fixture
        FixtureDef fd = new FixtureDef();
        fd.shape = sd;
        // Parameters that affect physics
        fd.density = 1;
        fd.friction = 0.3f;
        fd.restitution = 0.5f;

        // Define the body and make it from the shape
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(app.box2d.coordPixelsToWorld(center));

        body = app.box2d.createBody(bd);
        body.createFixture(fd);
    }

    void display() {

        // We look at each body and get its screen position
        Vec2 pos = app.box2d.getBodyPixelCoord(body);
        // Get its angle of rotation
        float a = body.getAngle();

        app.rectMode(app.CENTER);
        app.pushMatrix();
        app.translate(pos.x, pos.y);
        app.rotate(-a);
        app.fill(100);
        app.image(carImage, 0, 0);

        app.stroke(255, 0, 255);
//        app.noStroke();
        app.strokeWeight(2);
//        app.rect(0, 0, width, height);

        app.popMatrix();
        dirVec.x = (float) (pos.x + 100 * Math.cos(pi2 - a));
        dirVec.y = (float) (pos.y + 100 * Math.sin(pi2 - a));
        app.line(pos.x, pos.y, dirVec.x, dirVec.y);
    }

    public Vec2 getDirVec(){
        return dirVec;
    }

    void attract(float x, float y) {
        // From BoxWrap2D example
        Vec2 worldTarget = app.box2d.coordPixelsToWorld(x, y);
        Vec2 bodyVec = body.getWorldCenter();
        // First find the vector going from this body to the specified point
        worldTarget.subLocal(bodyVec);
        // Then, scale the vector to the specified force
        worldTarget.normalize();
        worldTarget.mulLocal((float) 5000);
        // Now apply it to the body's center of mass.
        body.applyForce(worldTarget, bodyVec);
    }

    void attract(float x) {
        // From BoxWrap2D example
        Vec2 worldTarget = app.box2d.coordPixelsToWorld(x, app.height - 100);
        Vec2 bodyVec = body.getWorldCenter();
        // First find the vector going from this body to the specified point
        worldTarget.subLocal(bodyVec);
        // Then, scale the vector to the specified force
        worldTarget.normalize();
        worldTarget.mulLocal((float) 5000);
        // Now apply it to the body's center of mass.
        body.applyForce(worldTarget, bodyVec);
    }

    public Body getBody() {
        return body;
    }
}
