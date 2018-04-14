package main.java.animation;

import main.java.MusicShakerGUI;
import main.java.logic.MusicShaker;

import javax.swing.*;

public class AnimationWait extends Thread {

    MusicShaker musicShaker = MusicShaker.getInstance();
    JTextArea area;
    JButton shakeIt;

    public static boolean isScanRemaining = true;

    @Override
    public void run() {
        System.out.println("START THREAD ANIMATION");
        area = MusicShakerGUI.shaker.getArea();
        shakeIt = MusicShakerGUI.shaker.getShakeIt();

            String wait = "Please wait";
            int count = 0;
            while (isScanRemaining){
                area.setText("Start scanning folder for path: " + musicShaker.getPathSource() + "\n" + wait);
                if (count < 3){
                    wait = wait.concat(".");
                    count++;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else {
                    wait = "Please wait";
                    count = 0;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            shakeIt.setEnabled(true);
        }
}
