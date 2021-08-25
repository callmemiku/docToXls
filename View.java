package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class View {

    private final Controller controller;
    private final JFrame jFrame = new JFrame();
    private final JButton importChoice = new JButton();
    private final JButton importDirChoice = new JButton();
    private final JButton outputChoice = new JButton();
    private final JFileChooser jFileChooser = new JFileChooser();
    private final JButton jButton = new JButton();
    private final JButton exitChoice = new JButton();

    public View(Controller controller) {
        this.controller = controller;
    }

    public void popUp(String s){
        JOptionPane.showMessageDialog(jFrame, s);
    }

    public void init() {

        importChoice.setOpaque(false);
        importChoice.setBorderPainted(false);
        outputChoice.setOpaque(false);
        outputChoice.setBorderPainted(false);
        importDirChoice.setOpaque(false);
        importDirChoice.setBorderPainted(false);

        jFrame.setSize(300, 125);
        jFrame.setResizable(true);
        jFileChooser.setMultiSelectionEnabled(true);

        jFileChooser.setPreferredSize(new Dimension(640, 300));

        importChoice.addActionListener(e -> {
            jFileChooser.setDialogTitle("Выберите файл");
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = jFileChooser.showOpenDialog(jFileChooser);
            if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.gatherDataAndClean(jFileChooser.getSelectedFiles());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
            }
            else {
                JOptionPane.showMessageDialog(jFileChooser, "Файлы не выбраны.");
            }
        });

        importDirChoice.addActionListener(e -> {
            jFileChooser.setDialogTitle("Выберите папку");
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int result = jFileChooser.showOpenDialog(jFileChooser);
            if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        controller.gatherDataAndClean(jFileChooser.getSelectedFiles());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
            }
            else {
                JOptionPane.showMessageDialog(jButton, "Файлы не выбраны.");
            }
        });

        outputChoice.addActionListener(e -> {
            while (true) {
                jFileChooser.setDialogTitle("Выберите папку для сохранения");
                jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = jFileChooser.showSaveDialog(jFileChooser);
                if (result == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(jButton, "Запись.");
                    try {
                        controller.write(jFileChooser.getSelectedFile().toString());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    break;
                }
            }
        });

        exitChoice.addActionListener(e -> {
            System.exit(0);
        });

        JPanel jPanel = new JPanel();
        jPanel.add(importChoice);
        jPanel.add(importDirChoice);
        jPanel.add(outputChoice);
        jPanel.add(exitChoice);
        importChoice.setText("Файл");
        importChoice.setVerticalAlignment(SwingConstants.NORTH);
        importChoice.setHorizontalAlignment(SwingConstants.LEFT);
        importDirChoice.setText("Папка");
        importDirChoice.setVerticalAlignment(SwingConstants.NORTH);
        importDirChoice.setHorizontalAlignment(SwingConstants.RIGHT);
        outputChoice.setText("Запись");
        outputChoice.setVerticalAlignment(SwingConstants.CENTER);
        outputChoice.setHorizontalAlignment(SwingConstants.CENTER);
        exitChoice.setText("Выход");
        exitChoice.setVerticalAlignment(SwingConstants.BOTTOM);
        exitChoice.setHorizontalAlignment(SwingConstants.CENTER);
        jFrame.add(jPanel);

        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        jFrame.setVisible(true);
    }
}
