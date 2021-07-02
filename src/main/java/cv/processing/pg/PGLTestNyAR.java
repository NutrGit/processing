package cv.processing.pg;

import jp.nyatla.nyar4psg.MultiMarker;
import jp.nyatla.nyar4psg.NyAR4PsgConfig;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PGL;
import processing.opengl.PShader;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class PGLTestNyAR extends PApplet {

    private PGL pgl;
    private PShader sh;
    private FloatBuffer pointCloudBuffer;
    private ArrayList<PImage> images = new ArrayList();
    private MultiMarker nya_l;

    private int vertData;
    private int vertexVboId;
    private int zval = -50;
    private int vertLoc;
    private int n = 0;

    private float xScene;
    private float yScene;
    private float w;
    private float h;
    private float a = 7;
    private float scaleVal = 140;
    private float c = 0;

    private boolean isShowUI = true;
    private boolean isMovingMode = false;
    private boolean isOrtho = false;

    public static void main(String[] args) {
        PGLTestNyAR pglTest = new PGLTestNyAR();
        pglTest.main(pglTest.getClass().getName());
    }

    @Override
    public void settings() {
        size(1280, 1024, P3D);
    }

    @Override
    public void setup() {

        xScene = width / 2;
        yScene = height / 2;

        w = width;
        h = height;
        vertData = 0;

        pointCloudBuffer = getRandomPointCloud();

        sh = loadShader(new File("src\\main\\resources\\res\\frag.glsl").getAbsolutePath(),
                new File("src\\main\\resources\\res\\vert.glsl").getAbsolutePath());
        PGL pgl = beginPGL();
        IntBuffer intBuffer = IntBuffer.allocate(1);
        pgl.genBuffers(1, intBuffer);
        //memory location of the VBO
        vertexVboId = intBuffer.get(0);
        endPGL();

        File folderName = new File("C://Users//UserCV//Documents//Processing//libraries//nyar4psg//data//1");
        println(folderName.getAbsolutePath());
        File[] fileNames = folderName.listFiles();
        for (File f : fileNames) {
            PImage img = loadImage(f.getAbsolutePath());
            images.add(img);
        }

//        colorMode(RGB, 100);
        println(MultiMarker.VERSION);


        nya_l = new MultiMarker(this, width, height,
                "C://Users//UserCV//Documents//Processing//libraries//nyar4psg//data//camera_para.dat",
                new NyAR4PsgConfig(NyAR4PsgConfig.CS_LEFT_HAND, NyAR4PsgConfig.TM_NYARTK));
        nya_l.addNyIdMarker(0, 80);

//        frameRate(30);
    }

    @Override
    public void draw() {
        background(0);
        surface.setTitle("" + frameRate);
        c += 0.01;

        checkWindowResised();

        if (keyPressed) {
            if (keyCode == LEFT) {
                a += 0.05;
            } else if (keyCode == RIGHT) {
                a -= 0.05;
            }
        }

//        if (isShowUI) drawMoveRect();

        nya_l.detect(images.get(n));
        background(0);
        nya_l.drawBackground(images.get(n));//frustumを考慮した背景描画


        for (int i = 0; i < 1; i++) {
            if ((!nya_l.isExist(i))) {
                continue;
            }
            nya_l.beginTransform(i);
            fill(0, 255, 0);
//            drawgrid();
            if (isShowUI) drawPurpleBox();
            fill(100 * (((i + 1) / 4) % 2), 100 * (((i + 1) / 2) % 2), 100 * (((i + 1)) % 2));
            rotate(c);
            translate(0, 0, 20);

            drawPGL();

//            box(40);
            nya_l.endTransform();
        }


//        translate(xScene, yScene, zval);
//        scale(scaleVal, -1 * scaleVal, scaleVal);
//        rotate(a, 0.0f, 1.0f, 0.0f);
//
//        if (isShowUI) drawPurpleBox();
//
//        drawPGL();
    }

    private void drawgrid() {
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

    private void drawPGL() {
//        pointCloudBuffer = getFloatBufferFromList();

        pgl = beginPGL();
        sh.bind();
        vertLoc = pgl.getAttribLocation(sh.glProgram, "vertex");
        pgl.enableVertexAttribArray(vertLoc);

        //vertex
        {
            pgl.bindBuffer(PGL.ARRAY_BUFFER, vertexVboId);
            pgl.bufferData(PGL.ARRAY_BUFFER, Float.BYTES * vertData, pointCloudBuffer, PGL.DYNAMIC_DRAW);
            pgl.vertexAttribPointer(vertLoc, 3, PGL.FLOAT, false, Float.BYTES * 3, 0);
        }

        // unbind VBOs
        pgl.bindBuffer(PGL.ARRAY_BUFFER, 0);
        pgl.drawArrays(PGL.POINTS, 0, vertData);
        pgl.disableVertexAttribArray(vertLoc);

        sh.unbind();
        endPGL();
    }

    private FloatBuffer getRandomPointCloud() {
        FloatBuffer floatBuffer = null;
        List<Float> floatList = new ArrayList<>();

        for (int i = 0; i < 900; i++) {
            floatList.add(random(0, 20));
            floatList.add(random(0, 20));
            floatList.add(random(0, 20));
            vertData++;
        }

        vertData = vertData * 3;

        floatBuffer = floatBufferFromArray(floatList.toArray(new Float[0]));
        return floatBuffer;
    }

    private static FloatBuffer floatBufferFromArray(Float[] f) {
        float[] array = new float[f.length];
        for (int i = 0; i < f.length; i++) {
            array[i] = f[i];
        }

        ByteBuffer byteBuf = ByteBuffer.allocateDirect(array.length * Float.BYTES); //4 bytes per float
        byteBuf.order(ByteOrder.nativeOrder());

        FloatBuffer buffer = byteBuf.asFloatBuffer();
        buffer.put(array);
        buffer.position(0);

        return buffer;
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

    private void drawMoveRect() {
        fill(200);
        rect(xScene - 40, yScene - 40, 80, 80);
    }

    private void drawPurpleBox() {
        stroke(100, 0, 100);
        strokeWeight(1.0f);
        noFill();
        //box(0.25);
        box(30);
        noStroke();
        fill(255);
    }

    private void setOrtho() {
        if (isOrtho) {
            isOrtho = false;
        } else {
            isOrtho = true;
        }

        if (isOrtho) {
            float fov = PI / 3.0f;
            float cameraZ = (height / 2.0f) / tan(fov / 2.0f);
            perspective(fov, (float) width / (float) height, cameraZ / 20.0f, cameraZ * 20.0f);
        } else {
            ortho(-width / 2, width / 2, -height / 2, height / 2);
        }
    }

    private void showUI() {
        if (isShowUI) {
            isShowUI = false;
        } else {
            isShowUI = true;
        }
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        if (e > 0) {
            scaleVal -= 10;
        } else {
            scaleVal += 10;
        }
        println("scaleVal = " + scaleVal);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        if (isMovingMode) {
            xScene = event.getX();
            yScene = event.getY();
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (event.getX() >= xScene - 40 && event.getX() <= xScene + 40 &&
                event.getY() >= yScene - 40 && event.getY() <= yScene + 40) {
            isMovingMode = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        isMovingMode = false;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 32) {
            //space
        } else if (event.getKeyCode() == 44) {
            //<
            if (n > 0) {
                n--;
            }
        } else if (event.getKeyCode() == 46) {
            //>
            if (n < images.size() - 1) {
                n++;
            }
        } else if (event.getKeyCode() == 79) {
            //'O'
            setOrtho();
        } else if (event.getKeyCode() == 73) {
            //'I'
            showUI();
        } else if (event.getKeyCode() == 70) {
            //'F'
//            openFolder();
        } else if (event.getKeyCode() == 71) {
            //'G'
//            resFolderName = FileChooserTest.getFolderName();
//            pointCloudList.clear();
//            vertDataList.clear();
//            initPointCloudList(resFolderName);
        }

        println("event.getKeyCode() = " + event.getKeyCode());

    }

}
