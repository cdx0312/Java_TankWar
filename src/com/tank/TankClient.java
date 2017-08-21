package com.tank;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class TankClient extends Frame{
    public static final int GAMEWIDTH = 800;
    public static final int GAMEHEIGHT = 600;
    Wall wall1 = new Wall(100, 200, 20, 150, this);
    Wall wall2 = new Wall(300, 100, 300, 20, this);
    Tank myTank = new Tank(50, 50,true,Direction.STOP, this);
    java.util.List<Missile> missiles = new ArrayList<>();
    List<Explode> explodes = new ArrayList<>();
    List<Tank> tanks = new ArrayList<>();
    Image offScreenImage = null;

    Blood blood = new Blood();

    public void paint(Graphics graphics) {
        graphics.drawString("missles count: " + missiles.size(), 10, 50);
        graphics.drawString("expolodes count: " + explodes.size(), 400, 50);
        graphics.drawString("enermy tanks : " + tanks.size(), 600,50);
        graphics.drawString("tank life: " + this.myTank.getLife(), 200,50);
        if (tanks.size() <= 0) {
            for (int i = 0; i < Integer.parseInt(PropertyMgr.getProperty("reProduceTankCount")); i++) {
                tanks.add(new Tank(50 + 40 * (i+1), 50, false, Direction.D,this));
            }
        }
        myTank.draw(graphics);
        wall1.draw(graphics);
        wall2.draw(graphics);
        myTank.collideWithWall(wall1);
        myTank.collideWithWall(wall2);
        myTank.eat(blood);
        blood.draw(graphics);

        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i);
            tank.collideWithWall(wall2);
            tank.collideWithWall(wall1);
            tank.collidesWithTanks(tanks);
            tank.draw(graphics);
        }

        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(graphics);
        }
        for (int i = 0; i < missiles.size(); i++) {
            Missile missile = missiles.get(i);
            missile.hitTanks(tanks);
            missile.hitTank(myTank);
            missile.hitWall(wall1);
            missile.hitWall(wall2);
//            if (!missile.isLive())
//                missiles.remove(missile);
//            else
//                missile.draw(graphics);
            missile.draw(graphics);

        }
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(GAMEWIDTH,GAMEHEIGHT);
        }
        Graphics graphicsOffScreen = offScreenImage.getGraphics();
        Color c = graphicsOffScreen.getColor();
        graphicsOffScreen.setColor(Color.GRAY);
        graphicsOffScreen.fillRect(0,0,GAMEWIDTH, GAMEHEIGHT);
        graphicsOffScreen.setColor(c);
        paint(graphicsOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    public void lauchFrame() {

        int initTankCount = Integer.parseInt(PropertyMgr.getProperty("initTankCount"));

        for (int i = 0; i < initTankCount; i++) {
            tanks.add(new Tank(50 + 40 * (i+1), 50, false,Direction.D,this));
        }
        this.setLocation(400,300);
        this.setSize(GAMEWIDTH, GAMEHEIGHT);
        //匿名类实现窗口的关闭
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setTitle("TankWar");
        setVisible(true);
        this.setBackground(Color.GRAY);
        this.setResizable(false);
        this.addKeyListener(new KeyMonitor());
        new Thread(new PaintThread()).start();
    }



    public static void main(String[] args) {
        TankClient tankClient = new TankClient();
        tankClient.lauchFrame();
    }

    private class PaintThread implements Runnable {

        @Override
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
                myTank.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }
}