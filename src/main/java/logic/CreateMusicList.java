package main.java.logic;

import main.java.ShakerCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreateMusicList implements Runnable {

    public static final int LIST_COMPLETE = 24;

    private ShakerCallBack callBack;
    private Integer[] randomNumberList;
    private int count, songsQuantity, remainCount, randomNumberCount;
    private File[] musicList;
    private ArrayList<File> listOfSongs;

    public CreateMusicList(ArrayList<File> listOfSongs, int count, File[] musicList, ShakerCallBack callBack){
        this.listOfSongs = listOfSongs;
        this.count = count;
        this.musicList = musicList;
        this.callBack = callBack;
        songsQuantity = listOfSongs.size();
    }

    @Override
    public void run() {
        generateRandomNumbers();
        musicList = createMusicListWithRandomNumbers();
        callBack.sendMessage(LIST_COMPLETE, "successful");
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


    private File[] createMusicListWithRandomNumbers(){
        System.out.println("Build list of chosen music: ");
        musicList = new File[count];
        remainCount = songsQuantity - count;
        System.out.println("Remain count is " + remainCount);
        for (int i = 0; i < musicList.length; i++){
              System.out.println("randomNumberCount is " + randomNumberCount);
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
}
