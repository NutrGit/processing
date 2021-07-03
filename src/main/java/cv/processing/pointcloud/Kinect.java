package cv.processing.pointcloud;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.opengl.PGL;
import processing.opengl.PShader;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import KinectPV2.*;


public class Kinect extends PApplet {

    /*
Thomas Sanchez Lengeling.
 http://codigogenerativo.com/

 KinectPV2, Kinect for Windows v2 library for processing

 How to record Point Cloud Data and store it in several obj files.
 Record with 'r'
 */

    KinectPV2 kinect;

    boolean saveVideo = false;

    PGL pgl;
    PShader sh;

    int vertLoc;

    float fov;

    //transformations
    float a = 7;
    int zval = -50;
    float scaleVal = 220;

    //Distance Threashold
    int maxD = 4500; // 4.5 m
    int minD = 0;  //  50 cm

    float deltaX;

    float w;
    float h;

    int numFrames = 30 * 20; // 30 frames  = 1s of recording
    int frameCounter = 0; // frame counter

    boolean recordFrame = false;  //recording flag
    boolean doneRecording = false;

    //Array where all the frames are allocated
    ArrayList<FrameBuffer> mFrames;

    //VBO buffer location in the GPU
    int vertexVboId;

    float xScene, yScene;

    boolean isMovingMode;

    FloatBuffer pointCloudBuffer;

    boolean isOrtho;

    public void setup() {

        isOrtho = false;

        xScene = width / 2;
        yScene = height / 2;

        w = width;
        h = height;

        kinect = new KinectPV2(this);

        //create arrayList
        mFrames = new ArrayList<FrameBuffer>();

        fov = PI / 2.0f;

        //Enable point cloud
        kinect.enableDepthImg(true);
        kinect.enablePointCloud(true);

        kinect.init();

        sh = loadShader(new File("src\\main\\resources\\res\\frag.glsl").getAbsolutePath(),
                new File("src\\main\\resources\\res\\vert.glsl").getAbsolutePath());

        PGL pgl = beginPGL();

        IntBuffer intBuffer = IntBuffer.allocate(1);
        pgl.genBuffers(1, intBuffer);

        //memory location of the VBO
        vertexVboId = intBuffer.get(0);

        endPGL();

        //set framerate to 30
        frameRate(30);
    }

    public void draw() {
        background(0);
        surface.setTitle("" + frameRate);

        checkWindowResised();

        //draw the depth capture images
//        image(kinect.getDepthImage(), 0, 0);
//        image(kinect.getPointCloudDepthImage(), 0, 424);

        if (keyPressed) {
            if (keyCode == LEFT) {
                a += 0.05;
            } else if (keyCode == RIGHT) {
                a -= 0.05;
            }
        }


        fill(200);
        rect(xScene - 40, yScene - 40, 80, 80);

        //translate the scene to the center
        //translate(width / 2, height / 2, zval);
        translate(xScene, yScene, zval*2);
        scale(scaleVal, -1 * scaleVal, scaleVal);
        //if (mousePressed) {
        //  a = (float)mouseX / (float)100;
        //}
        rotate(a, 0.0f, 1.0f, 0.0f);

        stroke(100, 0, 100);
        strokeWeight(0.005f);
        noFill();
        //box(0.25);
        box(3);
        noStroke();
        fill(255);

        // Threahold of the point Cloud.
        kinect.setLowThresholdPC(minD);
        kinect.setHighThresholdPC(maxD);

        //get the points in 3d space
        pointCloudBuffer = kinect.getPointCloudDepthPos();

        //data size, 512 x 424 x 3 (XYZ) coordinate
        int vertData = kinect.WIDTHDepth * kinect.HEIGHTDepth * 3;

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

        //allocate the current pointCloudBuffer into an array of FloatBuffers
        allocateFrame(pointCloudBuffer);

        //when the allocation is done write the obj frames
        writeFrames();

        //stroke(255, 0, 0);
        //surface.setTitle("" + (int)frameRate + ", arrayList = " + mFrames.size()
        //  + ", vertData = " + vertData + ", a = " + a);
        if(saveVideo){
            saveFrame();
        }
    }

    //allocate all the frame in a temporary array
    void allocateFrame(FloatBuffer buffer) {
        if (recordFrame) {
            if (frameCounter < numFrames) {
                FrameBuffer frameBuffer = new FrameBuffer(buffer);
                frameBuffer.setFrameId(frameCounter);
                mFrames.add(frameBuffer);
            } else {
                recordFrame = false;
                //doneRecording = true;
            }
            println("recorded " + mFrames.size() + " frames in cache");
            frameCounter++;
        }
    }

    //Write all the frames recorded
    void writeFrames() {
        if (doneRecording) {
            for (int i = 0; i < mFrames.size(); i++) {
                FrameBuffer fBuffer = (FrameBuffer) mFrames.get(i);
//                fBuffer.saveOBJFrame();
                fBuffer.saveTxtFrame();
//                if (i == mFrames.size() - 1) {
//                    fBuffer.saveOBJFrame();
//                }
            }
            doneRecording = false;
            println("Done Recording frames: " + numFrames);
        }
    }

    public void keyPressed(KeyEvent event) {

        //start recording 30 frames with 'r'
        if (key == 'r') {
            recordFrame = true;
        }
        if (key == 'a') {
            zval += 1;
            println(zval);
        }
        if (key == 's') {
            zval -= 1;
            println(zval);
        }

        if (key == 'z') {
            scaleVal += 0.1;
            println(scaleVal);
        }
        if (key == 'x') {
            scaleVal -= 0.1;
            println(scaleVal);
        }

        if (key == 'q') {
            a += 0.1;
            println(a);
        }
        if (key == 'w') {
            a -= 0.1;
            println(a);
        }

        if (key == '1') {
            minD += 10;
            println("Change min: " + minD);
        }

        if (key == '2') {
            minD -= 10;
            println("Change min: " + minD);
        }

        if (key == '3') {
            maxD += 10;
            println("Change max: " + maxD);
        }

        if (key == '4') {
            maxD -= 10;
            println("Change max: " + maxD);
        }

        if (key == '5') {
            FrameBuffer fBuffer = new FrameBuffer(pointCloudBuffer);
            fBuffer.setRunFile(true);
            fBuffer.saveOBJFrame();
            println("save 1 frame");
        }

        if (key == '6') {
            doneRecording = true;
            println("doneRecording = " + doneRecording);
        }

        if(key == '7'){
            saveVideo = true;
        }

        if (event.getKeyCode() == 79) {
            //'O'
            setOrtho();
        }
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

    boolean isDeltaXPositive() {
        if (deltaX > 0) {
            return true;
        } else {
            return false;
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

    private void setOrtho() {
        if (isOrtho) {
            isOrtho = false;
        } else {
            isOrtho = true;
        }

        if (isOrtho) {
            float cameraZ = (height / 2.0f) / tan(fov / 2.0f);
//            perspective(fov, (float) width / (float) height, cameraZ / 20.0f, cameraZ * 20.0f);
            perspective(2.2f, (float) width / (float) height, cameraZ / 50.0f, cameraZ * 50.0f);
        } else {
            ortho(-width / 2, width / 2, -height / 2, height / 2);
        }
    }

    public static void main(String[] args) {
        Kinect kinect = new Kinect();
        kinect.main(kinect.getClass().getName());
    }

    public void settings() {
        size(1280, 720, P3D);
    }

}