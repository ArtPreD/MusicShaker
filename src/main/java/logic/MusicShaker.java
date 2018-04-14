package main.java.logic;

import main.java.MusicShakerGUI;
import main.java.animation.AnimationWait;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;


public class MusicShaker implements Runnable {

    private static final MusicShaker INSTANCE = new MusicShaker();

    //private String pathSource= "C:\\MusicShaker\\Input Folder";
    //private String pathSource = "E:\\Download\\Torrent\\Files\\Blood on the Dance Floor - Discography - 2008-2011\\Albums";
    private String pathSource = "E:\\";
    private String pathTarget = "C:\\MusicShaker\\Output Folder";
    private Integer[] randomNumberList;
    private File[] musicList;
    private int songsQuantity, songsQuantityInList;
    private int count = 30;
    private int sizeInByte;
    private boolean isCount, isSize;
    private int percentForProgressBar, fileCount, existCount, musicCount, remainCount, randomNumberCount;

    JTextArea textArea;

    private ArrayList<File> listOfSongs;

    private MusicShaker() {
    }

    private void initialize() {
        System.out.println("Initializing system...");
        listOfSongs = new ArrayList<>();
        textArea = MusicShakerGUI.shaker.getArea();
    }

    public void run() {
        System.out.println("START THREAD PROGRAM");
        initialize();
        System.out.println("\n" + "Start scanning folder for path: " + pathSource + "\n" + "Please wait..." + "\n\n");
        scanDirForSongs(new File(pathSource));
        AnimationWait.isScanRemaining = false;
        textArea.setText("Start scanning folder for path: " + pathSource + "\n" + "Please wait..." + "\n\n");
        //System.out.println("Scan complete. Found " + listOfSongs.size() + " files. " + "File list: ");
        textArea.setText(textArea.getText() + "Scan complete. Found " + listOfSongs.size() + " files. " + "File list: " + "\n");
        randomNumberCount = 0;
        listOfSongs.forEach(x -> textArea.setText(textArea.getText() + "\n" + x.getName()));
        songsQuantity = listOfSongs.size();
        generateRandomNumbers();
        createMusicListWithCount();
        writeSongListToFolder(createMusicListWithCount());


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
                }
            }
        }
        //TODO if new File(path).list return null then display error dialog
    }

    private void generateRandomNumbers() {
        System.out.println("\n" + "=========================");
        System.out.println("Generate " + count + " random numbers");
        randomNumberList = new Integer[songsQuantity];
        for (int i = 0; i < randomNumberList.length; i++){
            randomNumberList[i] = i;
        }
        List<Integer> randomList = Arrays.asList(randomNumberList);
        Collections.shuffle(randomList);
        randomNumberList = randomList.toArray(randomNumberList);
        for (int j = 0; j < count; j++){
            System.out.print(randomNumberList[j] + " ");
        }
        System.out.println("\n" + "=========================" + "\n");
    }


    private File[] createMusicListWithCount(){
        System.out.println("Build list of chosen music: ");
        musicList = new File[count];
        remainCount = songsQuantity - count;
        System.out.println("Remain count is " + remainCount);
        for (int i = 0; i < musicList.length; i++){
          //  System.out.println("randomNumberCount is " + randomNumberCount);
            musicList[i] = addMusicToMusicList(randomNumberCount);
            randomNumberCount++;
            System.out.println((i+1) + " " + musicList[i].getName());
        }

        return musicList;
    }


    private File addMusicToMusicList(int i){
        // listOfSongs - all music, musicList - list of music for copy, randomNumberList - list of random numbers
        File song = listOfSongs.get(randomNumberList[i]);
        for (File track : musicList){
            if (track != null) {
                if (remainCount != 0 && track.getName().equals(song.getName())) {
                    remainCount--;
                    System.out.println("REPLAY!" + song.getName());
                        song = addMusicToMusicList(i + 1);
                    System.out.println("Remain count is " + remainCount);
                    break;
                }
            }
        }
        return song;
    }


    private void writeSongListToFolder(File[] songsList) {
        File fileForAccess;
        Path sourcePath;
        Path targetPath = Paths.get(pathTarget);
        System.out.println("\n" + "Start coping files into " + pathTarget);
        try {
            for (File song : songsList) {
                percentForProgressBar = fileCount * 100 / count;
                sourcePath = Paths.get(song.getPath());
                MusicShakerGUI.shaker.updateProgress("Copy file: " + song.getName(), percentForProgressBar);
                try {
                    Files.copy(sourcePath, targetPath.resolve(song.getName()));
                } catch (FileAlreadyExistsException exist) {
                    System.out.println("I catch FileAlreadyExistsException! Replace file");
                    existCount++;
                    try {
                        Files.copy(sourcePath, targetPath.resolve(song.getName()), StandardCopyOption.REPLACE_EXISTING);
                    } catch (AccessDeniedException denied) {
                        System.out.println("I catch AccessDeniedException!");
                        fileForAccess = new File(targetPath + "\\" + song.getName());
                        fileForAccess.setWritable(true);
                        Files.copy(sourcePath, targetPath.resolve(song.getName()), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                fileCount++;
                System.out.println("Copy " + fileCount + " files");
            }
            MusicShakerGUI.shaker.updateProgress("Copy successful! Copy " + (fileCount - existCount)
                    + " files. " + existCount + " file(s) replace because already exist.", 100);
            fileCount = 0;
            System.out.println("Copy successful!");
        } catch (IOException ex) {
            fileCount = 0;
            MusicShakerGUI.shaker.updateProgress("Error. Can't copy file", percentForProgressBar);
            ex.printStackTrace();
            System.out.println("Cannot copy files...");
            //TODO if writeSongListToFolder throw IOException show error dialog
        }
    }

    public static MusicShaker getInstance() {
        return INSTANCE;
    }

    public int getSongsQuantity() {
        return songsQuantity;
    }

    public void setPathSource(String pathSource) {
        this.pathSource = pathSource;
    }

    public void setPathTarget(String pathTarget) {
        this.pathTarget = pathTarget;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getPathSource() {
        return pathSource;
    }
}
