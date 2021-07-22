package cv.processing.pointcloud;

import processing.core.PApplet;
import cv.processing.pointcloud.utils.ConverterOBJ;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class FrameBuffer extends PApplet {
    private String folderName;

    {
        String dayStr = String.valueOf(PApplet.day());
        String monthStr = String.valueOf(month());
        String hourStr = String.valueOf(hour());
        String minuteStr = String.valueOf(minute());
        folderName = monthStr + "_" + dayStr + "_" + hourStr + "_" + minuteStr + "_";
    }

    private FloatBuffer frame;

    private boolean isRunFile = false;

    //id of the frame
    private int frameId;

    public FrameBuffer(FloatBuffer f) {
        frame = clone(f);
    }

    public void setFrameId(int fId) {
        frameId = fId;
    }

    /*
    Writing of the obj file,
     */
    public void saveTxtFrame() {
        int vertData = 512 * 424;
        String[] points = new String[vertData];
        ArrayList<String> listPoints = new ArrayList<String>();

        //Iterate through all the XYZ points
        for (int i = 0; i < vertData; i++) {
            //float x =  frame.get(i*3 + 0) * 1000;
            //float y =  frame.get(i*3 + 1) * 1000;
            //float z =  frame.get(i*3 + 2) * 1000;
            float x = frame.get(i * 3 + 0);
            float y = frame.get(i * 3 + 1);
            float z = frame.get(i * 3 + 2);
            int xi = (int) x;
            int yi = (int) y;
            int zi = (int) z;
            //if (x < 0.001 && y < 0.001 && z < 0.001) {
            //} else {
            //listPoints.add("v "+x+" "+y+" "+z);
            listPoints.add(x + " " + y + " " + z);
        }

        String[] pointsArray = listPoints.toArray(new String[0]);

        saveStrings("res/" + folderName + "/" + millis() + (int) random(0, 10) + ".txt", pointsArray);

        println("Done Saving Frame " + frameId);
        println("pointsArray.length = " + pointsArray.length);
    }

    public void saveOBJFrame() {
        int vertData = 512 * 424;
        String[] points = new String[vertData];

        //Iterate through all the XYZ points
        for (int i = 0; i < vertData; i++) {
            //float x =  frame.get(i*3 + 0) * 1000;
            //float y =  frame.get(i*3 + 1) * 1000;
            //float z =  frame.get(i*3 + 2) * 1000;
            float x = frame.get(i * 3 + 0);
            float y = frame.get(i * 3 + 1);
            float z = frame.get(i * 3 + 2);
            int xi = (int) x;
            int yi = (int) y;
            int zi = (int) z;
            //if (x < 0.001 && y < 0.001 && z < 0.001) {
            //} else {
            //listPoints.add("v "+x+" "+y+" "+z);
            points[i] = "v " + x + " " + y + " " + z;
        }

        if (isRunFile) {
            String s = "res/" + millis() + (int) random(0, 10) + ".obj";
            System.out.println("s = " + s);
            saveStrings(s, points);
            ConverterOBJ.runFile(new File(s));
        } else {
            String fileName = "res/" + folderName + "/" + millis() + (int) random(0, 10) + ".obj";
            saveStrings(fileName, points);
        }

        println("Done Saving Frame " + frameId);
        println("pointsArray.length = " + points.length);
    }

    //Simple function that copys the FloatBuffer to another FloatBuffer
    public FloatBuffer clone(FloatBuffer original) {
        FloatBuffer clone = FloatBuffer.allocate(original.capacity());
        original.rewind();//copy from the beginning
        clone.put(original);
        original.rewind();
        clone.flip();
        return clone;
    }

    public boolean isRunFile() {
        return isRunFile;
    }

    public void setRunFile(boolean runFile) {
        isRunFile = runFile;
    }
}
