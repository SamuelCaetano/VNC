package com.caetsamuel.gmail;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.inventivetalent.mapmanager.MapManagerPlugin;
import org.inventivetalent.mapmanager.controller.MapController;
import org.inventivetalent.mapmanager.manager.MapManager;
import org.inventivetalent.mapmanager.wrapper.MapWrapper;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class VNCScreen implements Runnable {

    private MapManager mapManager = ((MapManagerPlugin) Bukkit.getPluginManager().getPlugin("MapManager")).getMapManager();

    private Thread thread;

    private boolean render = false;
    private boolean running = false;

    private ArrayList<ItemFrame> frames = new ArrayList<>();

    private final double FRAME_CAP = 1.0/70.0;//70 FPS

    private File file;

    private Robot robot;

    private World world;

    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public VNCScreen(Location f1, Location f2) { //Need to add the source stream as an argument
        world = f1.getWorld();

        System.out.println("Spawning FRAMES!");


        //This doesnt get run for some reason and skips straight to the try/catch. (So it doesnt spawn the frames or store them in the arraylist)
        for(int z = 0; z < (f2.getBlockZ() - f1.getBlockZ()); z++) {
            for (int y = 0; y < (f2.getBlockY() - f1.getBlockY()); y++) {
                for (int x = 0; x < (f2.getBlockX() - f1.getBlockX()); x++) {

                    Location loc = new Location(world, f1.getBlockX() + x, f1.getBlockY() + y, f1.getBlockZ() + z);
                    System.out.println("frame at: " + loc.toString());

                    ItemFrame frame = world.spawn(loc, ItemFrame.class);
                    frames.add(frame);
                }
            }
        }

        try {
            MapWrapper mapWrapper = mapManager.wrapImage(robot.createScreenCapture(new Rectangle(1920, 1080)));

            Player p = Bukkit.getPlayer("SecondAmendment");

            MapController mapController = mapWrapper.getController();
            mapController.addViewer(p);
            mapController.sendContent(p);
            mapController.showInFrame(p, frames.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //start();
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
