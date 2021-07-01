package cv.processing.nyar;

import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.File;
import java.util.ArrayList;

public class NyArApp extends PApplet {

    /**
     * NyARToolkit for proce55ing/3.0.5
     * (c)2008-2017 nyatla
     * airmail(at)ebony.plala.or.jp
     * <p>
     * ２つの座標系を同時に扱う例です。
     * 人マーカの上に右手系、Hiroマーカの上に左手系の座標系を使って、立方体を表示します。
     * 全ての設定ファイルとマーカファイルはスケッチディレクトリのlibraries/nyar4psg/dataにあります。
     * <p>
     * This sketch handles 2 coordinate system in same time.(left and right).
     * Each markers show different coordinate system.
     * The marker is "patt.hiro" and "patt.kanji"
     * Any pattern and configuration files are found in libraries/nyar4psg/data inside your sketchbook folder.
     */


    //Capture cam;
    MultiMarker nya_l;
    PFont font;

    float rotX = 0;

    float shiftX;
    float shiftY;
    float shiftZ;
    float xScene, yScene;

    float w;
    float h;

    float a = 7;
    int zval = -50;
    float scaleVal = 1;
    float fov;
    boolean isMovingMode;

    float fovVal = 3;

    ArrayList<PImage> images = new ArrayList();
    int n = 0;
    float c = 0.1f;

//    File resourcesDirectory = new File("src/main/resources/data");

    @Override
    public void setup() {


        //size(640, 512, P3D);

        String s = "src\\main\\resources\\data\\1";
        File folderName = new File(s);
        println(folderName.getAbsolutePath());
        File[] fileNames = folderName.listFiles();
        for (File f : fileNames) {
            println("f.getAbsolutePath() = " +
                    f.getAbsolutePath());
            PImage img = loadImage(f.getAbsolutePath());
            images.add(img);
        }

        //font=createFont("FFScala", 32);
        colorMode(RGB, 100);
        println(MultiMarker.VERSION);

        //キャプチャを作成
        //cam=new Capture(this,640,480);
        File fileMaker = new File("src\\main\\resources\\data\\camera_para.dat");
        nya_l = new MultiMarker(this, width, height, fileMaker.getAbsolutePath(),
                new NyAR4PsgConfig(NyAR4PsgConfig.CS_LEFT_HAND, NyAR4PsgConfig.TM_NYARTK));
        //nya_l.addARMarker("../../data/patt.hiro", 80);
        nya_l.addNyIdMarker(0, 80);
        println(images.size());
    }

    void drawgrid() {
        pushMatrix();
        stroke(0);
        strokeWeight(2);
        line(0, 0, 0, 100, 0, 0);
        //textFont(font, 20.0);
        text("X", 100, 0, 0);
        line(0, 0, 0, 0, 100, 0);
        //textFont(font, 20.0);
        text("Y", 0, 100, 0);
        line(0, 0, 0, 0, 0, 100);
        //textFont(font, 20.0);
        text("Z", 0, 0, 100);
        popMatrix();
    }

    @Override
    public void draw() {
        c += 0.01;
        surface.setTitle("" + frameRate);

        checkWindowResised();

        if (keyPressed) {
            if (keyCode == LEFT) {
                a += 0.05;
            } else if (keyCode == RIGHT) {
                a -= 0.05;
            }
        }

        //nya_r.detect(pic);
        PImage pic1 = images.get(n);
        if (pic1 == null) {
            println("null");
        }
        nya_l.detect(pic1);
        background(0);
        nya_l.drawBackground(images.get(n));//frustumを考慮した背景描画

        for (int i = 0; i < 1; i++) {
            if ((!nya_l.isExist(i))) {
                continue;
            }
            nya_l.beginTransform(i);
            fill(0, 255, 0);
            drawgrid();
            fill(100 * (((i + 1) / 4) % 2), 100 * (((i + 1) / 2) % 2), 100 * (((i + 1)) % 2));
            rotate(c);
            translate(0, 0, 20);

            box(40);
            nya_l.endTransform();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        println(event.getKeyCode());
        if (event.getKeyCode() == 37) {
            rotX++;
            println("rotX = " + rotX);
        } else if (event.getKeyCode() == 39) {
            rotX--;
            println("rotX = " + rotX);
        } else if (event.getKeyCode() == 10) {
        } else if (event.getKeyCode() == 44 && n > 0) {
            //drawBox(images.get(n));
            n--;
        } else if (event.getKeyCode() == 46 && n < images.size() - 1) {
            //drawBox(images.get(n));
            n++;
        }
    }

    private void drawBox(PImage pic) {
        nya_l.detect(pic);
        background(0);
        nya_l.drawBackground(pic);//frustumを考慮した背景描画

        for (int i = 0; i < 1; i++) {
            if ((!nya_l.isExist(i))) {
                continue;
            }
            nya_l.beginTransform(i);
            fill(0, 255, 0);
            drawgrid();
            fill(100 * (((i + 1) / 4) % 2), 100 * (((i + 1) / 2) % 2), 100 * (((i + 1)) % 2));
            translate(0, 0, 20);
            box(40);
            nya_l.endTransform();
        }
    }

    private void checkWindowResised() {
        if (w != width && h != height) {
            w = width;
            h = height;

            xScene = w / 2;
            yScene = h / 2;

            println("resised " + w + " " + h);
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getX() >= xScene - 40 && event.getX() <= xScene + 40 &&
                event.getY() >= yScene - 40 && event.getY() <= yScene + 40) {
            isMovingMode = true;
        }

        println(fov);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        isMovingMode = false;
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (isMovingMode) {
            xScene = event.getX();
            yScene = event.getY();
        }
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        if (e > 0) {
            fovVal -= 0.1;
            //scaleVal -= 1;
        } else {
            fovVal += 0.1;
            //scaleVal += 1;
        }
        println("scaleVal = " + scaleVal);
    }

    @Override
    public void settings() {
        size(1280, 1024, P3D);
    }

    public static void main(String[] args) {
        NyArApp nyArApp = new NyArApp();
        nyArApp.main(nyArApp.getClass().getName());
    }
}
