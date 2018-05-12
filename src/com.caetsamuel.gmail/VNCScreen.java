package com.caetsamuel.gmail;

import org.bukkit.Bukkit;
import org.inventivetalent.animatedframes.AnimatedFrame;
import org.inventivetalent.animatedframes.AnimatedFramesPlugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VNCScreen implements Runnable {

    private AnimatedFramesPlugin plugin = (AnimatedFramesPlugin) Bukkit.getPluginManager().getPlugin("AnimatedFrames");

    private Thread thread;

    private boolean render = false;
    private boolean running = false;

    private final double FRAME_CAP = 1.0/70.0;

    private AnimatedFrame frame;

    private File file;

    private Robot robot;

    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public VNCScreen(AnimatedFrame frame) { //Need to add the source stream as an argument
        file = new File("C:\\Users\\Samuel Caetano\\Desktop\\Recording\\screen1.jpg");
        start();
    }

    public void start() {
        thread = new Thread(this);
        thread.run();
    }

    public void stop(){
        running = false;
    }

    public void run(){
        running = true;

        double initTime = 0;
        double lastTime = System.nanoTime() / 1000000000.0;
        double elapsedTime = 0;
        double unprocessedTime = 0;

        while(running){
            render = false;

            initTime = System.nanoTime() / 1000000000.0;
            elapsedTime = initTime - lastTime;
            lastTime = initTime;

            unprocessedTime += elapsedTime;

            while(unprocessedTime >= FRAME_CAP)
            {
                unprocessedTime -= FRAME_CAP;
                render = true;

                //Obtain Screen Updates
                BufferedImage screenshot = robot.createScreenCapture(new Rectangle(1920, 1080));
                try {
                    ImageIO.write(screenshot, "jpg", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(render){
                //Render Screen Update
            }
            else{
                try{
                    Thread.sleep(1);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
