package main.java.logic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScaningDir implements Runnable {


    @Override
    public void run() {
//        File[] folderEntries = folder.listFiles();
//        if (folderEntries != null) {
//            for (File entry : folderEntries) {
//                if (entry.isDirectory()) {
//                    scanDirForSongs(entry);
//                    continue;
//                }
//                try {
//                    String checker = Files.probeContentType(Paths.get(entry.getPath()));
//                    if (checker != null && checker.equals("audio/mpeg")) {
//                        listOfSongs.add(entry);
//                    }
//                } catch (IOException e) {
//                    System.out.println("Cannot probe content type");
//                }
//            }
//        }
//        //TODO if new File(path).list return null then display error dialog
    }
}
