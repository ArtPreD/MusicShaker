package main.java;

import main.java.animation.AnimationWait;
import main.java.logic.CreateMusicList;
import main.java.logic.ScanDir;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

public class MainEngine {

    private static String pathSource = "C:\\MusicShaker\\Input Folder";
    private static String pathTarget = "C:\\MusicShaker\\Output Folder";

    private MusicShakerGUI musicShakerGUI;
    private ShakerCallBack callBack;

    private boolean isTargetSet;

    private JTextArea area;
    private JProgressBar progressBar;
    private JButton start, startScan;

    private int fileCount, percentForProgressBar;
    private int count = 5;
    private ArrayList<File> listOfSongs;
    private File[] musicList;
    private Thread animationThread;

    private MainEngine() {
        ShakerCallBack shakerCallBack = (id, message) -> {
            System.out.println("Main engine send id : " + id + ", message: " + message);
            switch (id) {
                case MusicShakerGUI.START_SCAN:
                    System.out.println(message + " send");
                    startScanning();
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
                    if (isTargetSet) {
                        progressBar.setValue(0);
                        Thread createList = new Thread(new CreateMusicList(listOfSongs, count, musicList, callBack));
                        createList.start();
                        System.out.println(message + " send");
                    } else {
                        musicShakerGUI.targetNotSelectDialog();
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
//                case MusicShaker.UPDATE_PROGRESS:
//                    percentForProgressBar = fileCount * 100 / count;
//                    fileCount++;
//                    musicShakerGUI.updateProgress(message, percentForProgressBar);
//                    break;
//                case MusicShaker.FINISH_WRITE:
//                    musicShakerGUI.updateProgress(message, 100);
//                    break;
                case ScanDir.SCAN_COMPLETE:
                    animationThread.stop();
                    area.setText(area.getText() + "\n\n" + "Scan complete. Found " + listOfSongs.size()
                            + " files. " + "File list: " + "\n");
                    listOfSongs.forEach(x -> area.setText(area.getText() + "\n" + x.getName()));
                    startScan.setEnabled(true);
                    start.setEnabled(true);
                    break;
                case AnimationWait.SCAN_REMAINING:
                    area.setText("Start scanning folder for path: " + pathSource + "\n" + message);
                    break;
                case CreateMusicList.LIST_COMPLETE:
                    //start write
                    System.out.println("list create " + message);
                    break;
            }
        };

        callBack = shakerCallBack;
        listOfSongs = new ArrayList<File>();
        musicShakerGUI = new MusicShakerGUI(shakerCallBack);
       // musicShaker = new MusicShaker(shakerCallBack);


        progressBar = musicShakerGUI.getProgressBar();
        area = musicShakerGUI.getArea();
        start = musicShakerGUI.getShakeIt();
        startScan = musicShakerGUI.getStartScan();

    }

    private void startScanning(){
        area.setText("Start scanning folder for path: " + pathSource + "\n" + "Please wait");
        listOfSongs.clear();
        Thread scanThread = new Thread(new ScanDir(pathSource, listOfSongs, callBack));
        scanThread.start();
        animationThread = new Thread(new AnimationWait(callBack));
        animationThread.start();
        startScan.setEnabled(false);
    }

    public static void main(String[] args) {
        new MainEngine();
    }
}
