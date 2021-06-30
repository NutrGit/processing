package cv.processing.nyar;

import processing.core.PApplet;

public class NyArApp extends PApplet {

    @Override
    public void settings() {
        size(1024, 768, P3D);
    }

    @Override
    public void setup() {
    }

    @Override
    public void draw() {
        if (mousePressed) {
            circle(mouseX, mouseY, 100);
        }
    }

    public static void main(String[] args) {
        NyArApp nyArApp = new NyArApp();
        nyArApp.main(nyArApp.getClass().getName());
    }
}
