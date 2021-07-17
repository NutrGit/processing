package cv.processing.pid.test.car;

import processing.core.PApplet;

import java.util.HashMap;
import java.util.Map;

public class Info {
    private String s;
    private PApplet app;

    private int x;
    private int y;

    private boolean isShow = true;

    public Info(String s, int x, int y, PApplet app) {
        this.s = s;
        this.x = x;
        this.y = y;
        this.app = app;
    }

    public void draw() {
        if (isShow) {
            app.noStroke();
            app.fill(0, 0, 255, 19);
            app.rect(x, y, 400, 400);
            app.fill(0);
            app.text("\n" + s, x, y);
        }
    }

    public void updateText(String text){
        s = text;
    }

    public void showInfo(boolean isShow) {
        this.isShow = isShow;
    }

    public void toggleShowInfo() {
        if (isShow) {
            isShow = false;
        } else {
            isShow = true;
        }
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PApplet getApp() {
        return app;
    }

    public void setApp(PApplet app) {
        this.app = app;
    }
}
