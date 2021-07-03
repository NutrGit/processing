package cv.processing.nyar;


import processing.core.PApplet;
import processing.core.PMatrix3D;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PGL;
import processing.opengl.PShader;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class NyArStatic extends PApplet {

    PGL pgl;
    PShader sh;

    private PMatrix3D matrix3D;
    float[] floats;

    int vertLoc;

    //VBO buffer location in the GPU
    int vertexVboId;

    //transformations
    float a = 7;
    int zval = -50;
    //float scaleVal = 220;
    float scaleVal = 480;

    //int vertData = 512*424*3;
//int vertData = 119756*3; //count of points*3 by frame
    int vertData = 217088 * 3;
    List<Integer> vertDataList = new ArrayList();

    FloatBuffer pointCloudBuffer;

    List<FloatBuffer> pointCloudList = new ArrayList();
    int frameNumber = 0;

    float xScene = width >> 1, yScene;

    String sketchPath;

    boolean pause;
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
    boolean isMovingMode;
    boolean isShowUI;

    float fov;

    int k = 1;

    public void setup() {

        sketchPath = sketchPath();
//        resFolderName = "7_3_12_35_";
        resFolderName = "7_3_12_35_";

        println(sketchPath);


        isShowUI = true;

        shiftX = 0;
        shiftY = 0;
        shiftZ = 1;

        fov = PI / 3.0f;

        initFrameBar();
        File folder = new File("src\\main\\resources\\data\\txt");

        initPointCloudList(folder);

        sh = loadShader(new File("src\\main\\resources\\res\\frag.glsl").getAbsolutePath(),
                new File("src\\main\\resources\\res\\vert.glsl").getAbsolutePath());
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
//        surface.setTitle("" + frameRate);
//        surface.setTitle("" + fov);
        checkWindowResised();


        if (keyPressed) {
            if (keyCode == LEFT) {
                a += 0.05;
            } else if (keyCode == RIGHT) {
                a -= 0.05;
            }
        }

        if (pause) {
            drawFrameBar();
        }

        //increase FPS idoono why
        if (isShowUI) {
            drawMoveRect();
        }

//        fov = PI / ((float)mouseX/100);


        translate(xScene, yScene, zval);
        scale(scaleVal, -1 * scaleVal, scaleVal);

        if (floats == null) {
            rotate(a, 0.0f, 1.0f, 0.0f);
        } else {
//            rotateX(PI/2 - floats[0]);
//            rotateY(-floats[1]);
//            rotateZ(floats[2]);
        }

        //increase FPS idoono why
        if (isShowUI) {
            drawPurpleBox();
//            drawGrid();
//            drawFov();
        }

        pointCloudBuffer = getFloatBufferFromList();

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
        xScene = width >> 1;
        yScene = height >> 1;

        w = width;
        h = height;

        xCoordFrameBar = width * 0.1f;
        yCoordFrameBar = height * 0.9f;
        frameBarLength = width * 0.8f;
        frameCoordPosition = width * 0.1f;

        yCoordFrameBar = h * 0.9f;
        frameBarLength = w * 0.8f;

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

    private void initPointCloudList(File folderName) {
        String path = folderName.getAbsolutePath();
        System.out.println(path);

        println("Listing all filenames in a directory: ");
        String[] filenames = listFileNames(path);

        k = 0;
        printArray(filenames);

        long st = System.nanoTime();

        for (String s : filenames) {
            if (k == 3) {
                k = 0;
            }
            String filePath = folderName + "\\" + s;
            println(filePath);
            File file = new File(folderName.getAbsolutePath() + "\\" + s);
//            pointCloudList.add(getFloatBufferFromFile(sketchPath + "/res/" + filePath));
            pointCloudList.add(getFloatBufferFromFile(file));
            surface.setTitle("" + pointCloudList.size());
            k++;
        }
        long en = System.nanoTime();
        long res = (en - st) / 1000000000;
        System.out.println("seconds = " + res);


        frameStep = frameBarLength / (pointCloudList.size() - 1);
        println("pointCloudList.size() = " + pointCloudList.size());
    }

    private FloatBuffer getFloatBufferFromFile(File fileName) {
        FloatBuffer floatBuffer = null;
        BufferedReader bufferedReader = createReader(fileName);
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

//                float xi = Float.parseFloat(pieces[0]) - shiftX;
//                float yi = Float.parseFloat(pieces[1]) - shiftY;
//                float zi = Float.parseFloat(pieces[2]) - shiftZ;
//
//                if (xi != 0.0f && yi != 0.0f && zi != 0.0f) {
//                    tmpFloatList.add(xi * 2 + xr);
//                    tmpFloatList.add(yi * 2 + yr);
//                    tmpFloatList.add((zi - 1) * 2 + zr);
//                    i++;
//                }
//                j++;

                float xi = Float.parseFloat(pieces[0]);
                float yi = Float.parseFloat(pieces[1]);
                float zi = Float.parseFloat(pieces[2]);

                if (xi != 0.0f && yi != 0.0f && zi != 0.0f) {
                    tmpFloatList.add(xi);
                    tmpFloatList.add(yi);
                    tmpFloatList.add(zi);
                    i++;
                }
                j++;


            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        println("lines = " + i);
        vertDataList.add(i);

        Float[] tmpFloatArray;
        tmpFloatArray = tmpFloatList.toArray(new Float[0]);

        floatBuffer = floatBufferFromArray(tmpFloatArray);

        return floatBuffer;
    }

    private String[] listFileNames(String dir) {

        File file = new File(dir);
        if (file.isDirectory()) {
            String names[] = file.list();
            return names;
        } else {
            // If it's not a directory
            return null;
        }
    }

    private FloatBuffer getFloatBufferFromList() {

        if (!pause) {
            frameNumber++;

            if (frameNumber >= pointCloudList.size()) {
                frameNumber = 0;
                //println("1");
            }
            //println("frameNumber = " + frameNumber);
            vertData = vertDataList.get(frameNumber) * 3;
        }

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

    private void checkPause() {
        if (pause) {
            pause = false;
//            frameRate(30);
            println("resume");
        } else {
            pause = true;
//            frameRate(144);
//            frameRate(5);
            println("pause");
        }
    }

    private void drawGrid() {
        stroke(100, 0, 100);
        strokeWeight(0.01f);
        for (int i = -9; i < 9; i++) {
            line(-10, -1.5f, i, 10, -1.5f, i);
        }
    }

    private void drawFov() {
        stroke(100, 100, 100);
        strokeWeight(0.01f);
        line(0, 0, 0, 1, 1, 1);
    }

    private void setOrtho() {
        if (isOrtho) {
            isOrtho = false;
        } else {
            isOrtho = true;
        }

        if (isOrtho) {
            float cameraZ = (height / 2.0f) / tan(fov / 2.0f);
//            perspective(fov, (float) width / (float) height, cameraZ / 20.0f, cameraZ * 20.0f);
            perspective(2.2f, (float) width / (float) height, cameraZ / 20.0f, cameraZ * 20.0f);
        } else {
            ortho(-width / 2, width / 2, -height / 2, height / 2);
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
            checkPause();
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
            openFolder();
        } else if (event.getKeyCode() == 75) {
            //'K'
            System.out.println("get matrix from NyArApp");
            matrix3D = NyArApp.getMatrix3D();
            matrix3D.print();
            System.out.println(matrix3D);
        } else if (event.getKeyCode() == 76) {
            //'L'
            System.out.println(matrix3D);
//            floats = euler(matrix3D);
//            getFloatBufferRotate(pointCloudBuffer);
            FloatBuffer tmpBuffer = pointCloudList.get(0);
            int tmpVertData = vertDataList.get(0);
            vertDataList.add(tmpVertData);
            pointCloudList.add(getFloatBufferRotate(tmpBuffer));
        }

        println("event.getKeyCode() = " + event.getKeyCode());
        vertData = vertDataList.get(frameNumber) * 3;
        println("frameNumber = " + frameNumber);
    }

    private float[] euler(PMatrix3D m) {
        float[] out = new float[3];
        out[0] = atan2(-m.m12, m.m22);
        out[1] = atan2(m.m02, sqrt(pow(m.m12, 2.0f) + pow(m.m22, 2.0f)));
        out[2] = atan2(-m.m01, m.m00);
        return out;
    }

    private FloatBuffer getFloatBufferRotate(FloatBuffer srcBuffer) {
        FloatBuffer resBuffer = null;
        List<String> listPoints = new ArrayList<>();
        List<Float> tmpFloatList = new ArrayList();

        Float[] tmp = new Float[3];

        float minX = 99999;
        float minY = 99999;
        float minZ = 99999;

        for (int j = 0; j < vertDataList.get(0); j++) {
            float x = srcBuffer.get(j * 3 + 0);
            float y = srcBuffer.get(j * 3 + 1);
            float z = srcBuffer.get(j * 3 + 2);

            if (x < minX) {
                minX = x;
            }
            if (y < minY) {
                minY = y;
            }
            if (z < minZ) {
                minZ = z;
            }

//            listPoints.add(x + " " + y + " " + z);
//            tmpFloatList.add(x);
//            tmpFloatList.add(y);
//            tmpFloatList.add(z);

            float[] newCoordFloats = getNewCoords(0, 1, 0, x, y, z);
            listPoints.add(newCoordFloats[0] + " " + newCoordFloats[1] + " " + newCoordFloats[2]);
            tmpFloatList.add(newCoordFloats[0]);
            tmpFloatList.add(newCoordFloats[1]);
            tmpFloatList.add(newCoordFloats[2]);

        }
        System.out.println(minX + "\t" + minY + "\t" + minZ);

        Float[] tmpFloatArray;
        tmpFloatArray = tmpFloatList.toArray(new Float[0]);

        resBuffer = floatBufferFromArray(tmpFloatArray);

//        String[] pointsArray = listPoints.toArray(new String[0]);
//        saveStrings("res/" + millis() + (int) random(0, 10) + ".txt", pointsArray);
        return resBuffer;
    }

    private float[] getNewCoords(float pitch, float yaw, float roll, float x, float y, float z) {
        float cosa = (float) Math.cos(yaw);
        float sina = (float) Math.sin(yaw);

        float cosb = (float) Math.cos(pitch);
        float sinb = (float) Math.sin(pitch);

        float cosc = (float) Math.cos(roll);
        float sinc = (float) Math.sin(roll);

        float Axx = cosa * cosb;
        float Axy = cosa * sinb * sinc - sina * cosc;
        float Axz = cosa * sinb * cosc + sina * sinc;

        float Ayx = sina * cosb;
        float Ayy = sina * sinb * sinc + cosa * cosc;
        float Ayz = sina * sinb * cosc - cosa * sinc;

        float Azx = -sinb;
        float Azy = cosb * sinc;
        float Azz = cosb * cosc;

        float px = x;
        float py = y;
        float pz = z;

        float[] resFloat = new float[3];
        resFloat[0] = Axx * px + Axy * py + Axz * pz;
        resFloat[1] = Ayx * px + Ayy * py + Ayz * pz;
        resFloat[2] = Azx * px + Azy * py + Azz * pz;

        return resFloat;
    }

    private void extractData() {

        float[] floats = euler(matrix3D);

        pushMatrix();
        translate(width / 2, height / 2, 0);
        scale(2);
        rotateX(floats[0]);
        rotateY(floats[1]);
        rotateZ(floats[2]);


        strokeWeight(9);

        fill(255, 0, 0);
        stroke(255, 0, 0);
        line(0, 0, 0, 100, 0, 0);
        //textFont(font, 20.0);
        text("X", 100, 0, 0);

        fill(0, 255, 0);
        stroke(0, 255, 0);
        line(0, 0, 0, 0, 100, 0);
        //textFont(font, 20.0);
        text("Y", 0, 100, 0);

        fill(0, 0, 255);
        stroke(0, 0, 255);
        line(0, 0, 0, 0, 0, 100);
        //textFont(font, 20.0);
        text("Z", 0, 0, 100);
        popMatrix();
    }

    private void showUI() {
        if (isShowUI) {
            isShowUI = false;
        } else {
            isShowUI = true;
        }
    }

    private void drawFrameBar() {

        //xCoordFrameBar = mouseX;
        //yCoordFrameBar = mouseY;
        //xCoordFrameBar = width*0.1;

        xCoordFrameBar = w * 0.1f + frameNumber * frameStep;

        noStroke();
        fill(100);
        rect(w * 0.1f, h * 0.9f, frameBarLength, 10);
        //rect(xCoordFrameBar - 20, yCoordFrameBar - 40, 40, 80);
        fill(200, 0, 200);
        rect(xCoordFrameBar, yCoordFrameBar - 50, 1, 100);
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

    private void openFolder() {
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
            resFolderName = fileChooser.getSelectedFile().getName();
            vertDataList.clear();
            pointCloudList.clear();
            frameNumber = 0;
            initPointCloudList(fileChooser.getSelectedFile());
        }
    }

    public static void main(String[] args) {
        NyArApp nyArApp = new NyArApp();
        nyArApp.main(nyArApp.getClass().getName());

        NyArStatic nyArStatic = new NyArStatic();
        nyArStatic.main(nyArStatic.getClass().getName());

    }

    public void settings() {
        size(1024, 768, P3D);
    }

}