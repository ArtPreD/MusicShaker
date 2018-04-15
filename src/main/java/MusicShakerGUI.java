package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MusicShakerGUI extends JFrame implements ActionListener {

    public static final int START_SCAN = 1;
    public static final int START_SERVER = 2;
    public static final int COPY_RANDOM = 3;
    public static final int COPY_ALL = 4;
    public static final int START = 5;
    public static final int SELECT_PATH_SOURCE = 6;
    public static final int SELECT_PATH_TARGET = 7;

    private static final String chooserTitle = "Select folder";

    private JButton selectSource, selectTarget, shakeIt, startScan, startServer, copyRandom, copyAll;
    private JTextField pathS, pathT;
    private JLabel pathSourceLabel, pathTargetLabel, copyFileName;
    private JFileChooser chooser;
    private JProgressBar progressBar;
    private JTextArea area;

    private JScrollPane jScrollPane;

    private ShakerCallBack shakerCallBack;



    public MusicShakerGUI(ShakerCallBack shakerCallBack){
        super("MusicShaker");
        this.shakerCallBack = shakerCallBack;
        setLayout(null);
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildGUI();
        setVisible(true);
    }

    private void buildGUI(){
        int x = 10, y = 10;

        pathSourceLabel = new JLabel("Choose input folder:");
        pathSourceLabel.setBounds(x, y, 200, 15);
        add(pathSourceLabel);
        y += 25;

        pathS = new JTextField();
        pathS.setBounds(x, y, 700, 25);
        pathS.setFocusable(false);
        add(pathS);
        x += 710;

        selectSource = new JButton("Select");
        selectSource.setBounds(x, y, 100, 25);
        selectSource.addActionListener(this);
        add(selectSource);
        y += 35;
        x = 10;

        pathTargetLabel = new JLabel("Choose output folder");
        pathTargetLabel.setBounds(x, y, 200, 15);
        add(pathTargetLabel);
        y += 25;

        pathT = new JTextField();
        pathT.setBounds(x, y, 700, 25);
        pathT.setFocusable(false);
        add(pathT);
        x += 710;

        selectTarget = new JButton("Select");
        selectTarget.setBounds(x, y, 100, 25);
        selectTarget.addActionListener(this);
        add(selectTarget);
        y += 35;
        x = 10;

        startScan = new JButton("Start Scan");
        startScan.setBounds(x, y, 195, 25);
        startScan.addActionListener(this);
        startScan.setEnabled(false);
        add(startScan);

        x += 205;
        startServer = new JButton("Start Server");
        startServer.setBounds(x, y, 195, 25);
        startServer.addActionListener(this);
        add(startServer);

        x += 205;
        copyRandom = new JButton("Random copy");
        copyRandom.setBounds(x, y, 195, 25);
        copyRandom.addActionListener(this);
        add(copyRandom);

        x += 205;
        copyAll = new JButton("Copy all");
        copyAll.setBounds(x, y, 195, 25);
        copyAll.addActionListener(this);
        add(copyAll);



        x = 10;
        y = 475;
        shakeIt = new JButton("Start!");
        shakeIt.setBounds(x, y, 100, 25);
        shakeIt.addActionListener(this);
        shakeIt.setEnabled(false);
        add(shakeIt);
        x += 110;

        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);
        progressBar.setBounds(x, y, 700, 25);
        add(progressBar);
        x = 10;

        y -= 25;
        copyFileName = new JLabel("");
        copyFileName.setBounds(x, y, 400, 15);
        add(copyFileName);

        y -= 200;
        area = new JTextArea();
        area.setForeground(Color.BLACK);
        area.setFont(new Font("Raster Fonts", Font.BOLD, 11));
        area.setFocusable(false);
        jScrollPane = new JScrollPane(area);
        jScrollPane.setBounds(x, y, 810, 190);
        add(jScrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectSource || e.getSource() == selectTarget) {

            selectPath(e);

        } else if (e.getSource() == startScan){


                shakerCallBack.sendMessage(START_SCAN, "Start scan");

            } else if (e.getSource() == startServer){

                shakerCallBack.sendMessage(START_SERVER, "Start server");

            }else if (e.getSource() == copyRandom){

                shakerCallBack.sendMessage(COPY_RANDOM, "Random copy");

            }else if (e.getSource() == copyAll){

                shakerCallBack.sendMessage(COPY_ALL, "Copy all");

            }else if (e.getSource() == shakeIt){

                shakerCallBack.sendMessage(START, "Start program");

//            progressBar.setValue(0);
//            shakeIt.setEnabled(false);
//            Thread animationWait = new AnimationWait();
//            animationWait.start();
//            Thread thread = new Thread(musicShaker);
//            thread.start();

        }
    }

    private void selectPath(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(chooserTitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (e.getSource() == selectTarget) {
                pathT.setText(chooser.getSelectedFile().getPath());
                shakerCallBack.sendMessage(SELECT_PATH_TARGET, chooser.getSelectedFile().getPath());

            } else if (e.getSource() == selectSource) {
                pathS.setText(chooser.getSelectedFile().getPath());
                shakerCallBack.sendMessage(SELECT_PATH_SOURCE, chooser.getSelectedFile().getPath());

            }
        } else {
            System.out.println("No Selection ");
        }
    }

    public Dimension getPreferredSize(){
        return new Dimension(800, 480);
    }

    public void updateProgress(String fileName, int percent){
        copyFileName.setText(fileName);
        progressBar.setValue(percent);
    }

    public JTextArea getArea() {
        return area;
    }

    public JButton getShakeIt() {
        return shakeIt;
    }

    public JButton getStartScan() {
        return startScan;
    }
}
