package main.java.animation;


import main.java.ShakerCallBack;


public class AnimationWait extends Thread {

    public static final int SCAN_REMAINING = 22;

    private ShakerCallBack callBack;
    private static boolean isScanRemaining = true;


    public AnimationWait(ShakerCallBack callBack){
        this.callBack = callBack;
    }

    @Override
    public void run() {
            String wait = "Please wait";
            int count = 0;
            while (isScanRemaining){
                System.out.println("SCAN_REMAINING");
                callBack.sendMessage(SCAN_REMAINING, wait);
                if (count < 3){
                    wait = wait.concat(".");
                    count++;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        isScanRemaining = false;
                    }
                }else {
                    wait = "Please wait";
                    count = 0;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        isScanRemaining = false;
                    }
                }
            }
        }
}
