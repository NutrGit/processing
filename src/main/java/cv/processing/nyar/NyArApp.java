package cv.processing.nyar;

import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import jp.nyatla.nyartoolkit.markersystem.NyARMarkerSystem;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.KeyEvent;

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


    private MultiMarker nya_l;
    private ArrayList<PImage> images = new ArrayList();
    private NyARMarkerSystem markerSystem;

    private float rotX = 0;
    private float a = 7;
    private float c = 0.1f;

    private int n = 0;

    @Override
    public void setup() {
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

        colorMode(RGB, 100);
        println(MultiMarker.VERSION);

        File fileMaker = new File("src\\main\\resources\\data\\camera_para.dat");

        nya_l = new MultiMarker(this, width, height, fileMaker.getAbsolutePath(),
                new NyAR4PsgConfig(NyAR4PsgConfig.CS_LEFT_HAND, NyAR4PsgConfig.TM_NYARTK));

        nya_l.addNyIdMarker(0, 80);
        markerSystem = nya_l.get_ms();

        System.out.println("NyaAr PApplet = " + this);

        frameRate(9999);
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

        if (keyPressed) {
            if (keyCode == LEFT) {
                a += 0.05;
            } else if (keyCode == RIGHT) {
                a -= 0.05;
            }
        }

        compute();
    }

    private void compute() {
        PImage pic1 = images.get(n);
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
//            fill(100 * ((((float) i + 1) / 4) % 2), 100 * ((((float) i + 1) / 2) % 2), 100 * (((i + 1)) % 2));
//            rotate(c);
//            translate(0, 0, 20);
//            box(40);
            nya_l.endTransform();

            fill(150, 0, 150);
            PVector[] pVectors = nya_l.getMarkerVertex2D(i);
            for (PVector vector : pVectors) {
                circle(vector.x, vector.y, 10);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
//        println(event.getKeyCode());
        if (event.getKeyCode() == 37) {
            rotX++;
            println("rotX = " + rotX);
        } else if (event.getKeyCode() == 39) {
            rotX--;
            println("rotX = " + rotX);
        } else if (event.getKeyCode() == 10) {
            //enter
            System.out.println();

            PVector[] pVectors = nya_l.getMarkerVertex2D(0);
            for (PVector vector : pVectors) {
                System.out.println("vector.x = " + vector.x + " vector.y = " + vector.y);
            }


        } else if (event.getKeyCode() == 44 && n > 0) {
            //drawBox(images.get(n));
            n--;
        } else if (event.getKeyCode() == 46 && n < images.size() - 1) {
            //drawBox(images.get(n));
            n++;
        }
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
