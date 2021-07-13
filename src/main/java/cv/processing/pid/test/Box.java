package cv.processing.pid.test;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import processing.core.PApplet;

import static cv.processing.pid.test.Box2dPid.box2d;

public // The Nature of Code
// <http://www.shiffman.net/teaching/nature>
// Spring 2012
// Box2DProcessing example

// A rectangular box
class Box {

    private float temp;
    private Body body;
    private float w;
    private float h;
    private PApplet app;


    public Box(float x, float y, PApplet pApplet, float temp) {
        this.app = pApplet;
//        w = app.random(8, 16);
//        h = w;

        w = 5;
        h = 5;
        this.temp = temp;
        // Add the box to the box2d world
        makeBody(new Vec2(x, y), w, h);
    }

    // This function removes the particle from the box2d world
    void killBody() {
        box2d.destroyBody(body);
    }

    // Is the particle ready for deletion?
    boolean done() {
        // Let's find the screen position of the particle
        Vec2 pos = box2d.getBodyPixelCoord(body);
        // Is it off the bottom of the screen?
        if (pos.y > app.height + w * h) {
            killBody();
            return true;
        }
        return false;
    }

    void attract(float x, float y) {
        // From BoxWrap2D example
        Vec2 worldTarget = box2d.coordPixelsToWorld(x, y);
        Vec2 bodyVec = body.getWorldCenter();
        // First find the vector going from this body to the specified point
        worldTarget.subLocal(bodyVec);
        // Then, scale the vector to the specified force
        worldTarget.normalize();
        worldTarget.mulLocal((float) 50);
        // Now apply it to the body's center of mass.
        body.applyForce(worldTarget, bodyVec);
    }


    // Drawing the box
    void display() {
        // We look at each body and get its screen position
        Vec2 pos = box2d.getBodyPixelCoord(body);
        // Get its angle of rotation
        float a = body.getAngle();

        app.rectMode(app.CENTER);
        app.pushMatrix();
        app.translate(pos.x, pos.y);
        app.rotate(-a);
//        app.fill(175);
        app.fill(temp);
//        app.stroke(0);
        app.noStroke();
        app.rect(0, 0, w, h);

//        app.fill(255);
//        app.text(temp, 0, 0);
        app.popMatrix();
    }

    void setTemp(float temp) {
        this.temp = temp;
    }

    // This function adds the rectangle to the box2d world
    void makeBody(Vec2 center, float w_, float h_) {

        // Define a polygon (this is what we use for a rectangle)
        PolygonShape sd = new PolygonShape();
        float box2dW = box2d.scalarPixelsToWorld(w_ / 2);
        float box2dH = box2d.scalarPixelsToWorld(h_ / 2);
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
        bd.position.set(box2d.coordPixelsToWorld(center));

        body = box2d.createBody(bd);
        body.createFixture(fd);
    }

    public float getTemp() {
        return this.temp;
    }
}