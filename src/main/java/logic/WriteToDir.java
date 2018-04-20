package main.java.logic;

import main.java.ShakerCallBack;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class WriteToDir implements Runnable {

    public static final int UPDATE_PROGRESS = 9;
    public static final int WRITE_COMPLETE = 20;
    public static final int WRITE_FAIL = 21;

    private ShakerCallBack callBack;

    private String path;
    private File[] songsList;

    private int fileCount, existCount, count;

    public WriteToDir(File[] songsList, String path, ShakerCallBack callBack){
        this.callBack = callBack;
        this.songsList = songsList;
        this.path = path;
        count = songsList.length;
    }

    @Override
    public void run() {
        writeSongListToFolder(songsList, path);
        callBack.sendMessage(WRITE_COMPLETE, "Write successful");
    }

    private void writeSongListToFolder(File[] songsList, String path) {
        File fileForAccess;
        Path sourcePath;
        Path targetPath = Paths.get(path);
        System.out.println("\n" + "Start coping files into " + path);
        try {
            for (File song : songsList) {
                sourcePath = Paths.get(song.getPath());
                callBack.sendMessage(UPDATE_PROGRESS, song.getName());
                try {
                    Files.copy(sourcePath, targetPath.resolve(song.getName()));
                } catch (FileAlreadyExistsException exist) {
                    existCount++;
                    try {
                        Files.copy(sourcePath, targetPath.resolve(song.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (AccessDeniedException denied) {
                        fileForAccess = new File(targetPath + "\\" + song.getName());
                        fileForAccess.setWritable(true);
                        Files.copy(sourcePath, targetPath.resolve(song.getName()), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                fileCount++;
            }
           callBack.sendMessage(WRITE_COMPLETE, "Copy successful! Copy " + (fileCount - existCount)
                    + " files. " + existCount + " file(s) replace because already exist.");
            //MusicShakerGUI.shaker.updateProgress("Copy successful! Copy " + (fileCount - existCount)
            //  + " files. " + existCount + " file(s) replace because already exist.", 100);
            fileCount = 0;
        } catch (IOException ex) {
            fileCount = 0;
            callBack.sendMessage(WRITE_FAIL, "Error. Can't copy file");
            ex.printStackTrace();
            //TODO if writeSongListToFolder throw IOException show error dialog
        }
    }
}
