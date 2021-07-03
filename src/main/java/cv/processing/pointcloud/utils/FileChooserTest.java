package cv.processing.pointcloud.utils;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class FileChooserTest extends JFrame {

    private JButton btnSaveFile = null;
    private JButton btnOpenDir = null;
    private JButton btnFileFilter = null;

    private JFileChooser fileChooser = null;

    private static String folderName;
    private File fileTxt;

    private final String[][] FILTERS = {{"txt", "Файлы txt (*.txt)"},
            {"pdf", "Adobe Reader(*.pdf)"}};

    public FileChooserTest() {
        super("Пример FileChooser");
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Кнопка создания диалогового окна для выбора директории
        btnOpenDir = new JButton("Открыть директорию");
        // Кнопка создания диалогового окна для сохранения файла
        btnSaveFile = new JButton("Сохранить файл");
        // Кнопка создания диалогового окна для сохранения файла
        btnFileFilter = new JButton("Фильтрация файлов");

        // Создание экземпляра JFileChooser
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir") + "/res/"));
        // Подключение слушателей к кнопкам
        addFileChooserListeners();

        // Размещение кнопок в интерфейсе
        JPanel contents = new JPanel();
        contents.add(btnOpenDir);
        contents.add(btnSaveFile);
        contents.add(btnFileFilter);
        setContentPane(contents);
        // Вывод окна на экран
        setSize(360, 110);
        setVisible(true);
    }

    private void addFileChooserListeners() {
        btnOpenDir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выбор директории");
                // Определение режима - только каталог
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(FileChooserTest.this);
                // Если директория выбрана, покажем ее в сообщении
                if (result == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(FileChooserTest.this, fileChooser.getSelectedFile());
                    setFolderName(fileChooser.getSelectedFile().getName());
//                    System.out.println("fileChooser.getName() = " + fileChooser.getSelectedFile().getName());

                }
            }
        });

        btnSaveFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Сохранение файла");
                // Определение режима - только файл
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showSaveDialog(FileChooserTest.this);
                // Если файл выбран, то представим его в сообщении
                if (result == JFileChooser.APPROVE_OPTION)
                    JOptionPane.showMessageDialog(FileChooserTest.this,
                            "Файл '" + fileChooser.getSelectedFile() +
                                    " ) сохранен");
            }
        });

        btnFileFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выберите файл");
                // Определяем фильтры типов файлов
                for (int i = 0; i < FILTERS[0].length; i++) {
                    FileFilterExt eff = new FileFilterExt(FILTERS[i][0], FILTERS[i][1]);
                    fileChooser.addChoosableFileFilter(eff);
                }
                // Определение режима - только файл
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//                int result = fileChooser.showSaveDialog(FileChooserTest.this);
                int result = fileChooser.showOpenDialog(FileChooserTest.this);
                // Если файл выбран, покажем его в сообщении
                if (result == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(FileChooserTest.this,
                            "Выбран файл ( " +
                                    fileChooser.getSelectedFile() + " )");
                    setFileTxt(fileChooser.getSelectedFile());
                    System.out.println(fileTxt.getAbsolutePath());
                }
            }
        });
    }

    public File getFileTxt() {
        return fileTxt;
    }

    public void setFileTxt(File fileTxt) {
        this.fileTxt = fileTxt;
    }

    public static String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}