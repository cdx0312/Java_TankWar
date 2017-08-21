package com.tank;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Missile {
    private int x;
    private int y;
    private boolean good;
    private static final int XSPEED = 10;
    private static final int YSPEED = 10;
    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;
    private boolean Live = true;
    private TankClient tankClient;

    public boolean isLive() {
        return Live;
    }

    Direction direction;

    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static Image[] missileImages = null;
    private static Map<String, Image> imgs = new HashMap<>();
    static {
        missileImages = new Image[] {
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/missileL.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/missileLU.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/missileD.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/missileRU.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/missileR.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/missileRD.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/MissileU.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/missileLD.gif")),
        };
        imgs.put("L", missileImages[0]);
        imgs.put("LU", missileImages[1]);
        imgs.put("U", missileImages[2]);
        imgs.put("RU", missileImages[3]);
        imgs.put("R", missileImages[4]);
        imgs.put("RD", missileImages[5]);
        imgs.put("D", missileImages[6]);
        imgs.put("LD", missileImages[7]);
    }


    public Missile(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Missile(int x, int y, boolean good, Direction direction, TankClient tankClient) {
        this(x,y,direction);
        this.good = good;
        this.tankClient = tankClient;
    }

    public void draw(Graphics graphics) {
        if (!Live) {
            tankClient.missiles.remove(this);
            return;
        }
        switch (direction) {
            case L:
                graphics.drawImage(imgs.get("L"),x,y,null);
                break;
            case LU:
                graphics.drawImage(imgs.get("LU"),x,y,null);
                break;
            case U:
                graphics.drawImage(imgs.get("U"),x,y,null);
                break;
            case RU:
                graphics.drawImage(imgs.get("RU"),x,y,null);
                break;
            case R:
                graphics.drawImage(imgs.get("R"),x,y,null);
                break;
            case RD:
                graphics.drawImage(imgs.get("RD"),x,y,null);
                break;
            case D:
                graphics.drawImage(imgs.get("D"),x,y,null);
                break;
            case LD:
                graphics.drawImage(imgs.get("LD"),x,y,null);
        }

        move();
    }

    private void move() {
        switch (direction) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
        }
        if (x < 0 || y < 0 || x > TankClient.GAMEWIDTH || y > TankClient.GAMEHEIGHT) {
            Live = false;
            tankClient.missiles.remove(this);
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public boolean hitTank(Tank tank) {
        if (this.Live && this.getRect().intersects(tank.getRect()) && tank.isLive() && this.good != tank.isGood()) {
            if (tank.isGood()) {
                tank.setLife(tank.getLife() - 20);
                if (tank.getLife() <= 0) {
                    tank.setLive(false);
                }
            } else {
                tank.setLive(false);
            }
            this.Live = false;
            Explode explode = new Explode(x,y,tankClient);
            tankClient.explodes.add(explode);
            return true;
        }
        return false;
    }

    public boolean hitTanks(java.util.List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            if (hitTank(tanks.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean hitWall (Wall wall) {
        if (this.Live && this.getRect().intersects(wall.getRect())) {
            this.Live = false;
            return true;
        }
        return false;
    }
}
