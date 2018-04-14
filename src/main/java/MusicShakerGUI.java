package main.java;

import main.java.animation.AnimationWait;
import main.java.logic.MusicShaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MusicShakerGUI extends JFrame implements ActionListener {

    private static MusicShaker musicShaker = MusicShaker.getInstance();

    private static final String chooserTitle = "Select folder";
    private static String pathSource = "C:\\MusicShaker\\Input Folder";
    private static String pathTarget = "C:\\MusicShaker\\Output Folder";

    private JButton selectSource, selectTarget, shakeIt;
    private JTextField pathS, pathT;
    private JLabel pathSourceLabel, pathTargetLabel, copyFileName;
    private JFileChooser chooser;
    private JProgressBar progressBar;
    private JTextArea area;

    private JScrollPane jScrollPane;

    public static MusicShakerGUI shaker;




    public static void main(String[] args) {
        shaker = new MusicShakerGUI();

    }

    private MusicShakerGUI(){
        super("MusicShaker");
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
        add(pathT);
        x += 710;

        selectTarget = new JButton("Select");
        selectTarget.setBounds(x, y, 100, 25);
        selectTarget.addActionListener(this);
        add(selectTarget);
        y += 35;
        x = 10;





        y = 475;
        shakeIt = new JButton("Start!");
        shakeIt.setBounds(x, y, 100, 25);
        shakeIt.addActionListener(this);
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
        }else if (e.getSource() == shakeIt){
            System.out.println("I shake it!");
            progressBar.setValue(0);
            shakeIt.setEnabled(false);
            Thread animationWait = new AnimationWait();
            animationWait.start();
            Thread thread = new Thread(musicShaker);
            thread.start();
        }
    }

    private void selectPath(ActionEvent e){
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(chooserTitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (e.getSource() == selectTarget) {
                pathTarget = chooser.getSelectedFile().getPath();
                musicShaker.setPathTarget(pathTarget);
                pathT.setText(pathTarget);
                System.out.println("Select target folder: " + pathTarget);
            }else if (e.getSource() == selectSource){
                pathSource = chooser.getSelectedFile().getPath();
                musicShaker.setPathSource(pathSource);
                pathS.setText(pathSource);
                System.out.println("Select source folder: " + pathSource);
            }
        }
        else {
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
}
