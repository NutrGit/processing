package cv.processing.pid.test;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import processing.core.PApplet;

import static cv.processing.pid.test.Box2dPid.box2d;

public class Boundary {

    // A boundary is a simple rectangle with x,y,width,and height
    private float x;
    private float y;
    private float w;
    private float h;
    private PApplet app;

    // But we also have to make a body for box2d to know about it
    Body b;

    public Boundary(float x_, float y_, float w_, float h_, PApplet pApplet) {
        this.app = pApplet;
        x = x_;
        y = y_;
        w = w_;
        h = h_;

        // Define the polygon
        PolygonShape sd = new PolygonShape();
        // Figure out the box2d coordinates
        float box2dW = box2d.scalarPixelsToWorld(w / 2);
        float box2dH = box2d.scalarPixelsToWorld(h / 2);
        // We're just a box
        sd.setAsBox(box2dW, box2dH);


        // Create the body
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(box2d.coordPixelsToWorld(x, y));
        b = box2d.createBody(bd);

        // Attached the shape to the body using a Fixture
        b.createFixture(sd, 1);
    }

    // Draw the boundary, if it were at an angle we'd have to do something fancier
    public void display() {
        app.fill(0);
        app.stroke(0);
        app.rectMode(app.CENTER);
        app.rect(x, y, w, h);
    }

}