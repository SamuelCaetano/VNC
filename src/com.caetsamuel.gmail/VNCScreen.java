package com.caetsamuel.gmail;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapCursorCollection;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapView;
import org.inventivetalent.mapmanager.MapManagerPlugin;
import org.inventivetalent.mapmanager.controller.MapController;
import org.inventivetalent.mapmanager.controller.MultiMapController;
import org.inventivetalent.mapmanager.manager.MapManager;
import org.inventivetalent.mapmanager.wrapper.MapWrapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class VNCScreen implements Runnable {

    private MapManager mapManager = ((MapManagerPlugin) Bukkit.getPluginManager().getPlugin("MapManager")).getMapManager();

    private Thread thread;

    private boolean render = false;
    private boolean running = false;

    private ItemFrame[][] frames = new ItemFrame[10][15];

    private final double FRAME_CAP = 1.0/70.0;//70 FPS

    private File file;

    private Robot robot;

    private World world;

    public VNCScreen(Location f1, Location f2, BlockFace bf) { //Need to add the source stream as an argument
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        world = f1.getWorld();

        System.out.println("Spawning FRAMES!");

        int minX = Math.min(f1.getBlockX(), f2.getBlockX());
        int maxX = Math.max(f1.getBlockX(), f2.getBlockX());
        int minY = Math.min(f1.getBlockY(), f2.getBlockY());
        int maxY = Math.max(f1.getBlockY(), f2.getBlockY());
        int minZ = Math.min(f1.getBlockZ(), f2.getBlockZ());
        int maxZ = Math.max(f1.getBlockZ(), f2.getBlockZ());

        Location loc = new Location(world,0, 0, 0);

        int row = 0;
        int column = 0;

        //Add Frames
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    loc.setX(x);
                    loc.setY(y);
                    loc.setZ(z);

                    ItemFrame frame;

                    world.getBlockAt(loc.getBlock().getRelative(bf).getLocation()).setType(Material.AIR);

                    /*
                    for (Entity entity : world.getNearbyEntities(loc, 2, 2, 2)) {
                        if (entity instanceof ItemFrame && entity.getLocation().getBlock().getRelative(((ItemFrame) entity).getAttachedFace()).equals(world.getBlockAt(loc))) {
                            frame = (ItemFrame) entity;
                            System.out.println("SKIPPING frame at: " + frame.getLocation().toString());
                        }
                     */
                    frame = world.spawn(loc.getBlock().getRelative(bf).getLocation(), ItemFrame.class);

                    frames[row][column] = frame;

                    if(row < 9){
                        row++;
                    }
                    else if(column < 14){
                        row = 0;
                        column++;
                    }
                }
            }
        }

        try {
//robot.createScreenCapture(new Rectangle(1920, 1080))
            Player player = Bukkit.getPlayer("SecondAmendment");
            MapWrapper mapWrapper = mapManager.wrapMultiImage(ImageIO.read(new File("C:/Users/Samuel Caetano/Desktop/Recording/red.jpg")), 15, 10);
            MultiMapController mapController = (MultiMapController)mapWrapper.getController();
            mapController.addViewer(player);
            mapController.sendContent(player);

            System.out.println("Frames.length = " + frames.length);
            System.out.println("Frames[0].length = " + frames[0].length);

            for (int r = 0; r<frames.length; r++){
                for (int c = 0; c<frames[0].length; c++){
                    System.out.println("Rows: " + r + "Columns: " + c);
                    ItemStack map = new ItemStack(Material.MAP);
                    map.setDurability(mapController.getMapId(Bukkit.getOfflinePlayer(player.getUniqueId())));
                    frames[r][c].setItem(map);
                }
            }

            System.out.println("GOT TO THIS POINT!");
            mapController.showInFrames(player, frames);
            System.out.println("IF YOU DONT SEE THIS IT MEANS THE FRAMES WERE NEVER SHOWN!");

            /*
            for(ItemFrame f : frames){
                ItemStack mapStack = new ItemStack(Material.MAP);
                ItemStack map = new ItemStack(Material.MAP);
                map.setDurability(mapController.getMapId(Bukkit.getOfflinePlayer(player.getUniqueId())));
                f.setItem(map);
                mapController.showInFrame(player, f, true);

            }*/
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
