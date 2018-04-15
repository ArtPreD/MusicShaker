package main.java;

import main.java.logic.MusicShaker;

import javax.swing.*;

public class MainEngine {

    private static String pathSource = "C:\\MusicShaker\\Input Folder";
    private static String pathTarget = "C:\\MusicShaker\\Output Folder";

    private MusicShaker musicShaker = MusicShaker.getInstance();
    private MusicShakerGUI musicShakerGUI;

    private boolean isTargetSet;

    private JTextArea area;
    private JProgressBar progressBar;
    private JButton start, startScan;

    private MainEngine(){
        ShakerCallBack shakerCallBack = (id, message) -> {
            System.out.println("Main engine send id : " + id + ", message: " + message);
            switch (id) {
                case MusicShakerGUI.START_SCAN:
                    System.out.println(message + " send");
                    musicShaker.startScan(area, pathSource);
                    start.setEnabled(true);
                    break;
                case MusicShakerGUI.START_SERVER:
                    System.out.println(message + " send");
                    break;
                case MusicShakerGUI.COPY_RANDOM:
                    System.out.println(message + " send");
                    break;
                case MusicShakerGUI.COPY_ALL:
                    System.out.println(message + " send");
                    break;
                case MusicShakerGUI.START:
                    if (isTargetSet){
                        System.out.println(message + " send");
                    }else {
                        System.out.println("Target path isn't set");
                    }

                    break;
                case MusicShakerGUI.SELECT_PATH_TARGET:
                    System.out.println(message);
                    isTargetSet = true;
                    pathTarget = message;
                    break;
                case MusicShakerGUI.SELECT_PATH_SOURCE:
                    System.out.println(message);
                    pathSource = message;
                    startScan.setEnabled(true);
                    break;
            }
        };

        musicShakerGUI = new MusicShakerGUI(shakerCallBack);

        area = musicShakerGUI.getArea();
        start = musicShakerGUI.getShakeIt();
        startScan = musicShakerGUI.getStartScan();

    }

    public static void main(String[] args) {
        new MainEngine();
    }
}
