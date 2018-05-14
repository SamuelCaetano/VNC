package com.caetsamuel.gmail;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
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

        int minX = Math.min(f1.getBlockX(), f2.getBlockX());
        int maxX = Math.max(f1.getBlockX(), f2.getBlockX());
        int minY = Math.min(f1.getBlockY(), f2.getBlockY());
        int maxY = Math.max(f1.getBlockY(), f2.getBlockY());
        int minZ = Math.min(f1.getBlockZ(), f2.getBlockZ());
        int maxZ = Math.max(f1.getBlockZ(), f2.getBlockZ());

        Location loc = new Location(world,0, 0, 0);

        //Add Frames
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    loc.setX(x);
                    loc.setY(y);
                    loc.setZ(z);

                    for (Entity entity : world.getNearbyEntities(loc, 2, 2, 2)) {
                        if (entity instanceof ItemFrame && entity.getLocation().getBlock().getRelative(((ItemFrame) entity).getAttachedFace()).equals(world.getBlockAt(loc))) {
                            ItemFrame itemFrame = (ItemFrame) entity;
                            itemFrame.remove();
                        }
                    }

                    world.getBlockAt(loc).setType(Material.AIR);

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

    public Entity getEntityByLocation(Location loc){
        for(Entity e : loc.getWorld().getEntities()){
            if(e.getLocation().distanceSquared(loc) <= 1.0){
                System.out.println("ANYTHING HERE: " + e.getType());
                return e;
            }
        }
        return null;
    }

}
