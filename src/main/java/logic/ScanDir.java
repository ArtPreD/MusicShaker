package main.java.logic;

import main.java.ShakerCallBack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ScanDir implements Runnable {
    public static final int SCAN_COMPLETE = 8;

    private ShakerCallBack callBack;

    private String path;
    private ArrayList<File> listOfSongs;

    public ScanDir(String path, ArrayList<File> listOfSongs, ShakerCallBack callBack){
        this.path = path;
        this.listOfSongs = listOfSongs;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        scanDirForSongs(new File(path));
        callBack.sendMessage(SCAN_COMPLETE, "scan successful");
    }

    private void scanDirForSongs(File folder) {
        File[] folderEntries = folder.listFiles();
        if (folderEntries != null) {
            for (File entry : folderEntries) {
                if (entry.isDirectory()) {
                    scanDirForSongs(entry);
                    continue;
                }
                try {
                    String checker = Files.probeContentType(Paths.get(entry.getPath()));
                    if (checker != null && checker.equals("audio/mpeg")) {
                        listOfSongs.add(entry);
                    }
                } catch (IOException e) {
                    System.out.println("Cannot probe content type");
                    //TODO sent message
                }
            }
        }
        //TODO if new File(path).list return null then display error dialog
    }
}
