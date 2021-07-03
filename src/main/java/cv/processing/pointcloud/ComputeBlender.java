package cv.processing.pointcloud;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PGL;
import processing.opengl.PShader;
import cv.processing.pointcloud.utils.FileChooserTest;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComputeBlender extends PApplet {

    PGL pgl;
    PShader sh;

    int vertLoc;

    //VBO buffer location in the GPU
    int vertexVboId;

    //transformations
    float a = 7;
    float b = 0;
    int zval = -50;
    //float scaleVal = 220;
    float scaleVal = 480;

    //int vertData = 512*424*3;
//int vertData = 119756*3; //count of points*3 by frame
//    int vertData = 217088 * 3;
    int vertData = 0;

    FloatBuffer pointCloudBuffer;

    List<FloatBuffer> pointCloudList = new ArrayList();
    int frameNumber = 0;

    float xScene, yScene;

    String sketchPath;

    float xCoordFrameBar;
    float yCoordFrameBar;
    float frameBarLength;
    float frameCoordPosition;
    float frameStep;
    float shiftX;
    float shiftY;
    float shiftZ;

    float w;
    float h;

    String resFolderName;

    boolean isOrtho;
    float orthoNear;
    float orthoFar;

    boolean isMovingMode;

    boolean isShowUI;

    float fov;

    int k = 1;

    int pixelStart = 727;
    double angle = 0;
    float angleRad = 0;
    float angleDeg = 90;
    float angleShift = 1;
    int shift = 0;
    List<Float> listFromImages = new ArrayList<>();
    List<Integer> indList = new ArrayList<>();

    public static void main(String[] args) {
        ComputeBlender computeBlender = new ComputeBlender();
        computeBlender.main(computeBlender.getClass().getName());
    }

    public void setup() {

        //PC
        sketchPath = sketchPath();
        resFolderName = "frames";

        println(sketchPath);

        //android
        //sketchPath = "/data/data/processing.test.pointcloud/files/frames";

        //reader = createReader("positions_500.txt");

        isShowUI = true;

        shiftX = 0;
        shiftY = 0;
        shiftZ = 1;

        fov = PI / 3.0f;

        orthoNear = 0.0f;
        orthoFar = 10.0f;

//        File file = openFolder();
//        initPointCloudList(file);
//        initPointCloudList(new File("F:\\Processing\\sketch_210330_2a\\rotate_with_plane"));
//        initPointCloudList(new File(sketchPath() + "\\res\\rotate_cube_360"));
//        initPointCloudList(new File(sketchPath() + "\\res\\rotate_cube_360_shift"));
//        initPointCloudList(new File(sketchPath() + "\\res\\rotate_deer_360"));
//        initPointCloudList(new File("C:\\tmp"));
        initPointCloudList(new File(sketchPath() + "\\res\\deer\\720"));
//        initPointCloudFromFile(new File(sketchPath() + "\\res\\rotate_cube_360_points.txt"));
        initFrameBar();
//        initPointCloudList(new File(sketchPath + "\\res\\" + resFolderName));
//        initPointCloudList(new File("F:\\Processing\\sketch_210330_3a_reader\\data"));

        sh = loadShader("/res/frag.glsl", "/res/vert.glsl");
        PGL pgl = beginPGL();
        IntBuffer intBuffer = IntBuffer.allocate(1);
        pgl.genBuffers(1, intBuffer);
        //memory location of the VBO
        vertexVboId = intBuffer.get(0);
        endPGL();

        frameRate(30);
    }

    public void draw() {
        background(0);
        surface.setTitle("" + frameRate);
//        surface.setTitle("" + fov);
        checkWindowResised();

//        ortho(-width / 2, width / 2, -height / 2, height / 2, orthoNear, orthoFar);


        if (keyPressed) {
            if (keyCode == LEFT) {
                a += 0.05;
            } else if (keyCode == RIGHT) {
                a -= 0.05;
            } else if (keyCode == UP) {
                b += 0.05;
            } else if (keyCode == DOWN) {
                b -= 0.05;
            }
        }

        //increase FPS idoono why
        if (isShowUI) {
            drawMoveRect();
        }

        pushMatrix();
        translate(xScene, yScene, zval);
        scale(scaleVal, -1 * scaleVal, scaleVal);

        rotate(a, 0.0f, 1.0f, 0.0f);
        rotate(b, 1.0f, 0.0f, 0.0f);

        //increase FPS idoono why
        if (isShowUI) {
            drawPurpleBox();
        }

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
        popMatrix();
    }

    private void drawPurpleBox() {
        stroke(100, 0, 100);
        strokeWeight(0.005f);
        noFill();
        //box(0.25);
        box(3);
        noStroke();
        fill(255);
    }

    private void drawMoveRect() {
        fill(200);
        rect(xScene - 40, yScene - 40, 80, 80);
    }

    private void initFrameBar() {
        xScene = width / 2;
        yScene = height / 2;

        w = width;
        h = height;

        isMovingMode = false;
    }

    private FloatBuffer floatBufferFromArray(Float[] f) {
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

    private void initPointCloudFromFile(File filename) {
        BufferedReader reader = createReader(filename.getAbsolutePath());
        List<Float> res = new ArrayList<>();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                String[] pieces = split(line, ' ');
                float x = Float.parseFloat(pieces[0]);
                float y = Float.parseFloat(pieces[1]);
//                println(x + " " + y);

//                angleRad = angleDeg * PI / 180;
//                float r = x * 3 - 2000;
//                float x1 = r * cos(angleRad);
//                float z1 = r * sin(angleRad);
//
//                res.add(x1 / 250);
//                res.add(((float) -y + 500) / 250);
//                res.add(z1 / 250);
//
//                listFromImages.add((float) x);
//                listFromImages.add((float) y);
//
//                vertData++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        FloatBuffer floatBuffer = null;
//        ArrayList<Float> pointCloudFloatList = new ArrayList<>();
//        floatBuffer = getFloatBufferFromList(pointCloudFloatList);
//
//        pointCloudBuffer = floatBuffer;
//        vertData = vertData * 3;
    }

    private void initPointCloudList(File folderName) {
        k = 0;
        long st = System.nanoTime();
        println("Listing all filenames in a directory: ");

        FloatBuffer floatBuffer = null;
        ArrayList<Float> pointCloudFloatList = new ArrayList<>();
        shift = 0;
        angleShift = 360.0f / (float) folderName.listFiles().length;

        for (File file : folderName.listFiles()) {
            if (!file.isDirectory()) {
                println(file.getName());

                pointCloudFloatList.addAll(getFloatListFromFile(file));

                surface.setTitle("" + pointCloudList.size());
            }
        }


        saveFloatList(pointCloudFloatList);
//        savePoints(listFromImages);
        floatBuffer = getFloatBufferFromList(pointCloudFloatList);

        pointCloudBuffer = floatBuffer;
        vertData = vertData * 3;

        long en = System.nanoTime();
        long res = (en - st) / 1000000000;
        System.out.println("seconds = " + res);
        System.out.println("angleShift = " + angleShift);

    }

    private FloatBuffer getFloatBufferFromList(List<Float> list) {
        Float[] tmpFloatArray;
        tmpFloatArray = list.toArray(new Float[0]);

        FloatBuffer floatBuffer = floatBufferFromArray(tmpFloatArray);
        return floatBuffer;
    }

    private void savePoints(List<Float> list) {
        String[] res = new String[list.size() / 2];

        int i = 0;
        Iterator<Float> iterator = list.iterator();
        while (iterator.hasNext()) {
            String x = String.format("%s", iterator.next());
            String y = String.format("%s", iterator.next());

            res[i] = "" + x + " " + y;
            i++;
        }

        saveStrings(sketchPath() + "\\res\\rotate_cube_360_points.txt", res);
    }

    private void saveFloatList(List<Float> list) {
        String[] res = new String[list.size() / 3];

        int i = 0;
        Iterator<Float> iterator = list.iterator();
        while (iterator.hasNext()) {
//            String x = String.format("%s", iterator.next() * 100000);
            String x = String.format("%s", iterator.next());
            String y = String.format("%s", iterator.next());
            String z = String.format("%s", iterator.next());

            res[i] = "v " + x + " " + y + " " + z;
            i++;
        }

        saveStrings(sketchPath() + "\\res\\deer_720.obj", res);
    }

    private List<Float> getFloatListFromFile(File file) {
        List<Float> res = new ArrayList<>();
        PImage pic = loadImage(file.getAbsolutePath());
//        PImage picTmp = loadImage(file.getAbsolutePath());
//        PImage pic = getFlippedImage(picTmp);

        for (int i = 0; i < 727; i++) {
            for (int j = 0; j < pic.height; j++) {
                int c = pic.get(i, j);

                int c1 = -7000000;

                if (c > c1) {
                    float r = (i - 727) * 3.2f;
                    float x1 = r * cos((angleDeg + 360) * PI / 180);
                    float z1 = r * sin((angleDeg + 360) * PI / 180);

                    res.add(x1 / 250);
                    res.add(((float) -j + 500) / 250);
                    res.add(z1 / 250);

                    vertData++;
                }
            }
        }

        for (int i = 727; i < pic.width; i++) {
            for (int j = 0; j < pic.height; j++) {
                int c = pic.get(i, j);

                int c1 = -7000000;

                if (c > c1) {
                    angleRad = angleDeg * PI / 180;
//                    float r = i * 3 - 2000;
                    float r = (i - 727) * 3.2f;
                    float x1 = r * cos(angleRad);
                    float z1 = r * sin(angleRad);

                    res.add(x1 / 250);
                    res.add(((float) -j + 500) / 250);
                    res.add(z1 / 250);

                    listFromImages.add((float) i);
                    listFromImages.add((float) j);

                    vertData++;
                }
            }
        }
        shift = shift + 30;
//        angleDeg = angleDeg + 1;
        angleDeg = angleDeg + angleShift;

        return res;
    }

    private FloatBuffer getFloatBufferFromFile(File fileName) {
        FloatBuffer floatBuffer = null;
        BufferedReader bufferedReader = createReader(fileName.getAbsolutePath());
        ArrayList<Float> tmpFloatList = new ArrayList();

        int i = 0;
        String line = null;
        int j = 1;

        try {
            while ((line = bufferedReader.readLine()) != null) {

//                if (j % 2 == 0) {
//                    j = 1;
//                    if ((line = bufferedReader.readLine()) == null) {
//                        break;
//                    }
//                    continue;
//                }

                float xr = 0;
                float yr = 0;
                float zr = 0;

//                if (k % 3 == 0) {
//                    xr = random(-0.05f, 0.05f);
//                    yr = random(-0.05f, 0.05f);
//                    zr = random(-0.05f, 0.05f);
//                }

                String[] pieces = split(line, ' ');

                float xi = Float.parseFloat(pieces[0]) - shiftX;
                float yi = Float.parseFloat(pieces[1]) - shiftY;
                float zi = Float.parseFloat(pieces[2]) - shiftZ;

                if (xi != 0.0f && yi != 0.0f && zi != 0.0f) {
                    tmpFloatList.add(xi * 2 + xr);
                    tmpFloatList.add(yi * 2 + yr);
                    tmpFloatList.add((zi - 1) * 2 + zr);
                    i++;
                }
//                tmpFloatList.add(xi / 200 + xr);
//                tmpFloatList.add(yi / 200 + yr);
//                tmpFloatList.add(zi / 200 + zr);
//                i++;
                j++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Float[] tmpFloatArray;
        tmpFloatArray = tmpFloatList.toArray(new Float[0]);

        floatBuffer = floatBufferFromArray(tmpFloatArray);

        return floatBuffer;
    }


    private FloatBuffer getFloatBufferFromList() {
        frameNumber++;

        if (frameNumber >= pointCloudList.size()) {
            frameNumber = 0;
            //println("1");
        }
        //println("frameNumber = " + frameNumber);

        return pointCloudList.get(frameNumber);
    }

    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        if (e > 0) {
            scaleVal -= 10;
        } else {
            scaleVal += 10;
        }
        println("scaleVal = " + scaleVal);
    }

    public void mouseDragged(MouseEvent event) {
        if (isMovingMode) {
            xScene = event.getX();
            yScene = event.getY();
        }

    }

    private void setOrtho() {
        if (isOrtho) {
            isOrtho = false;
        } else {
            isOrtho = true;
        }

        if (isOrtho) {
            float cameraZ = (height / 2.0f) / tan(fov / 2.0f);
            perspective(fov, (float) width / (float) height, cameraZ / 20.0f, cameraZ * 20.0f);
        } else {
//            ortho(-width / 2, width / 2, -height / 2, height / 2);
            ortho(-width / 2, width / 2, -height / 2, height / 2, -5000, 5000);
        }
    }

    private void checkWindowResised() {
        if (w != width && h != height) {
            w = width;
            h = height;

            xScene = w / 2;
            yScene = h / 2;

            yCoordFrameBar = h * 0.9f;
            frameBarLength = w * 0.8f;

            xCoordFrameBar = frameCoordPosition;
            frameStep = frameBarLength / (pointCloudList.size() - 1);
            println("resised " + w + " " + h);
        }
    }

    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 32) {

        } else if (event.getKeyCode() == 44) {
            if (frameNumber > 0) {
                frameNumber--;
                frameCoordPosition -= frameStep;
            }
        } else if (event.getKeyCode() == 46) {
            if (frameNumber < pointCloudList.size() - 1) {
                frameNumber++;
                frameCoordPosition += frameStep;
            }
        } else if (event.getKeyCode() == 79) {
            //'O'
            setOrtho();
        } else if (event.getKeyCode() == 73) {
            //'I'
            showUI();
        } else if (event.getKeyCode() == 70) {
            //'F'
            File file = openFolder();
//            resFolderName = file.getName();

//            vertDataList.clear();
//            pointCloudList.clear();

//            initPointCloudList(resFolderName);
            if (file != null) {
                frameNumber = 0;
                initPointCloudList(file);
            }
        } else if (event.getKeyCode() == 45) {
            //'-'
            orthoNear -= 10;
            println("orthoNear = " + orthoNear);
        } else if (event.getKeyCode() == 61) {
            //'+'
            orthoNear += 10;
            println("orthoNear = " + orthoNear);
        } else if (event.getKeyCode() == 91) {
            //'['
            orthoFar -= 10;
            println("orthoFar = " + orthoFar);
        } else if (event.getKeyCode() == 93) {
            //']'
            orthoFar += 10;
            println("orthoFar = " + orthoFar);
        } else if (event.getKeyCode() == 71) {
            //'G'

        } else if (event.getKeyCode() == 75) {
            fov += 0.01;
        } else if (event.getKeyCode() == 76) {
            fov -= 0.01;
        }

        println("event.getKeyCode() = " + event.getKeyCode());
        println("frameNumber = " + frameNumber);
    }

    private void showUI() {
        if (isShowUI) {
            isShowUI = false;
        } else {
            isShowUI = true;
        }
    }


    public void mousePressed(MouseEvent event) {
        if (event.getX() >= xScene - 40 && event.getX() <= xScene + 40 &&
                event.getY() >= yScene - 40 && event.getY() <= yScene + 40) {
            isMovingMode = true;
//            println("isMovindMode on");
        }
    }

    public void mouseReleased(MouseEvent event) {
        isMovingMode = false;
//        println("isMovindMode off");
    }

    private File openFolder() {
        System.out.println("open folder");

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Локализация компонентов окна JFileChooser
        UIManager.put(
                "FileChooser.saveButtonText", "Сохранить");
        UIManager.put(
                "FileChooser.cancelButtonText", "Отмена");
        UIManager.put(
                "FileChooser.fileNameLabelText", "Наименование файла");
        UIManager.put(
                "FileChooser.filesOfTypeLabelText", "Типы файлов");
        UIManager.put(
                "FileChooser.lookInLabelText", "Директория");
        UIManager.put(
                "FileChooser.saveInLabelText", "Сохранить в директории");
        UIManager.put(
                "FileChooser.folderNameLabelText", "Путь директории");

        JFrame jFrame = new JFrame();
        jFrame.setTitle("open frame folder");
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setDialogTitle("open frame folder");
        fileChooser.setCurrentDirectory(new File(sketchPath + "/res/"));

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(jFrame);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();

        } else {
            return null;
        }
    }

    private PImage getFlippedImage(PImage img) {
        PImage flipped = createImage(img.width, img.height, RGB);//create a new image with the same dimensions
        for (int i = 0; i < flipped.pixels.length; i++) {       //loop through each pixel
            int srcX = i % flipped.width;                        //calculate source(original) x position
            int dstX = flipped.width - srcX - 1;                     //calculate destination(flipped) x position = (maximum-x-1)
            int y = i / flipped.width;                        //calculate y coordinate
            flipped.pixels[y * flipped.width + dstX] = img.pixels[i];//write the destination(x flipped) pixel based on the current pixel
        }
        //y*width+x is to convert from x,y to pixel array index
        flipped.updatePixels();
        return flipped;
    }

    public void settings() {
        size(1024, 768, P3D);
    }

}