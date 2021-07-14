package cv.processing.nyar;

import KinectPV2.KinectPV2;
import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.io.File;

public class ArKinect extends PApplet {

    private MultiMarker nya;
    private KinectPV2 kinect;
    private PImage src;
    private PImage res;

    //supported resuolution:
    //works coorect only with 4:3 ratio
    //640x480
    //800x600
    //1024x768
    //1200x900
    private int widthRes = 1024;
    private int heightRes = 768;
//    int widthRes = 512 * 2;
//    int heightRes = 424 * 2;

    @Override
    public void setup() {

        colorMode(RGB, 100);
        println(MultiMarker.VERSION);

        res = createImage(widthRes, heightRes, RGB);

        kinect = new KinectPV2(this);

        kinect.enableColorImg(true);

        System.out.println("sketchPath() = " + sketchPath());
//        nya = new MultiMarker(this, width, height, "../../data/camera_para.dat", NyAR4PsgConfig.CONFIG_PSG);

        File file = new File(sketchPath() + "\\src\\main\\resources\\data\\camera_para.dat");
        System.out.println(file.getAbsolutePath());

        nya = new MultiMarker(this, width, height, file.getAbsolutePath(), NyAR4PsgConfig.CONFIG_PSG);
        nya.addNyIdMarker(0, 80);//id=2

        kinect.init();

    }

    @Override
    public void draw() {
        surface.setTitle("" + frameRate);

        src = mirrorImage(kinect.getColorImage());
        //crop image to
//        cam2.copy(cam1, CNTR_X - x / 2, CNTR_Y - y / 2, x, y, 0, 0, x, y);
        res.copy(src, src.width / 2 - widthRes / 2, src.height / 2 - heightRes / 2,
                widthRes, heightRes, 0, 0, widthRes, heightRes);

        nya.detect(res);
        background(0);
        nya.drawBackground(res);

        for (int i = 0; i < 1; i++) {
            if ((!nya.isExist(i))) {
                continue;
            }
            nya.beginTransform(i);
            fill(100 * ((((float) i + 1) / 4) % 2), 100 * ((((float) i + 1) / 2) % 2), 100 * ((((float) i + 1)) % 2));
            translate(0, 0, 20);
//            box(40);
            nya.endTransform();
        }

//        stroke(255, 0, 0);
//        strokeWeight(1);
//        line(width >> 1, 0, width >> 1, height);
//        line(0, height >> 1, width, height >> 1);
    }

    @Override
    public void settings() {
        size(widthRes, heightRes, P3D);
    }

    void drawgrid() {
        pushMatrix();
        stroke(0);
        strokeWeight(2);
        line(0, 0, 0, 100, 0, 0);
        text("X", 100, 0, 0);
        line(0, 0, 0, 0, 100, 0);
        text("Y", 0, 100, 0);
        line(0, 0, 0, 0, 0, 100);
        text("Z", 0, 0, 100);
        popMatrix();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 32) {
            saveFrame("1.jpg");
            System.out.println("save 1.jpg");
        }
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        super.mouseDragged(event);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        super.mousePressed(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        super.mouseReleased(event);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        super.mouseWheel(event);
    }

    public static void main(String[] args) {
        ArKinect arKinect = new ArKinect();
        arKinect.main(arKinect.getClass().getName());
    }

    private PImage mirrorImage(PImage src) {
        PImage resultPImage = createImage(src.width, src.height, RGB);//create a new image with the same dimensions
        for (int i = 0; i < resultPImage.pixels.length; i++) {       //loop through each pixel
            int srcX = i % resultPImage.width;                        //calculate source(original) x position
            int dstX = resultPImage.width - srcX - 1;                     //calculate destination(flipped) x position = (maximum-x-1)
            int y = i / resultPImage.width;                        //calculate y coordinate
            resultPImage.pixels[y * resultPImage.width + dstX] = src.pixels[i];//write the destination(x flipped) pixel based on the current pixel
        }
        resultPImage.updatePixels();
        return resultPImage;
    }
}
