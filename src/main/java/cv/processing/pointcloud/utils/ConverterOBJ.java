package cv.processing.pointcloud.utils;

import processing.core.PApplet;
import processing.event.KeyEvent;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import controlP5.*;

public class ConverterOBJ extends PApplet {

    private Button buttonOpenFile;
    private ControlP5 controlP5;

    private final String[][] FILTERS = {{"txt", "Файлы txt (*.txt)"},
            {"pdf", "Adobe Reader(*.pdf)"}};

    public static void main(String[] args) {
        ConverterOBJ converterOBJ = new ConverterOBJ();
        converterOBJ.main(converterOBJ.getClass().getName());
    }

    @Override
    public void setup() {
        frameRate(1000);
        controlP5 = new ControlP5(this);
        buttonOpenFile = controlP5.addButton("open file button").setPosition(100, 100).setSize(200, 50);
        buttonOpenFile.addListener(new ControlListener() {
            @Override
            public void controlEvent(ControlEvent controlEvent) {
                ConverterOBJ.this.openFile();
            }
        });

    }

    @Override
    public void draw() {
        surface.setTitle("txt to obj converter " + frameRate);
    }

    @Override
    public void settings() {
        size(500, 200);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 10) {
            openFile();
        }
    }

    private void openFile() {
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
        for (int i = 0; i < FILTERS[0].length; i++) {
            FileFilterExt eff = new FileFilterExt(FILTERS[i][0], FILTERS[i][1]);
            fileChooser.addChoosableFileFilter(eff);
        }

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/res/"));


        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(jFrame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            ArrayList<String> list = new ArrayList<>();

            println(file.getAbsolutePath());
            BufferedReader reader = createReader(file);
            try {
                while (reader.ready()) {
                    String line = reader.readLine();
                    String resLine = "v " + line;
                    list.add(resLine);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String[] array = list.toArray(new String[0]);
            String outputFileName = file.getName().substring(0, file.getName().length() - 4) + ".obj";
            saveStrings("res/" + outputFileName, array);


            File outputFile = new File(System.getProperty("user.dir") + "\\res\\" + outputFileName);

            outputFile.setExecutable(true);
            System.out.println(outputFile.canExecute());
            this.runFile(outputFile);

//            println("file.getParent() = " + file.getParent());
//            println("file.getPath() = " + file.getPath());
//
//            println("outputFileName = " + outputFileName);
//            File outputOBJFile = new File(sketchPath() + "/res/" + outputFileName);
//
//            PrintWriter writer = createWriter(outputOBJFile);

        }
    }

    public static void runFile(File file) {

        System.out.println("ping method");
        ProcessBuilder processBuilder = new ProcessBuilder();

        // Run this on Windows, cmd, /c = terminate after this run
        processBuilder.command("cmd.exe", "/c", file.getAbsolutePath());

        try {

            Process process = processBuilder.start();

            // blocked :(
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
